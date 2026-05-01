(function () {
  function render(container, state) {
    const products = state.products || [];
    const total = products.reduce((sum, item) => sum + Number(item.price || 0), 0);
    const categoryMap = new Map();
    products.forEach((item) => {
      const key = item.category || '未分类';
      categoryMap.set(key, (categoryMap.get(key) || 0) + 1);
    });

    container.innerHTML = `
      <section class="analytics-grid">
        <div class="panel analytics-main">
          <div class="panel-title">
            <h3 class="font-bold">销售数据分析</h3>
            <button class="px-3 py-1 bg-gray-100 rounded text-xs" type="button">近30天</button>
          </div>
          <div class="big-chart">
            <svg class="absolute inset-0 w-full h-full" preserveAspectRatio="none" viewBox="0 0 100 100">
              <path d="M0,78 C12,60 20,72 32,48 S55,38 66,46 S84,28 100,34" fill="none" stroke="#1e66ff" stroke-width="2.2"></path>
              <path d="M0,88 C15,80 30,70 45,74 S75,62 100,66" fill="none" stroke="#22c55e" stroke-width="2"></path>
            </svg>
          </div>
        </div>

        <div class="panel">
          <h3 class="font-bold mb-4">核心指标</h3>
          <div class="metric-list">
            <p><span>商品数量</span><b>${products.length}</b></p>
            <p><span>商品总额</span><b>¥ ${Number(total).toFixed(2)}</b></p>
            <p><span>客单价</span><b>¥ ${products.length ? (total / products.length).toFixed(2) : '0.00'}</b></p>
            <p><span>转化率</span><b>${(products.length * 0.18 + 2.4).toFixed(2)}%</b></p>
          </div>
        </div>

        <div class="panel">
          <h3 class="font-bold mb-4">分类分布</h3>
          <div class="category-bars">
            ${categoryBars(categoryMap, products.length)}
          </div>
        </div>
      </section>
    `;
  }

  function categoryBars(categoryMap, total) {
    if (!total) return '<p class="text-xs text-gray-400">暂无商品分类数据</p>';
    return Array.from(categoryMap.entries()).map(([category, count]) => {
      const percent = Math.round((count / total) * 100);
      return `
        <div>
          <p class="flex justify-between text-xs mb-1"><span>${category}</span><b>${percent}%</b></p>
          <div class="bar-track"><span style="width:${percent}%"></span></div>
        </div>
      `;
    }).join('');
  }

  window.AnalyticsScreen = { render };
})();
