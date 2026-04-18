package com.mrwang.coffeeapp.presentation.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 专门为 Banner 定义的状态
data class HomeUiState(
    val isLoadingBanners: Boolean = true,
    val bannerUrls: List<String> = emptyList(), // 存放网络下载下来的链接
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // ViewModel 一启动，就开始去网上拉取 Banner 数据
        fetchBanners()
    }

    private fun fetchBanners() {
        viewModelScope.launch {
            // 1. 告诉 UI，开始转圈圈加载啦
            _uiState.update { it.copy(isLoadingBanners = true) }

            // 2. 模拟网络请求的耗时（假装请求花了 1.5 秒）
            kotlinx.coroutines.delay(1500)

            try {
                // TODO: 等你以后学了后端，这里换成 val urls = NetworkManager.api.getBanners()

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