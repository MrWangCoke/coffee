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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mrwang.coffeeapp.presentation.navigation.Routes
import com.mrwang.coffeeapp.presentation.settings.AppLanguage
import com.mrwang.coffeeapp.presentation.settings.AppSettingsViewModel
import com.mrwang.coffeeapp.presentation.theme.LightBrown
import com.mrwang.coffeeapp.presentation.theme.LightGray
import com.mrwang.coffeeapp.presentation.ui_components.MyButtonNavBar

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    appSettingsViewModel: AppSettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()
    val email by userViewModel.email.collectAsState()
    val language by appSettingsViewModel.language.collectAsState()
    val isChinese = language == AppLanguage.Chinese

    Scaffold(
        bottomBar = { MyButtonNavBar(navController = navController, "Profile") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
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
                }
            }
        }
    }
}

@Composable
fun SettingItem(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).clickable { },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.Black, fontSize = 16.sp)
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = LightBrown)
    }
}
