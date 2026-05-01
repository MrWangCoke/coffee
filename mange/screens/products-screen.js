(function () {
  // 商品管理页：负责展示商品列表，并提供名称搜索和咖啡分类筛选。
  function money(value) {
    return Number(value || 0).toFixed(2);
  }

  function render(container, state, handlers) {
    const products = state.products || [];
    const categories = window.MockData.coffeeCategories;

    container.innerHTML = `
      <section class="panel page-panel">
        <div class="page-header">
          <div>
            <h3 class="text-lg font-bold">商品管理</h3>
            <p class="text-xs text-text-muted mt-1">管理咖啡商品、价格和库存。</p>
          </div>
          <button class="primary-button" data-view-jump="publish" type="button">
            <i class="fa-solid fa-plus"></i> 发布商品
          </button>
        </div>

        <div class="filters-row">
          <input id="product-search" class="filter-input" placeholder="搜索商品名称" type="text" />
          <select id="product-category-filter" class="filter-select">
            <option value="">全部咖啡分类</option>
            ${categories.map((category) => `<option value="${category}">${category}</option>`).join('')}
          </select>
        </div>

        <div id="products-table-wrap" class="table-scroll">
          ${table(products)}
        </div>
      </section>
    `;

    container.querySelector('[data-view-jump="publish"]').addEventListener('click', () => handlers.onNavigate('publish'));
    const search = document.getElementById('product-search');
    const category = document.getElementById('product-category-filter');

    // 筛选只影响当前页面展示，不会修改真实商品数据。
    const update = () => {
      const keyword = search.value.trim();
      const selected = category.value;
      const filtered = products.filter((product) => {
        const matchesName = !keyword || String(product.name || '').includes(keyword);
        const matchesCategory = !selected || product.category === selected;
        return matchesName && matchesCategory;
      });
      document.getElementById('products-table-wrap').innerHTML = table(filtered);
    };
    search.addEventListener('input', update);
    category.addEventListener('change', update);
  }

  function table(products) {
    if (!products.length) {
      return '<div class="empty-state">暂无商品，去发布第一件咖啡商品吧。</div>';
    }

    return `
      <table class="data-table">
        <thead>
          <tr>
            <th>商品</th>
            <th>分类</th>
            <th>价格</th>
            <th>库存</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          ${products.map((product) => `
            <tr>
              <td>
                <div class="table-product">
                  <img src="${product.image_url || 'https://via.placeholder.com/80'}" alt="${product.name || '商品'}" />
                  <span>${product.name || '未命名商品'}</span>
                </div>
              </td>
              <td>${product.category || '未分类'}</td>
              <td class="font-bold text-brand-blue">¥ ${money(product.price)}</td>
              <td>${product.stock ?? '-'}</td>
              <td><span class="status-badge badge-green">在售</span></td>
              <td>
                <button class="table-action" type="button">编辑</button>
                <button class="table-action" type="button">下架</button>
              </td>
            </tr>
          `).join('')}
        </tbody>
      </table>
    `;
  }

  window.ProductsScreen = { render };
})();
