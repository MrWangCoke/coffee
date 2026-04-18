package com.mrwang.coffeeapp.presentation.screens.homescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.mrwang.coffeeapp.domain.model.Product
import com.mrwang.coffeeapp.presentation.ui_components.MyButtonNavBar
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    location: String = "Bilbao, Spain",
    // 1. 注入我们写好的 ViewModel
    homeViewModel: HomeViewModel = viewModel()
) {
    // 2. 观察 ViewModel 里的状态广播 (类似调频收音机)
    val uiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { MyButtonNavBar(navController = navController,"Home") }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0C0F14), Color(0xFF0C0F14))
                    )
                )
                .padding(paddingValues)
        ) {
            // 这里保留你原来的本地产品列表数据（暂不改动）
            val products = listOf(
                Product(1,"Cappuccino","With Oat Milk",25.0,R.drawable.coffee_1),
                Product(2,"Machiato","With Oat Milk",12.0,R.drawable.coffee_2),
                Product(3,"Latte","With Oat Milk",15.0,R.drawable.coffee_3),
                Product(4,"Americano","With Oat Milk",18.0,R.drawable.coffee_4),
                Product(5,"Macchiato","Bold and milky",19.0,R.drawable.coffee_5),
                Product(6,"Flat White","Velvety smooth",22.0,R.drawable.coffee_6),
                Product(7,"Luckin","Refreshing and rich",9.9,R.drawable.coffee_7),
            )

            ProductGrid(products = products, navController = navController) {
                // --- 顶部位置选择与搜索框保持不变 ---
                Text(text = "Location", color = Color.Gray, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(location, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Change", tint = Color.White)
                }
                Spacer(modifier = Modifier.height(30.dp))
                MySearchBar()
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
                    val pagerState = rememberPagerState(pageCount = { uiState.bannerUrls.size })

                    // 自动轮播计时器
                    LaunchedEffect(pagerState.currentPage) {
                        delay(3000)
                        val nextPage = (pagerState.currentPage + 1) % uiState.bannerUrls.size
                        pagerState.animateScrollToPage(nextPage)
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) { page ->
                        // 4. 使用 Coil 的 AsyncImage 替换原来的 Image
                        AsyncImage(
                            model = uiState.bannerUrls[page], // 使用从网络拿到的真实 URL
                            contentDescription = "Coffee Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            placeholder = painterResource(R.drawable.banner_1), // 加载中显示的占位图
                            error = painterResource(R.drawable.banner_1)        // 加载失败显示的图
                        )
                    }
                } else {
                    // 如果彻底没网或出错，用你原来的本地图片兜底
                    Image(
                        painter = painterResource(R.drawable.banner_1),
                        contentDescription = "Home Banner",
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HomeScreenCategories()
            }
        }
    }
}