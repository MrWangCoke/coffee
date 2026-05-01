(function () {
  // Supabase 项目配置：当前页面所有登录、查询、上传都走这个客户端。
  const SUPABASE_URL = 'https://aloziqbumeyxjukxfiyb.supabase.co';
  const SUPABASE_KEY =
    'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFsb3ppcWJ1bWV5eGp1a3hmaXliIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzY0ODE2NjUsImV4cCI6MjA5MjA1NzY2NX0.GoMBa3iYqt2oQlKwDnjlYzCpFZh771dp-Fp6GsAn8Nk';

  const client = window.supabase?.createClient?.(SUPABASE_URL, SUPABASE_KEY);

  window.MerchantApi = {
    // 获取浏览器里缓存的登录会话，用于刷新页面后自动进入后台。
    async getSession() {
      if (!client) return null;
      const { data } = await client.auth.getSession();
      return data.session;
    },

    // 邮箱密码登录。
    async login(email, password) {
      if (!client) {
        return { data: null, error: new Error('Supabase SDK 未加载，请检查网络或 CDN 连接。') };
      }
      return client.auth.signInWithPassword({ email, password });
    },

    // 退出登录并清理 Supabase 会话。
    async logout() {
      if (!client) return { error: null };
      return client.auth.signOut();
    },

    // 只拉取当前商家的商品，依赖 merchant_id 做数据隔离。
    async fetchProducts(merchantId) {
      if (!client) return { data: [], error: null };
      return client
        .from('products')
        .select('*')
        .eq('merchant_id', merchantId)
        .order('created_at', { ascending: false });
    },

    // 上传商品图片到 product-images bucket，并返回公开访问地址。
    async uploadProductImage(file, merchantId) {
      if (!client) {
        return { data: null, error: new Error('Supabase SDK 未加载，已切换为本地图片预览。') };
      }

      const ext = file.name.split('.').pop() || 'jpg';
      const fileName = `${merchantId}/${Date.now()}-${Math.random().toString(16).slice(2)}.${ext}`;
      const { error } = await client.storage
        .from('product-images')
        .upload(fileName, file, {
          cacheControl: '3600',
          contentType: file.type || 'image/jpeg',
          upsert: false,
        });

      if (error) return { data: null, error };

      const { data } = client.storage.from('product-images').getPublicUrl(fileName);
      return { data: { url: data.publicUrl }, error: null };
    },

    // 创建商品记录；表字段变化时，上层 app.js 会做降级重试。
    async createProduct(payload) {
      if (!client) return { data: payload, error: null };
      return client.from('products').insert(payload).select().single();
    },

    // 登录状态监听，用于同步 Supabase Auth 的变化。
    onAuthStateChange(callback) {
      if (!client) return null;
      return client.auth.onAuthStateChange((_event, session) => callback(session));
    },
  };
})();
