package com.mrwang.coffeeapp.presentation.screens.welcomescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mrwang.coffeeapp.R
import com.mrwang.coffeeapp.presentation.navigation.Routes
import com.mrwang.coffeeapp.presentation.theme.LightBrown

//欢迎界面

@Composable
fun WelcomeScreen(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ){
        Image(
            painter = painterResource(R.drawable.image_splash),
            contentDescription="Welcome Image",
            modifier = Modifier
                .fillMaxWidth()
        )
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 60.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ){
            Text(
                text = "Fall in love with Coffee in Blissful Delight",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Welcome to our cozy coffee corner ,where every cup is a delight for you",
                color = Color.LightGray,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button (
                onClick = { navController.navigate(Routes.HomeScreen) },
                modifier= Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors= ButtonDefaults.buttonColors(
                    containerColor = LightBrown,
                )
            ){
                Text(text="Get Started",
                    fontSize = 18.sp
                )
            }
        }
    }
}