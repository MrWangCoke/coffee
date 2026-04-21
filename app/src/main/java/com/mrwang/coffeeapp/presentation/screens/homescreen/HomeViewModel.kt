package com.mrwang.coffeeapp.presentation.screens.homescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.mrwang.coffeeapp.data.local.AppDatabase
import com.mrwang.coffeeapp.data.repository.CoffeeRepository
import com.mrwang.coffeeapp.domain.model.Product
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CoffeeRepository(AppDatabase.getInstance(application))

    // ViewModel 里会有这些数据：
    val categories = homeCategories
    var selectedCategory = "全部" // 默认选中全部
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var preloadImagesJob: Job? = null

    init {
        observeProducts()
        observeBanners()
        refreshHomeContent()
    }

    fun refreshHomeContent() {
        refreshProducts()
        refreshBanners()
    }

    private fun observeProducts() {
        viewModelScope.launch {
            repository.productsFlow.collectLatest { products ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoadingProducts = false,
                        allProducts = products,
                        displayedProducts = filterProducts(
                            products = products,
                            category = currentState.selectedCategory,
                            query = currentState.searchQuery
                        )
                    )
                }
                preloadImages(products.mapNotNull { it.imageUrl })
            }
        }
    }

    private fun observeBanners() {
        viewModelScope.launch {
            repository.bannerUrlsFlow.collectLatest { bannerUrls ->
                _uiState.update {
                    it.copy(
                        isLoadingBanners = false,
                        bannerUrls = bannerUrls
                    )
                }
                preloadImages(bannerUrls)
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

    private fun refreshProducts() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingProducts = it.allProducts.isEmpty(),
                    isRefreshing = true,
                    errorMessage = null
                )
            }
            val result = repository.refreshProducts()
            if (result.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoadingProducts = false,
                        isRefreshing = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to refresh products"
                    )
                }
            } else {
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    private fun refreshBanners() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingBanners = it.bannerUrls.isEmpty(),
                    errorMessage = null
                )
            }
            val result = repository.refreshBanners()
            if (result.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoadingBanners = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "Failed to refresh banners"
                    )
                }
            }
        }
    }

    private fun preloadImages(urls: List<String>) {
        val application = getApplication<Application>()
        preloadImagesJob?.cancel()
        preloadImagesJob = viewModelScope.launch {
            urls.distinct().forEach { imageUrl ->
                val request = ImageRequest.Builder(application)
                    .data(imageUrl)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .build()
                runCatching {
                    application.imageLoader.enqueue(request)
                }
            }
        }
    }
}
