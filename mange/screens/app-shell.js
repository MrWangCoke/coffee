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
            <button class="ai-upgrade-btn" type="button" aria-label="Ask AI">
              <span class="ai-upgrade-content">
                <svg viewBox="0 0 24 24" height="20" width="20" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                  <g fill="none">
                    <path
                      d="m12.594 23.258l-.012.002l-.071.035l-.02.004l-.014-.004l-.071-.036q-.016-.004-.024.006l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427q-.004-.016-.016-.018m.264-.113l-.014.002l-.184.093l-.01.01l-.003.011l.018.43l.005.012l.008.008l.201.092q.019.005.029-.008l.004-.014l-.034-.614q-.005-.019-.02-.022m-.715.002a.02.02 0 0 0-.027.006l-.006.014l-.034.614q.001.018.017.024l.015-.002l.201-.093l.01-.008l.003-.011l.018-.43l-.003-.012l-.01-.01z"
                    ></path>
                    <path
                      d="M9.107 5.448c.598-1.75 3.016-1.803 3.725-.159l.06.16l.807 2.36a4 4 0 0 0 2.276 2.411l.217.081l2.36.806c1.75.598 1.803 3.016.16 3.725l-.16.06l-2.36.807a4 4 0 0 0-2.412 2.276l-.081.216l-.806 2.361c-.598 1.75-3.016 1.803-3.724.16l-.062-.16l-.806-2.36a4 4 0 0 0-2.276-2.412l-.216-.081l-2.36-.806c-1.751-.598-1.804-3.016-.16-3.724l.16-.062l2.36-.806A4 4 0 0 0 8.22 8.025l.081-.216zM11 6.094l-.806 2.36a6 6 0 0 1-3.49 3.649l-.25.091l-2.36.806l2.36.806a6 6 0 0 1 3.649 3.49l.091.25l.806 2.36l.806-2.36a6 6 0 0 1 3.49-3.649l.25-.09l2.36-.807l-2.36-.806a6 6 0 0 1-3.649-3.49l-.09-.25zM19 2a1 1 0 0 1 .898.56l.048.117l.35 1.026l1.027.35a1 1 0 0 1 .118 1.845l-.118.048l-1.026.35l-.35 1.027a1 1 0 0 1-1.845.117l-.048-.117l-.35-1.026l-1.027-.35a1 1 0 0 1-.118-1.845l.118-.048l1.026-.35l.35-1.027A1 1 0 0 1 19 2"
                      fill="currentColor"
                    ></path>
                  </g>
                </svg>
                Ask AI
              </span>
            </button>
          </div>
        </aside>

        <main class="dashboard-main">
          <header class="topbar">
            <div class="flex items-center gap-8">
              <h2 id="page-title" class="text-lg font-bold">${getTitle(state.view)}</h2>
<!-- From Uiverse.io by ahmedyasserdev --> 
<form class="form relative">
  <button class="absolute left-2 -translate-y-1/2 top-1/2 p-1">
    <svg
      width="17"
      height="16"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      role="img"
      aria-labelledby="search"
      class="w-5 h-5 text-gray-700"
    >
      <path
        d="M7.667 12.667A5.333 5.333 0 107.667 2a5.333 5.333 0 000 10.667zM14.334 14l-2.9-2.9"
        stroke="currentColor"
        stroke-width="1.333"
        stroke-linecap="round"
        stroke-linejoin="round"
      ></path>
    </svg>
  </button>
  <input
    class="input rounded-full px-8 py-3 border-2 border-transparent focus:outline-none focus:border-blue-500 placeholder-gray-400 transition-all duration-300 shadow-md"
    placeholder="Search..."
    required=""
    type="text"
  />
  <button type="reset" class="absolute right-3 -translate-y-1/2 top-1/2 p-1">
    <svg
      xmlns="http://www.w3.org/2000/svg"
      class="w-5 h-5 text-gray-700"
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
    >
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M6 18L18 6M6 6l12 12"
      ></path>
    </svg>
  </button>
</form>

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
