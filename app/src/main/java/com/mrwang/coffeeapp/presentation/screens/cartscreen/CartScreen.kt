package com.mrwang.coffeeapp.presentation.screens.cartscreen

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.mrwang.coffeeapp.presentation.navigation.Routes
import com.mrwang.coffeeapp.presentation.settings.AppLanguage
import com.mrwang.coffeeapp.presentation.settings.AppSettingsViewModel
import com.mrwang.coffeeapp.presentation.screens.profilescreen.UserViewModel
import com.mrwang.coffeeapp.presentation.screens.shop.ProductSelection
import com.mrwang.coffeeapp.presentation.screens.shop.ShopViewModel
import com.mrwang.coffeeapp.presentation.theme.LightBrown
import com.mrwang.coffeeapp.presentation.ui_components.MyButtonNavBar

@Composable
fun CartScreen(
    navController: NavController,
    shopViewModel: ShopViewModel,
    userViewModel: UserViewModel,
    appSettingsViewModel: AppSettingsViewModel
) {
    val shopUiState by shopViewModel.uiState.collectAsState()
    val userId by userViewModel.userId.collectAsState()
    val accessToken by userViewModel.accessToken.collectAsState()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()
    val language by appSettingsViewModel.language.collectAsState()
    val isChinese = language == AppLanguage.Chinese
    val amount = shopUiState.cartItems.sumOf { (it.products?.price ?: 0.0) * it.quantity }
    val delivery = if (shopUiState.cartItems.isEmpty()) 0.0 else 1.0
    val totalAmount = amount + delivery

    LaunchedEffect(userId, accessToken) {
        shopViewModel.loadCart(userId, accessToken)
    }

    Scaffold(
        topBar = { CartScreenToAppBar(title = if (isChinese) "购物车" else "Cart") },
        bottomBar = { MyButtonNavBar(navController=navController,"Cart")}
    ){innerPadding->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(start =16.dp,end =16.dp)
                .padding(innerPadding)
        ){
            item {
                Row {
                    Text(text= if (isChinese) "购物车" else "Cart",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightBrown
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (!isLoggedIn) {
                    Text(
                        text = if (isChinese) "请先登录后使用购物车。" else "Please log in to use the cart.",
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { navController.navigate(Routes.LoginScreen) },
                        colors = ButtonDefaults.buttonColors(containerColor = LightBrown)
                    ) {
                        Text(if (isChinese) "登录" else "Log In")
                    }
                    return@item
                }

                if (shopUiState.isCartLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LightBrown)
                    }
                } else if (shopUiState.cartItems.isEmpty()) {
                    Text(
                        text = if (isChinese) "购物车是空的。" else "Your cart is empty.",
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                } else {
                    shopUiState.cartItems.forEach { item ->
                        CartItemCard(
                            item = item,
                            selection = shopUiState.cartSelections[item.productId] ?: ProductSelection(),
                            isChinese = isChinese,
                            onIncrease = { shopViewModel.increaseCartItem(item, accessToken) },
                            onDecrease = { shopViewModel.decreaseCartItem(item, accessToken) },
                            onRemove = { shopViewModel.removeCartItem(item, accessToken) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = if (isChinese) "支付摘要" else "Payment Summary",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = if (isChinese) "价格" else "Price", fontSize = 18.sp)
                    Text(text = "¥ ${"%.2f".format(amount)}",fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = if (isChinese) "配送费" else "Delivery Fee",fontSize = 18.sp)
                    Text(text = "¥ ${"%.2f".format(delivery)}",fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                PaymentModeSelectionCard(totalAmount, isChinese)
            }
        }



    }

}
