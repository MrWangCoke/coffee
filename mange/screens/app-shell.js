(function () {
  // 左侧导航配置：后续新增页面时，在这里补一个 key 和对应页面模块即可。
  const navItems = [
    { key: 'home', label: '首页', icon: 'fa-house' },
    { key: 'orders', label: '订单管理', icon: 'fa-clipboard-list' },
    { key: 'products', label: '商品管理', icon: 'fa-box' },
    { key: 'publish', label: '发布商品', icon: 'fa-plus-circle' },
    { key: 'analytics', label: '数据分析', icon: 'fa-chart-line' },
  ];

  // 应用外壳只负责公共布局：侧边栏、顶部栏、内容挂载点。
  function render(root, state, handlers) {
    root.innerHTML = `
      <div class="dashboard-shell">
        <aside class="app-sidebar">
          <div class="brand-block">
            <span class="brand-mark">商</span>
            <h1 class="text-base font-bold">商家管理后台</h1>
          </div>

          <nav class="sidebar-nav">
            ${navItems.map((item) => `
              <button class="nav-link ${state.view === item.key ? 'is-active' : ''}" data-view="${item.key}" type="button">
                <i class="fa-solid ${item.icon}"></i>
                <span>${item.label}</span>
              </button>
            `).join('')}
          </nav>

          <div class="sidebar-footer">
            <div class="upgrade-box">
              <p class="text-xs font-bold text-blue-900 mb-1">升级为全能商家</p>
              <p class="text-[10px] text-blue-700 mb-3 leading-tight">享受更多营销功能和服务</p>
              <button class="w-full bg-brand-blue text-white py-2 rounded-lg text-xs font-medium" type="button">立即升级</button>
            </div>
          </div>
        </aside>

        <main class="dashboard-main">
          <header class="topbar">
            <div class="flex items-center gap-8">
              <h2 id="page-title" class="text-lg font-bold">${getTitle(state.view)}</h2>
              <div class="relative">
                <i class="fa-solid fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 text-sm"></i>
                <input class="pl-10 pr-12 py-2 bg-gray-100 border-none rounded-lg text-sm w-80 focus:ring-2 focus:ring-brand-blue" placeholder="搜索订单、商品、客户等" type="text" />
                <span class="absolute right-3 top-1/2 -translate-y-1/2 text-[10px] bg-white border border-gray-200 px-1.5 py-0.5 rounded text-gray-400 font-mono">⌘K</span>
              </div>
            </div>

            <div class="flex items-center gap-5">
              <div class="relative">
                <i class="fa-regular fa-bell text-xl text-gray-600"></i>
                <span class="absolute -top-1 -right-1 bg-red-500 text-white text-[8px] w-4 h-4 rounded-full flex items-center justify-center border-2 border-white">9</span>
              </div>
              <i class="fa-regular fa-comment-dots text-xl text-gray-600"></i>
              <div>
                <p id="merchant-name" class="text-sm font-bold leading-4">${state.session?.user?.email || '美味小铺管理员'}</p>
                <p class="text-[10px] text-yellow-600 leading-4">金牌商家</p>
              </div>
              <button id="logout-btn" class="text-sm text-danger-red bg-red-50 px-3 py-1.5 rounded-lg hover:bg-red-100" type="button">退出</button>
            </div>
          </header>

          <section id="page-container" class="dashboard-content"></section>
        </main>
      </div>
    `;

    root.querySelectorAll('[data-view]').forEach((button) => {
      button.addEventListener('click', () => handlers.onNavigate(button.dataset.view));
    });
    document.getElementById('logout-btn').addEventListener('click', handlers.onLogout);
  }

  // 切换菜单高亮，同时同步顶部标题。
  function setActive(view) {
    document.querySelectorAll('[data-view]').forEach((button) => {
      button.classList.toggle('is-active', button.dataset.view === view);
    });

    const title = document.getElementById('page-title');
    if (title) title.textContent = getTitle(view);
  }

  // 根据 view key 获取页面标题。
  function getTitle(view) {
    return navItems.find((item) => item.key === view)?.label || '首页';
  }

  window.AppShell = { render, setActive, getTitle };
})();
