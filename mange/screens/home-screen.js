(function () {
  function money(value) {
    return Number(value || 0).toFixed(2);
  }

  function render(container, state) {
    const products = state.products || [];
    const total = products.reduce((sum, item) => sum + Number(item.price || 0), 0);
    const avg = products.length ? total / products.length : 0;
    const visitors = products.length * 23 + 568;
    const conversion = visitors ? (products.length / visitors) * 100 : 0;

    container.innerHTML = `
      <div class="metrics-grid">
        ${metricCard('商品总额', `¥ ${money(total)}`, '较昨日 +8.6%', 'fa-coins', '#eff6ff', '#1e66ff')}
        ${metricCard('在售商品', products.length, '较昨日 +2.3%', 'fa-box-open', '#ecfdf5', '#22c55e')}
        ${metricCard('平均售价', `¥ ${money(avg)}`, '较昨日 -1.2%', 'fa-chart-line', '#f5f3ff', '#8b5cf6')}
        ${metricCard('转化率', `${conversion.toFixed(2)}%`, `访客 ${visitors}`, 'fa-chart-pie', '#fff7ed', '#f97316')}
      </div>

      <div class="work-grid">
        <section class="panel trend-panel">
          <div class="panel-title">
            <h3 class="font-bold">销售趋势</h3>
            <div class="flex items-center gap-4 text-xs text-text-muted">
              <span><i class="fa-solid fa-circle text-brand-blue scale-50"></i> 销售额</span>
              <span><i class="fa-solid fa-circle text-success-green scale-50"></i> 订单数</span>
              <button class="px-3 py-1 bg-gray-100 rounded text-xs" type="button">近7天</button>
            </div>
          </div>
          <div class="chart-area">
            <div class="absolute inset-0 grid grid-rows-4 text-[10px] text-gray-400">
              <div class="border-t border-gray-50 pt-1">40,000</div>
              <div class="border-t border-gray-50 pt-1">30,000</div>
              <div class="border-t border-gray-50 pt-1">20,000</div>
              <div class="border-t border-gray-50 pt-1">10,000</div>
            </div>
            <svg class="absolute inset-0 w-full h-full" preserveAspectRatio="none" viewBox="0 0 100 100">
              <path d="M0,74 C16,58 31,52 46,56 S76,38 100,43" fill="none" stroke="#1e66ff" stroke-width="2"></path>
              <path d="M0,84 C15,80 30,70 45,75 S75,65 100,70" fill="none" stroke="#22c55e" stroke-width="2"></path>
            </svg>
            <div class="absolute -bottom-5 w-full flex justify-between text-[10px] text-gray-400 px-2">
              <span>04-22</span><span>04-23</span><span>04-24</span><span>04-25</span><span>04-26</span><span>04-27</span><span>04-28</span>
            </div>
          </div>
        </section>

        <section class="panel source-panel">
          <div class="panel-title">
            <h3 class="font-bold">订单来源</h3>
            <span class="text-xs text-text-muted">实时</span>
          </div>
          <div class="flex h-[190px] items-center justify-center gap-8">
            <div class="relative w-36 h-36">
              <svg class="w-full h-full -rotate-90" viewBox="0 0 36 36">
                <circle cx="18" cy="18" r="16" fill="transparent" stroke="#f3f4f6" stroke-width="4"></circle>
                <circle cx="18" cy="18" r="16" fill="transparent" stroke="#1e66ff" stroke-dasharray="42, 100" stroke-width="4"></circle>
                <circle cx="18" cy="18" r="16" fill="transparent" stroke="#22c55e" stroke-dasharray="23, 100" stroke-dashoffset="-42" stroke-width="4"></circle>
                <circle cx="18" cy="18" r="16" fill="transparent" stroke="#f97316" stroke-dasharray="15, 100" stroke-dashoffset="-65" stroke-width="4"></circle>
              </svg>
              <div class="absolute inset-0 flex flex-col items-center justify-center">
                <span class="text-[10px] text-gray-400">总计来源</span>
                <span class="text-xl font-bold">${Math.max(products.length, 1) * 3}</span>
              </div>
            </div>
            <div class="grid gap-2 text-xs min-w-[120px]">
              <p class="flex justify-between gap-6"><span>微信小程序</span><b>42.4%</b></p>
              <p class="flex justify-between gap-6"><span>抖音小店</span><b>23.3%</b></p>
              <p class="flex justify-between gap-6"><span>支付宝</span><b>15.3%</b></p>
            </div>
          </div>
        </section>

        <section class="panel orders-panel">
          <div class="panel-title">
            <h3 class="font-bold">最近订单</h3>
            <button class="text-brand-blue text-xs" data-view-jump="orders" type="button">查看全部</button>
          </div>
          <table class="orders-table">
            <thead class="text-text-muted border-b border-gray-50">
              <tr>
                <th class="text-left font-medium w-[24%]">订单号</th>
                <th class="text-left font-medium w-[14%]">客户</th>
                <th class="text-left font-medium">商品</th>
                <th class="text-left font-medium w-[14%]">金额</th>
                <th class="text-left font-medium w-[16%]">状态</th>
                <th class="text-left font-medium w-[16%]">时间</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-50">
              ${window.MockData.recentOrders.slice(0, 4).map(orderRow).join('')}
            </tbody>
          </table>
        </section>

        <section class="panel side-summary-panel">
          <div class="grid grid-cols-2 gap-4 h-full">
            <div class="min-w-0">
              <div class="panel-title"><h3 class="font-bold">热销商品</h3></div>
              <div class="summary-list">${hotProducts(products)}</div>
            </div>
            <div class="min-w-0">
              <div class="panel-title"><h3 class="font-bold">待处理</h3></div>
              <div class="todo-stack">${window.MockData.todos.map(todoRow).join('')}</div>
            </div>
          </div>
        </section>
      </div>
    `;
  }

  function metricCard(title, value, trend, icon, bg, color) {
    return `
      <article class="metric-card">
        <div class="flex items-center gap-2 mb-3">
          <span class="metric-icon" style="background:${bg};color:${color}"><i class="fa-solid ${icon}"></i></span>
          <span class="text-xs text-text-muted">${title}</span>
        </div>
        <p class="text-2xl font-bold mb-1">${value}</p>
        <p class="text-xs text-text-muted">${trend}</p>
      </article>
    `;
  }

  function orderRow(order) {
    return `
      <tr class="hover:bg-gray-50 transition-colors">
        <td class="font-mono text-gray-500">${order.id}</td>
        <td>${order.customer}</td>
        <td>${order.product}</td>
        <td class="font-bold">¥ ${order.amount}</td>
        <td><span class="status-badge ${order.badge}">${order.status}</span></td>
        <td class="text-gray-400">${order.time}</td>
      </tr>
    `;
  }

  function hotProducts(products) {
    const list = products.slice(0, 3);
    if (!list.length) return '<p class="text-xs text-gray-400">暂无商品数据</p>';
    return list.map((item, index) => `
      <div class="summary-row">
        <img src="${item.image_url || 'https://via.placeholder.com/80'}" alt="${item.name || '商品'}" />
        <div class="min-w-0">
          <p class="text-xs font-bold truncate">${item.name || '未命名商品'}</p>
          <p class="text-[10px] text-gray-400">已售 ${320 - index * 40} 件</p>
        </div>
        <b class="text-xs">¥ ${money(item.price)}</b>
      </div>
    `).join('');
  }

  function todoRow(todo, index) {
    const themes = ['theme-blue', 'theme-orange', 'theme-purple', 'theme-green', 'theme-red'];
    return `
      <div class="todo-row">
        <span class="todo-icon ${themes[index % themes.length]}"><i class="fa-solid ${todo.icon}"></i></span>
        <span>${todo.label}</span>
        <b>${todo.count}</b>
      </div>
    `;
  }

  window.HomeScreen = { render };
})();
