package com.mrwang.coffeeapp.presentation.screens.homescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mrwang.coffeeapp.R
import com.mrwang.coffeeapp.presentation.settings.AppLanguage
import com.mrwang.coffeeapp.presentation.settings.AppSettingsViewModel
import com.mrwang.coffeeapp.presentation.screens.profilescreen.UserViewModel
import com.mrwang.coffeeapp.presentation.screens.shop.ShopViewModel
import com.mrwang.coffeeapp.presentation.ui_components.AppMessageDialog
import com.mrwang.coffeeapp.presentation.ui_components.MyButtonNavBar
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    location: String = "Bilbao, Spain",
    shopViewModel: ShopViewModel,
    userViewModel: UserViewModel,
    appSettingsViewModel: AppSettingsViewModel,
    // 1. 注入我们写好的 ViewModel
    homeViewModel: HomeViewModel = viewModel()
) {
    // 2. 观察 ViewModel 里的状态广播 (类似调频收音机)
    val uiState by homeViewModel.uiState.collectAsState()
    val shopUiState by shopViewModel.uiState.collectAsState()
    val userId by userViewModel.userId.collectAsState()
    val accessToken by userViewModel.accessToken.collectAsState()
    val language by appSettingsViewModel.language.collectAsState()
    val isChinese = language == AppLanguage.Chinese
    val shopMessage = shopUiState.errorMessage ?: shopUiState.message

    Scaffold(
        bottomBar = { MyButtonNavBar(navController = navController,"Home") }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0C0F14))
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.66f)
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
            )

            ProductGrid(
                products = uiState.displayedProducts, // 👈 关键修改：直接使用状态里的产品列表
                navController = navController,
                favouriteProductIds = shopUiState.favouriteProducts.map { it.id }.toSet(),
                onToggleFavourite = shopViewModel::toggleFavourite,
                onAddToCart = { product ->
                    shopViewModel.addToCart(
                        product = product,
                        userId = userId,
                        accessToken = accessToken
                    )
                },
                categoryContent = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp)
                    ) {
                        HomeScreenCategories(
                            categories = uiState.categories,
                            selectedCategory = uiState.selectedCategory,
                            language = language,
                            onCategorySelected = homeViewModel::onCategorySelected
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0C0F14))
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    // --- 顶部位置选择与搜索框保持不变 ---
                    Text(text = if (isChinese) "位置" else "Location", color = Color.Gray, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(location, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Change", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    MySearchBar(
                        searchText = uiState.searchQuery,
                        onSearchTextChanged = homeViewModel::onSearchQueryChanged
                    )
                    Spacer(modifier = Modifier.height(40.dp))

                    // 3. 【重点修改区域】动态 Banner 逻辑
                    if (uiState.isLoadingBanners) {
                        // 如果正在从网上拉取链接，显示一个简单的加载提示
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp).padding(8.dp),
                            color = Color(0xFFD17842)
                        )
                    } else if (uiState.bannerUrls.isNotEmpty()) {
                        // 只有当网络请求成功拿到链接后，才显示轮播图
                        val bannerCount = uiState.bannerUrls.size
                        val pagerState = rememberPagerState(pageCount = { bannerCount })

                        // 自动轮播计时器
                        LaunchedEffect(bannerCount) {
                            if (bannerCount > 1) {
                                while (true) {
                                    delay(3000)
                                    val nextPage = (pagerState.currentPage + 1) % bannerCount
                                    pagerState.animateScrollToPage(nextPage)
                                }
                            }
                        }

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                // 使用 Crop 铺满广告位，比例不同的图片会被裁切但不会留黑边。
                                AsyncImage(
                                    model = uiState.bannerUrls[page],
                                    contentDescription = "Coffee Banner",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                    placeholder = painterResource(R.drawable.banner_1),
                                    error = painterResource(R.drawable.banner_1)
                                )
                            }
                        }
                    } else {
                        // 如果彻底没网或出错，用你原来的本地图片兜底
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.banner_1),
                                contentDescription = "Home Banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

            }
        }
    }

    AppMessageDialog(
        show = shopMessage != null,
        title = if (shopUiState.errorMessage != null) {
            if (isChinese) "提示" else "Notice"
        } else {
            if (isChinese) "成功" else "Success"
        },
        message = shopMessage.orEmpty(),
        onDismiss = shopViewModel::clearMessages
    )
}
