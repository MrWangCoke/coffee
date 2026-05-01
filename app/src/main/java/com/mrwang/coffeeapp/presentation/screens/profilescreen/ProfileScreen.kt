package com.mrwang.coffeeapp.presentation.screens.profilescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mrwang.coffeeapp.domain.model.OrderItem
import com.mrwang.coffeeapp.presentation.navigation.Routes
import com.mrwang.coffeeapp.presentation.settings.AppLanguage
import com.mrwang.coffeeapp.presentation.settings.AppSettingsViewModel
import com.mrwang.coffeeapp.presentation.screens.shop.ShopViewModel
import com.mrwang.coffeeapp.presentation.theme.LightBrown
import com.mrwang.coffeeapp.presentation.theme.LightGray
import com.mrwang.coffeeapp.presentation.ui_components.MyButtonNavBar

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    appSettingsViewModel: AppSettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    shopViewModel: ShopViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()
    val email by userViewModel.email.collectAsState()
    val userId by userViewModel.userId.collectAsState()
    val accessToken by userViewModel.accessToken.collectAsState()
    val shopUiState by shopViewModel.uiState.collectAsState()
    val language by appSettingsViewModel.language.collectAsState()
    val isChinese = language == AppLanguage.Chinese
    var selectedOrderTab by remember { mutableStateOf("pending") }
    val visibleOrders = if (selectedOrderTab == "pending") {
        shopUiState.pendingOrders
    } else {
        shopUiState.completedOrders
    }

    LaunchedEffect(userId, accessToken) {
        shopViewModel.loadOrderLists(userId, accessToken)
    }

    Scaffold(
        bottomBar = { MyButtonNavBar(navController = navController, "Profile") }
    ) { paddingValues ->
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
                    Text(if (isChinese) "个人中心" else "Profile", color = LightBrown, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = { navController.navigate(Routes.SettingsScreen) },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(LightGray)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = LightBrown
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(LightBrown.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = LightBrown, modifier = Modifier.size(40.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = if (isChinese) {
                                    if (isLoggedIn) "已登录" else "游客"
                                } else {
                                    if (isLoggedIn) "Signed in" else "Guest"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            )
                            Text(
                                text = email ?: if (isChinese) "登录后同步购物车和收藏" else "Log in to sync cart and favourites",
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (isChinese) "订单列表" else "Orders",
                    color = LightBrown,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightGray, RoundedCornerShape(14.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OrderTabButton(
                        title = if (isChinese) "待支付" else "Pending",
                        selected = selectedOrderTab == "pending",
                        modifier = Modifier.weight(1f),
                        onClick = { selectedOrderTab = "pending" }
                    )
                    OrderTabButton(
                        title = if (isChinese) "已完成" else "Completed",
                        selected = selectedOrderTab == "completed",
                        modifier = Modifier.weight(1f),
                        onClick = { selectedOrderTab = "completed" }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (!isLoggedIn) {
                    Text(
                        text = if (isChinese) "登录后查看订单。" else "Log in to view orders.",
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                } else if (shopUiState.isOrdersLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LightBrown)
                    }
                } else if (visibleOrders.isEmpty()) {
                    Text(
                        text = if (selectedOrderTab == "pending") {
                            if (isChinese) "暂无待支付订单。" else "No pending orders."
                        } else {
                            if (isChinese) "暂无已完成订单。" else "No completed orders yet."
                        },
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            items(visibleOrders, key = { it.id }) { order ->
                OrderItemRow(
                    order = order,
                    isChinese = isChinese,
                    isPending = selectedOrderTab == "pending",
                    onPay = { shopViewModel.payPendingOrder(order, userId, accessToken) },
                    onCancel = { shopViewModel.cancelPendingOrder(order, userId, accessToken) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
                        SettingItem(if (isChinese) "个人信息" else "Personal Info")
                        SettingItem(if (isChinese) "支付方式" else "Payment Method")
                        SettingItem(if (isChinese) "消息通知" else "Notifications")
                        SettingItem(
                            title = if (isChinese) "商家订单数据" else "Merchant Orders",
                            onClick = { navController.navigate(Routes.MerchantOrdersScreen) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderTabButton(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) LightBrown else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (selected) Color.White else Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun SettingItem(
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.Black, fontSize = 16.sp)
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = LightBrown)
    }
}

@Composable
fun OrderItemRow(
    order: OrderItem,
    isChinese: Boolean,
    isPending: Boolean = false,
    onPay: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LightGray),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.products?.name ?: if (isChinese) "商品 #${order.productId}" else "Product #${order.productId}",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isChinese) {
                            "数量：${order.quantity} · ${if (isPending) "待支付" else "已完成"}"
                        } else {
                            "Qty: ${order.quantity} · ${if (isPending) "Pending" else "Completed"}"
                        },
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "¥ ${"%.2f".format((order.products?.price ?: 0.0) * order.quantity)}",
                    color = LightBrown,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            if (isPending) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isChinese) "取消" else "Cancel", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onPay,
                        colors = ButtonDefaults.buttonColors(containerColor = LightBrown),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isChinese) "支付" else "Pay", color = Color.White)
                    }
                }
            }
        }
    }
}
