package com.mrwang.coffeeapp.presentation.screens.detailsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mrwang.coffeeapp.presentation.settings.AppLanguage
import com.mrwang.coffeeapp.presentation.settings.AppSettingsViewModel
import com.mrwang.coffeeapp.presentation.screens.profilescreen.UserViewModel
import com.mrwang.coffeeapp.presentation.screens.shop.ShopViewModel
import com.mrwang.coffeeapp.presentation.theme.LightBrown
import com.mrwang.coffeeapp.presentation.theme.LightGray
import com.mrwang.coffeeapp.presentation.ui_components.AppMessageDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    productId: Int, // 👈 完美对接 NavGraph 传过来的 ID
    navController: NavController,
    shopViewModel: ShopViewModel,
    userViewModel: UserViewModel,
    appSettingsViewModel: AppSettingsViewModel,
    viewModel: DetailsViewModel = viewModel() // 注入专属 ViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val shopUiState by shopViewModel.uiState.collectAsState()
    val userId by userViewModel.userId.collectAsState()
    val accessToken by userViewModel.accessToken.collectAsState()
    val language by appSettingsViewModel.language.collectAsState()
    val isChinese = language == AppLanguage.Chinese
    var selectedSize by remember { mutableStateOf("M") }
    var selectedTemperature by remember { mutableStateOf("Hot") }
    val shopMessage = shopUiState.errorMessage ?: shopUiState.message
    val currentProduct = uiState.product
    val isFavourite = currentProduct?.let { product ->
        shopUiState.favouriteProducts.any { it.id == product.id }
    } == true

    // 当这个页面一打开，或者 productId 发生变化时，立刻通知 ViewModel 去拉取数据
    LaunchedEffect(productId) {
        viewModel.fetchProductById(productId)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text(if (isChinese) "商品详情" else "Product Details", color = LightBrown) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = LightBrown)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (currentProduct != null) {
                                shopViewModel.toggleFavourite(
                                    product = currentProduct,
                                    size = selectedSize,
                                    temperature = selectedTemperature
                                )
                            }
                        },
                        enabled = currentProduct != null
                    ) {
                        Icon(
                            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle favourite",
                            tint = if (isFavourite) Color.Red else LightBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                // 判断：只有拿到了数据才显示价格
                val priceText = if (uiState.product != null) "¥ ${uiState.product!!.price}" else "--"

                Column(modifier = Modifier.weight(1f)) {
                    Text(if (isChinese) "价格" else "Price", color = Color.Gray, fontSize = 14.sp)
                    Text(priceText, color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        uiState.product?.let { product ->
                            shopViewModel.addToCart(
                                product = product,
                                userId = userId,
                                accessToken = accessToken,
                                size = selectedSize,
                                temperature = selectedTemperature
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LightBrown),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(50.dp).weight(1.5f)
                ) {
                    Text(if (isChinese) "加入购物车" else "Add to Cart", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // 根据不同的状态显示不同的界面
            if (uiState.isLoading) {
                // 加载中显示大圈圈
                CircularProgressIndicator(
                    color = LightBrown,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.product != null) {
                // 拿到了真实数据，开始渲染
                val product = uiState.product!!

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = LightGray),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            AsyncImage(
                                model = product.imageUrl ?: "",
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(18.dp))
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = product.name,
                                color = Color.Black,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                color = LightBrown.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = product.category,
                                    color = LightBrown,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isChinese) "规格" else "Size",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
                    ) {
                        listOf("S", "M", "L").forEach { size ->
                            SelectSizeChip(
                                sizeText = size,
                                selected = selectedSize == size,
                                onClick = { selectedSize = size },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isChinese) "冷热" else "Temperature",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
                    ) {
                        listOf(
                            "Hot" to if (isChinese) "热" else "Hot",
                            "Iced" to if (isChinese) "冰" else "Iced"
                        ).forEach { (temperature, label) ->
                            SelectSizeChip(
                                sizeText = label,
                                selected = selectedTemperature == temperature,
                                onClick = { selectedTemperature = temperature },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isChinese) "描述" else "Description",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.description ?: if (isChinese) "暂无描述。" else "No description yet.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            } else {
                // 如果出错了或者找不到这个 ID 的商品
                Text(
                    text = uiState.errorMessage ?: if (isChinese) "未找到商品" else "Product not found",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
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
