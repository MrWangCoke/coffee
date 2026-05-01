(function () {
  // 暂存用户选择的本地图片文件和预览地址，提交时交给 app.js 统一处理。
  let selectedFile = null;
  let previewUrl = '';

  // 发布商品页：表单负责收集数据，真正的上传和入库由外层 handlers 处理。
  function render(container, state, handlers) {
    const categories = window.MockData.coffeeCategories;
    selectedFile = null;
    previewUrl = '';

    container.innerHTML = `
      <section class="publish-layout">
        <form id="publish-form" class="panel publish-form">
          <div class="page-header">
            <div>
              <h3 class="text-lg font-bold">发布商品</h3>
              <p class="text-xs text-text-muted mt-1">上传图片，填写咖啡商品信息后发布到商品库。</p>
            </div>
          </div>

          <div class="publish-grid">
            <div>
              <label class="field-label">商品主图</label>
              <button id="upload-trigger" class="upload-box upload-box-large" type="button">
                <div id="upload-placeholder">
                  <i class="fa-solid fa-image text-2xl mb-2"></i>
                  <p class="text-xs leading-relaxed">点击上传商品图片<br>支持 JPG、PNG、WEBP</p>
                </div>
                <img id="image-preview" class="hidden upload-preview" alt="商品图片预览" />
              </button>
              <input id="product-image-input" type="file" accept="image/*" class="hidden" />
            </div>

            <div class="form-stack">
              <div>
                <label for="product-name" class="field-label">商品名称</label>
                <input id="product-name" class="form-input" placeholder="例如：冰美式" required type="text" />
              </div>
              <div>
                <label for="product-category" class="field-label">咖啡分类</label>
                <select id="product-category" class="form-input" required>
                  <option value="">请选择咖啡分类</option>
                  ${categories.map((category) => `<option value="${category}">${category}</option>`).join('')}
                </select>
              </div>
              <div class="grid grid-cols-2 gap-3">
                <div>
                  <label for="product-price" class="field-label">售价</label>
                  <input id="product-price" class="form-input" min="0" step="0.01" placeholder="0.00" required type="number" />
                </div>
                <div>
                  <label for="product-stock" class="field-label">库存</label>
                  <input id="product-stock" class="form-input" min="0" step="1" placeholder="0" type="number" />
                </div>
              </div>
              <div>
                <label for="product-description" class="field-label">商品简介</label>
                <textarea id="product-description" class="form-input min-h-[92px] resize-none" placeholder="口味、烘焙程度、推荐饮用方式等"></textarea>
              </div>
              <div class="form-actions">
                <button class="secondary-button" type="reset">清空</button>
                <button id="publish-submit" class="primary-button" type="submit">
                  <i class="fa-solid fa-paper-plane"></i> 立即发布
                </button>
              </div>
              <p id="publish-message" class="form-message"></p>
            </div>
          </div>
        </form>

        <aside class="panel publish-guide">
          <h4 class="font-bold mb-3">六个咖啡分类</h4>
          <div class="category-chip-list">
            ${categories.map((category) => `<span>${category}</span>`).join('')}
          </div>
        </aside>
      </section>
    `;

    const imageInput = document.getElementById('product-image-input');
    document.getElementById('upload-trigger').addEventListener('click', () => imageInput.click());
    imageInput.addEventListener('change', handleImageChange);
    document.getElementById('publish-form').addEventListener('submit', (event) => handleSubmit(event, handlers));
    document.getElementById('publish-form').addEventListener('reset', () => {
      selectedFile = null;
      previewUrl = '';
      setImagePreview('');
      setMessage('');
    });
  }

  // 选择图片后立刻生成本地预览，不需要等上传完成。
  function handleImageChange(event) {
    const [file] = event.target.files;
    selectedFile = file || null;
    if (!selectedFile) {
      setImagePreview('');
      return;
    }

    previewUrl = URL.createObjectURL(selectedFile);
    setImagePreview(previewUrl);
  }

  // 收集表单数据并做基础校验，通过后交给 app.js 发布商品。
  function handleSubmit(event, handlers) {
    event.preventDefault();
    const payload = {
      name: document.getElementById('product-name').value.trim(),
      category: document.getElementById('product-category').value,
      price: Number(document.getElementById('product-price').value),
      stock: Number(document.getElementById('product-stock').value || 0),
      description: document.getElementById('product-description').value.trim(),
      file: selectedFile,
      previewUrl,
    };

    if (!payload.name || !payload.category || Number.isNaN(payload.price)) {
      setMessage('请补全商品名称、分类和售价。', 'error');
      return;
    }

    handlers.onCreateProduct(payload);
  }

  // 控制上传区域里“占位提示”和“图片预览”的显示状态。
  function setImagePreview(url) {
    const preview = document.getElementById('image-preview');
    const placeholder = document.getElementById('upload-placeholder');
    if (!preview || !placeholder) return;
    preview.classList.toggle('hidden', !url);
    placeholder.classList.toggle('hidden', Boolean(url));
    if (url) preview.src = url;
  }

  // 发布中禁用按钮，避免重复提交。
  function setSubmitting(isSubmitting) {
    const button = document.getElementById('publish-submit');
    if (!button) return;
    button.disabled = isSubmitting;
    button.innerHTML = isSubmitting
      ? '<i class="fa-solid fa-spinner fa-spin"></i> 正在发布...'
      : '<i class="fa-solid fa-paper-plane"></i> 立即发布';
  }

  // 页面底部提示发布状态：处理中、成功或失败。
  function setMessage(text, type = 'info') {
    const message = document.getElementById('publish-message');
    if (!message) return;
    message.textContent = text;
    message.className = `form-message ${type}`;
  }

  window.PublishScreen = { render, setSubmitting, setMessage };
})();
