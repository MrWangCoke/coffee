package com.mrwang.coffeeapp.presentation.screens.favouritesscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mrwang.coffeeapp.presentation.settings.AppLanguage
import com.mrwang.coffeeapp.presentation.settings.AppSettingsViewModel
import com.mrwang.coffeeapp.presentation.screens.shop.ProductSelection
import com.mrwang.coffeeapp.presentation.screens.shop.ShopViewModel
import com.mrwang.coffeeapp.presentation.ui_components.MyButtonNavBar

@Composable
fun FavouritesScreen(
    navController: NavController,
    shopViewModel: ShopViewModel,
    appSettingsViewModel: AppSettingsViewModel
) {
    val shopUiState by shopViewModel.uiState.collectAsState()
    val language by appSettingsViewModel.language.collectAsState()
    val isChinese = language == AppLanguage.Chinese

    Scaffold(
        topBar = {FavouritesScreenTopBar(title = if (isChinese) "收藏" else "Favourites")},
        bottomBar = { MyButtonNavBar(navController = navController,"Favourite" ) }
    ) { innerPadding->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            item {
                if (shopUiState.favouriteProducts.isEmpty()) {
                    Text(
                        text = if (isChinese) "还没有收藏。" else "No favourites yet.",
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isChinese) "点击商品上的爱心即可保存到这里。" else "Tap the heart on a product to save it here.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                } else {
                    shopUiState.favouriteProducts.forEach {product ->
                        FavouriteItemCard(
                            product = product,
                            selection = shopUiState.favouriteSelections[product.id] ?: ProductSelection(),
                            isChinese = isChinese,
                            onRemove = { shopViewModel.removeFavourite(product) }
                        )
                    }
                }
            }

        }
    }


}
