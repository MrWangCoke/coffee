package com.mrwang.coffeeapp.presentation.screens.favouritesscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mrwang.coffeeapp.R
import com.mrwang.coffeeapp.domain.model.Product
import com.mrwang.coffeeapp.presentation.ui_components.MyButtonNavBar

@Composable
fun FavouritesScreen(navController: NavController) {
    var favouriteItems by remember{
        mutableStateOf(
            listOf(
                Product(1,"Espresso","Strong and rich",20.0,R.drawable.coffee_1),
                Product(2,"Latte","Smooth and creamy",25.0,R.drawable.coffee_2),
                Product(3,"Cappuccino","With Chocolate",22.0,R.drawable.coffee_3)
            )
        )
    }
    Scaffold(
        topBar = {FavouritesScreenTopBar()},
        bottomBar = { MyButtonNavBar(navController = navController,"Favourite" ) }
    ) { innerPadding->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            item {
                favouriteItems.forEach {product ->
                    FavouriteItemCard(
                        product,
                        onRemove={favouriteItems =favouriteItems-product}
                    )
                }
            }

        }
    }


}