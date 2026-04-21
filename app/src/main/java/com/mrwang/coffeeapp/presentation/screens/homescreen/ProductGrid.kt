package com.mrwang.coffeeapp.presentation.screens.homescreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mrwang.coffeeapp.domain.model.Product
//产品网格
@Composable
fun ProductGrid(
    products: List<Product>,
    isLoading: Boolean,
    navController: NavController,
    favouriteProductIds: Set<Int>,
    onToggleFavourite: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    categoryContent: @Composable () -> Unit,
    topContent: @Composable () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ){
        item {
            topContent()
        }
        item {
            categoryContent()
        }
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFD17842))
                }
            }
        } else if (products.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "No coffee found",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 40.dp),
                    )
                }
            }
        } else {
            items(products.chunked(2)){rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductCard(
                        product = rowItems[0],
                        modifier = Modifier.weight(1f),
                        navController = navController,
                        isFavourite = rowItems[0].id in favouriteProductIds,
                        onToggleFavourite = onToggleFavourite,
                        onAddToCart = onAddToCart
                    )
                    if (rowItems.size == 2) {
                        ProductCard(
                            product = rowItems[1],
                            modifier = Modifier.weight(1f),
                            navController = navController,
                            isFavourite = rowItems[1].id in favouriteProductIds,
                            onToggleFavourite = onToggleFavourite,
                            onAddToCart = onAddToCart
                        )
                    }else{
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
