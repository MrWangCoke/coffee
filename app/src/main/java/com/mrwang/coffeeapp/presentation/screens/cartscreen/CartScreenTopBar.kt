package com.mrwang.coffeeapp.presentation.screens.cartscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenToAppBar() {

    TopAppBar(
        title = {
            Text("已选商品",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
//        navigationIcon = {
//            Icon(
//                painter = painterResource(R.drawable.regular_outline_arrow_left),
//                contentDescription = "Back Button",
//                modifier = Modifier
//                    .padding(start =12.dp)
//                    .clickable(onClick = {navController.navigate(Routes.HomeScreen)})
//            )
//        }
    )
}