package com.mrwang.coffeeapp.presentation.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mrwang.coffeeapp.R
import com.mrwang.coffeeapp.domain.model.Product
import com.mrwang.coffeeapp.presentation.ui_components.MyButtonNavBar

//首页
@Composable
fun HomeScreen(navController: NavController){
    val location ="Janatha Rd,Palarivattom"
    Scaffold (
        bottomBar = { MyButtonNavBar(navController,"Home") }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f/3f)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF303030),
                            Color(0xFF1F1F1F),
                            Color(0xFF121212)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ){
            val products =listOf(
                Product(1,"Espresso","Strong and rich",20.0,R.drawable.coffee_1),
                Product(2,"Latte","Smooth and creamy",25.0,R.drawable.coffee_2),
                Product(3,"Cappuccino","With Chocolate",22.0,R.drawable.coffee_3),
                Product(4,"Mocha","With cocos flavor",29.0,R.drawable.coffee_4),
                Product(5,"Macchiato","Bold and milky",19.0,R.drawable.coffee_5),
                Product(6,"Flat White","Velvety smooth",22.0,R.drawable.coffee_6),
                Product(7,"Luckin","Refreshing and rich",9.9,R.drawable.coffee_7),
            )

            ProductGrid(products = products, navController= navController ){
                Text(
                    text = "Location",
                    color = Color.Gray,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(){
                    Text(
                        location,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                    Icon(imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Change Location",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))

                MySearchBar()

                Spacer(modifier = Modifier.height(40.dp))

                Image(
                    painter = painterResource(R.drawable.banner_1),
                    contentDescription = "Home Banner"
                )

                Spacer(modifier = Modifier.height(16.dp))

                HomeScreenCategories()
            }
        }
    }
}