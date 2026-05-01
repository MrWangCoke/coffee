package com.mrwang.coffeeapp.presentation.screens.profilescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mrwang.coffeeapp.presentation.settings.AppLanguage
import com.mrwang.coffeeapp.presentation.settings.AppSettingsViewModel
import com.mrwang.coffeeapp.presentation.screens.shop.ShopViewModel
import com.mrwang.coffeeapp.presentation.theme.LightBrown

@Composable
fun MerchantOrdersScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    appSettingsViewModel: AppSettingsViewModel,
    shopViewModel: ShopViewModel
) {
    val userId by userViewModel.userId.collectAsState()
    val accessToken by userViewModel.accessToken.collectAsState()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()
    val shopUiState by shopViewModel.uiState.collectAsState()
    val language by appSettingsViewModel.language.collectAsState()
    val isChinese = language == AppLanguage.Chinese
    val totalSales = shopUiState.merchantOrders.sumOf { (it.products?.price ?: 0.0) * it.quantity }

    LaunchedEffect(userId, accessToken) {
        shopViewModel.loadMerchantOrders(userId, accessToken)
    }

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isChinese) "商家订单数据" else "Merchant Orders",
                        color = LightBrown,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = LightBrown)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isChinese) {
                        "已完成 ${shopUiState.merchantOrders.size} 单 · 销售额 ¥ ${"%.2f".format(totalSales)}"
                    } else {
                        "${shopUiState.merchantOrders.size} completed · Sales ¥ ${"%.2f".format(totalSales)}"
                    },
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (!isLoggedIn) {
                    Text(
                        text = if (isChinese) "登录后查看商家订单数据。" else "Log in to view merchant order data.",
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                } else if (shopUiState.isMerchantOrdersLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LightBrown)
                    }
                } else if (shopUiState.merchantOrders.isEmpty()) {
                    Text(
                        text = if (isChinese) "暂无已完成订单数据。" else "No completed order data yet.",
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                }
            }

            items(shopUiState.merchantOrders, key = { it.id }) { order ->
                OrderItemRow(order = order, isChinese = isChinese)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
