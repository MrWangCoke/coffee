package com.mrwang.coffeeapp.presentation.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrwang.coffeeapp.data.network.NetworkManager
import com.mrwang.coffeeapp.data.network.SupabaseConfig
import com.mrwang.coffeeapp.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeCategory(
    val key: String,
    val englishLabel: String,
    val chineseLabel: String
)

val homeCategories = listOf(
    HomeCategory(key = "全部", englishLabel = "All", chineseLabel = "全部"),
    HomeCategory(key = "意式", englishLabel = "Espresso", chineseLabel = "意式"),
    HomeCategory(key = "美式", englishLabel = "Americano", chineseLabel = "美式"),
    HomeCategory(key = "拿铁", englishLabel = "Latte", chineseLabel = "拿铁"),
    HomeCategory(key = "卡布奇诺", englishLabel = "Cappuccino", chineseLabel = "卡布奇诺"),
    HomeCategory(key = "摩卡", englishLabel = "Mocha", chineseLabel = "摩卡"),
    HomeCategory(key = "玛奇朵", englishLabel = "Macchiato", chineseLabel = "玛奇朵")
)

// 专门为 Banner 定义的状态
data class HomeUiState(
    val isLoadingBanners: Boolean = true,
    val bannerUrls: List<String> = emptyList(), // 存放网络下载下来的链接
    // 👇 新增：产品列表与分类状态
    val isLoadingProducts: Boolean = true,
    val allProducts: List<Product> = emptyList(),      // 从云端拉下来的所有咖啡
    val displayedProducts: List<Product> = emptyList(),// 当前应该显示在屏幕上的咖啡
    val categories: List<HomeCategory> = homeCategories,
    val selectedCategory: String = "全部",
    val searchQuery: String = "",
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {
    // ViewModel 里会有这些数据：
    val categories = homeCategories
    var selectedCategory = "全部" // 默认选中全部
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // ViewModel 一启动，就开始去网上拉取 Banner 数据
        fetchBanners()
        fetchAllProducts() // 👇 启动时立刻去拉取咖啡列表
    }

    private fun fetchAllProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingProducts = true) }
            try {
                // 向 Supabase 发起请求
                val products = NetworkManager.api.getProducts(
                    apiKey = SupabaseConfig.ANON_KEY,
                    authorization = SupabaseConfig.authorizationHeader
                )

                _uiState.update {
                    it.copy(
                        isLoadingProducts = false,
                        allProducts = products,
                        displayedProducts = filterProducts(
                            products = products,
                            category = it.selectedCategory,
                            query = it.searchQuery
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingProducts = false, errorMessage = e.message) }
            }
        }
    }
    // 👇 处理用户点击分类标签的逻辑
    fun onCategorySelected(category: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategory = category,
                displayedProducts = filterProducts(
                    products = currentState.allProducts,
                    category = category,
                    query = currentState.searchQuery
                )
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                displayedProducts = filterProducts(
                    products = currentState.allProducts,
                    category = currentState.selectedCategory,
                    query = query
                )
            )
        }
    }

    private fun filterProducts(
        products: List<Product>,
        category: String,
        query: String
    ): List<Product> {
        val normalizedQuery = query.trim()

        return products.filter { product ->
            val matchesCategory = category == "全部" || product.category == category
            val matchesQuery = normalizedQuery.isBlank() ||
                product.name.contains(normalizedQuery, ignoreCase = true) ||
                product.category.contains(normalizedQuery, ignoreCase = true) ||
                product.description.orEmpty().contains(normalizedQuery, ignoreCase = true)

            matchesCategory && matchesQuery
        }
    }
    private fun fetchBanners() {
        viewModelScope.launch {
            // 1. 告诉 UI，开始转圈圈加载啦
            _uiState.update { it.copy(isLoadingBanners = true) }

            // 2. 模拟网络请求的耗时（假装请求花了 1.5 秒）
            kotlinx.coroutines.delay(1500)

            try {
                //  等你以后学了后端，这里换成 val urls = NetworkManager.api.getBanners()

                // 3. 模拟服务器成功返回了你的 3 张 Supabase 图片链接！
                // ⚠️ 请把下面这三个网址，换成你刚刚测试成功的那 3 个真实链接！
                val mySupabaseUrls = listOf(
                    "https://aloziqbumeyxjukxfiyb.supabase.co/storage/v1/object/public/coffee-assets/banner_1.png",
                    "https://aloziqbumeyxjukxfiyb.supabase.co/storage/v1/object/public/coffee-assets/pexels-manuel-aldana-321951004-13893740.png",
                    "https://aloziqbumeyxjukxfiyb.supabase.co/storage/v1/object/public/coffee-assets/pexels-becca-mitchell-32568510-7091089.png"
                )

                // 4. 数据拿到了，更新广播状态！
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoadingBanners = false,
                        bannerUrls = mySupabaseUrls
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoadingBanners = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }
}
