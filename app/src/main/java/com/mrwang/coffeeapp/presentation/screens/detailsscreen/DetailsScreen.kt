package com.mrwang.coffeeapp.presentation.screens.detailsscreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.mrwang.coffeeapp.R
import com.mrwang.coffeeapp.domain.model.Product

@Composable
fun DetailsScreen(productId: Int,navController: NavController) {
    val products =listOf(
        Product(1,"Espresso","Strong and rich",20.0,R.drawable.coffee_1),
        Product(2,"Latte","Smooth and creamy",25.0,R.drawable.coffee_2),
        Product(3,"Cappuccino","With Chocolate",22.0,R.drawable.coffee_3),
        Product(4,"Mocha","With cocos flavor",29.0,R.drawable.coffee_4),
        Product(5,"Macchiato","Bold and milky",19.0,R.drawable.coffee_5),
        Product(6,"Flat White","Velvety smooth",22.0,R.drawable.coffee_6),
        Product(7,"Luckin","Refreshing and rich",9.9,R.drawable.coffee_7),
    )

    val selectedProduct =products.find { it.id == productId }

    if (selectedProduct ==null){
        Text(text = "Product not found",color = Color.Red)
        return
    }

    Scaffold(
        topBar = {DetailsScreenToAppBar(navController)},
        bottomBar = {DetailsScreenBottomBar(selectedProduct)}
    ) { innerPadding->
        LazyColumn(
//            contentPadding = innerPadding // 直接把高度喂给列表！让列表自己去留白
        ){
            item {
                ProductDetailsContent(
                    selectedProduct,
                    innerPadding)
            }
        }

    }

}