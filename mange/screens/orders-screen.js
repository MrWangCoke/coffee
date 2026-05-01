(function () {
  // 订单管理页：目前使用 mock 订单数据，后续可以替换为 Supabase 订单表查询。
  function render(container) {
    const orders = window.MockData.recentOrders;
    container.innerHTML = `
      <section class="panel page-panel">
        <div class="page-header">
          <div>
            <h3 class="text-lg font-bold">订单管理</h3>
            <p class="text-xs text-text-muted mt-1">查看、筛选和处理店铺订单。</p>
          </div>
          <div class="toolbar">
            <button class="toolbar-button is-active" type="button">全部</button>
            <button class="toolbar-button" type="button">待付款</button>
            <button class="toolbar-button" type="button">待发货</button>
            <button class="toolbar-button" type="button">已完成</button>
          </div>
        </div>

        <div class="filters-row">
          <input class="filter-input" placeholder="搜索订单号、客户、商品" type="text" />
          <select class="filter-select">
            <option>全部状态</option>
            <option>待接受</option>
            <option>待付款</option>
            <option>已完成</option>
          </select>
          <button class="primary-soft-button" type="button"><i class="fa-solid fa-filter"></i> 筛选</button>
        </div>

        <div class="table-scroll">
          <table class="data-table">
            <thead>
              <tr>
                <th>订单号</th>
                <th>客户</th>
                <th>商品</th>
                <th>金额</th>
                <th>状态</th>
                <th>下单时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              ${orders.map((order) => `
                <tr>
                  <td class="font-mono text-gray-500">${order.id}</td>
                  <td>${order.customer}</td>
                  <td>${order.product}</td>
                  <td class="font-bold">¥ ${order.amount}</td>
                  <td><span class="status-badge ${order.badge}">${order.status}</span></td>
                  <td class="text-gray-400">${order.time}</td>
                  <td>
                    <button class="table-action" type="button">详情</button>
                    <button class="table-action" type="button">处理</button>
                  </td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        </div>
      </section>
    `;
  }

  window.OrdersScreen = { render };
})();
