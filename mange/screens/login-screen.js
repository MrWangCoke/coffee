(function () {
  function render(root, handlers) {
    root.innerHTML = `
      <main class="login-page">
        <section class="login-shell">
          <div class="login-intro">
            <p class="inline-flex items-center gap-2 rounded-full border border-blue-100 bg-blue-50 px-3 py-1 text-xs font-medium text-blue-700">
              <i class="fa-solid fa-sparkles"></i> 极简运营工作台
            </p>
            <h1 class="mt-5 text-4xl font-bold leading-tight">让商家后台，更安静也更高效。</h1>
            <p class="mt-4 text-sm text-text-muted leading-relaxed max-w-md">
              统一商品、订单与经营数据，把注意力留给真正重要的事情。
            </p>
            <div class="mt-8 grid grid-cols-3 gap-3 max-w-lg">
              <div class="soft-card rounded-lg p-3">
                <p class="text-xs text-text-muted">统一管理</p>
                <p class="mt-2 text-lg font-semibold">订单/商品</p>
              </div>
              <div class="soft-card rounded-lg p-3">
                <p class="text-xs text-text-muted">经营状态</p>
                <p class="mt-2 text-lg font-semibold">实时同步</p>
              </div>
              <div class="soft-card rounded-lg p-3">
                <p class="text-xs text-text-muted">界面风格</p>
                <p class="mt-2 text-lg font-semibold">简约优美</p>
              </div>
            </div>
          </div>

          <div class="login-panel">
            <h2 class="text-xl font-semibold">登录商家后台</h2>
            <p class="text-sm text-text-muted mt-1">欢迎回来，继续经营今天的生意。</p>
            <form id="login-form" class="space-y-5 mt-6">
              <div>
                <label for="email" class="field-label">邮箱账号</label>
                <input id="email" type="email" value="admin@163.com" placeholder="请输入邮箱账号" required class="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-brand-blue focus:outline-none" />
              </div>
              <div>
                <label for="password" class="field-label">密码</label>
                <input id="password" type="password" value="123456" placeholder="请输入密码" required class="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-brand-blue focus:outline-none" />
              </div>
              <button id="login-btn" type="submit" class="w-full bg-brand-blue text-white py-3 rounded-lg font-semibold hover:bg-blue-600 transition-colors disabled:opacity-70">
                进入后台
              </button>
            </form>
          </div>
        </section>
      </main>
    `;

    document.getElementById('login-form').addEventListener('submit', handlers.onLogin);
  }

  function setLoading(isLoading) {
    const button = document.getElementById('login-btn');
    if (!button) return;
    button.textContent = isLoading ? '正在登录...' : '进入后台';
    button.disabled = isLoading;
  }

  window.LoginScreen = { render, setLoading };
})();
