(function () {
  const root = document.getElementById('app-root');

  // 页面路由表：左侧菜单的 view key 会映射到这里的页面模块。
  const pageScreens = {
    home: window.HomeScreen,
    orders: window.OrdersScreen,
    products: window.ProductsScreen,
    publish: window.PublishScreen,
    analytics: window.AnalyticsScreen,
  };

  let currentSession = null;
  let products = [];
  let currentView = 'home';
  let isPreview = false;

  // 应用启动：优先判断预览模式，再判断 Supabase 登录状态。
  async function boot() {
    isPreview = new URLSearchParams(window.location.search).get('preview') === 'dashboard';
    if (isPreview) {
      currentSession = { user: { id: 'preview', email: 'preview@merchant.local' } };
      products = window.MockData.previewProducts.slice();
      showShell();
      return;
    }

    currentSession = await window.MerchantApi.getSession();
    if (currentSession) {
      showShell();
      await loadProducts();
      return;
    }
    showLogin();
  }

  // 渲染登录页，登录成功后再进入后台外壳。
  function showLogin() {
    window.LoginScreen.render(root, { onLogin: handleLogin });
  }

  // 渲染后台外壳：侧边栏和顶部栏只渲染一次，内容区按菜单切换。
  function showShell() {
    window.AppShell.render(root, getState(), {
      onNavigate: navigate,
      onLogout: handleLogout,
    });
    renderCurrentPage();
  }

  // 切换页面时，只更新内容区，避免整页闪动。
  function navigate(view) {
    currentView = view;
    window.AppShell.setActive(view);
    renderCurrentPage();
  }

  // 根据 currentView 渲染当前页面，并绑定页面内跳转事件。
  function renderCurrentPage() {
    const container = document.getElementById('page-container');
    const screen = pageScreens[currentView] || pageScreens.home;
    screen.render(container, getState(), {
      onNavigate: navigate,
      onCreateProduct: handleCreateProduct,
    });

    container.querySelectorAll('[data-view-jump]').forEach((button) => {
      button.addEventListener('click', () => navigate(button.dataset.viewJump));
    });
  }

  function getState() {
    return { session: currentSession, products, view: currentView };
  }

  // 登录表单提交：调用 Supabase Auth，成功后拉取当前商家的商品。
  async function handleLogin(event) {
    event.preventDefault();
    window.LoginScreen.setLoading(true);

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const { data, error } = await window.MerchantApi.login(email, password);

    if (error) {
      alert('登录失败: ' + error.message);
      window.LoginScreen.setLoading(false);
      return;
    }

    currentSession = data.session;
    products = [];
    currentView = 'home';
    showShell();
    await loadProducts();
  }

  // 退出登录后清空本地状态，回到登录页。
  async function handleLogout() {
    await window.MerchantApi.logout();
    currentSession = null;
    products = [];
    currentView = 'home';
    showLogin();
  }

  // 商品数据统一从这里加载，保证所有页面看到的是同一份 products 状态。
  async function loadProducts() {
    if (!currentSession || isPreview) return;

    const { data, error } = await window.MerchantApi.fetchProducts(currentSession.user.id);
    if (error) {
      console.error('获取商品失败:', error);
      alert('获取商品失败，请稍后重试。');
      return;
    }

    products = data || [];
    renderCurrentPage();
  }

  // 发布商品：先处理图片，再写入 Supabase，最后刷新本地商品列表。
  async function handleCreateProduct(formData) {
    window.PublishScreen.setSubmitting(true);
    window.PublishScreen.setMessage('正在处理商品图片和信息...');

    try {
      if (!currentSession) throw new Error('登录状态已失效，请重新登录。');

      const imageUrl = await resolveImageUrl(formData.file, formData.previewUrl);
      const product = {
        merchant_id: currentSession.user.id,
        name: formData.name,
        category: formData.category,
        price: formData.price,
        stock: formData.stock,
        description: formData.description,
        image_url: imageUrl,
      };

      const { data, error } = await createProductWithFallback(product);
      if (error) throw error;

      const created = data || { ...product, id: Date.now(), created_at: new Date().toISOString() };
      products = [created, ...products];
      window.PublishScreen.setMessage('发布成功，已加入商品管理。', 'success');
      setTimeout(() => navigate('products'), 500);
    } catch (error) {
      console.error('发布商品失败:', error);
      window.PublishScreen.setMessage('发布失败：' + error.message, 'error');
    } finally {
      window.PublishScreen.setSubmitting(false);
    }
  }

  // 图片优先上传到 Supabase Storage；预览模式或上传失败时使用本地 data URL 兜底。
  async function resolveImageUrl(file, previewUrl) {
    if (!file) return '';
    if (!isPreview && currentSession) {
      const { data, error } = await window.MerchantApi.uploadProductImage(file, currentSession.user.id);
      if (!error && data?.url) return data.url;
    }
    return readFileAsDataUrl(file).catch(() => previewUrl || '');
  }

  // 如果数据库表暂时没有 category/stock/description 字段，则自动退回核心字段发布。
  async function createProductWithFallback(product) {
    if (isPreview) return { data: product, error: null };

    const result = await window.MerchantApi.createProduct(product);
    if (!result.error) return result;

    const corePayload = {
      merchant_id: product.merchant_id,
      name: product.name,
      price: product.price,
      image_url: product.image_url,
    };
    const retry = await window.MerchantApi.createProduct(corePayload);
    if (!retry.error) {
      return { data: { ...retry.data, category: product.category, stock: product.stock, description: product.description }, error: null };
    }
    return retry;
  }

  // 将本地图片转成 data URL，保证没有 Storage bucket 时仍能看到图片。
  function readFileAsDataUrl(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }

  // 监听登录状态变化；预览模式不让 Supabase 回调覆盖模拟 session。
  window.MerchantApi.onAuthStateChange((session) => {
    if (isPreview) return;
    currentSession = session;
  });

  boot();
})();
