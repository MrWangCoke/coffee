package com.mrwang.coffeeapp.presentation.screens.loginscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mrwang.coffeeapp.presentation.navigation.Routes
import com.mrwang.coffeeapp.presentation.settings.AppLanguage
import com.mrwang.coffeeapp.presentation.settings.AppSettingsViewModel
import com.mrwang.coffeeapp.presentation.screens.profilescreen.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    appSettingsViewModel: AppSettingsViewModel,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val language by appSettingsViewModel.language.collectAsState()
    val isChinese = language == AppLanguage.Chinese
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) } // 切换登录/注册模式

    // 监听登录成功状态，成功后同步个人中心状态
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            val response = uiState.authResponse
            val accessToken = response?.accessToken
            if (!accessToken.isNullOrBlank()) {
                userViewModel.login(
                    userId = response.user?.id,
                    email = response.user?.email ?: email.trim(),
                    accessToken = accessToken
                )
            }
            navController.navigate(Routes.ProfileScreen) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0F14))
    ) {
        IconButton(
            onClick = {
                if (!navController.popBackStack()) {
                    navController.navigate(Routes.ProfileScreen) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 32.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isChinese) {
                    if (isLoginMode) "欢迎回来" else "创建账号"
                } else {
                    if (isLoginMode) "Welcome Back" else "Create Account"
                },
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isChinese) {
                    if (isLoginMode) "登录后继续使用" else "创建你的咖啡账号"
                } else {
                    if (isLoginMode) "Sign in to continue" else "Create your coffee account"
                },
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
            )

            // 邮箱输入框
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(if (isChinese) "邮箱" else "Email", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD17842),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 密码输入框
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(if (isChinese) "密码" else "Password", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD17842),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 报错信息展示
            if (uiState.errorMessage != null) {
                Text(text = uiState.errorMessage!!, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (uiState.successMessage != null && !uiState.isLoginSuccess) {
                Text(text = uiState.successMessage!!, color = Color(0xFFD17842), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 登录/注册按钮
            Button(
                onClick = { viewModel.loginOrSignUp(email, password, isLoginMode) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD17842)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        if (isChinese) {
                            if (isLoginMode) "登录" else "注册"
                        } else {
                            if (isLoginMode) "Login" else "Sign Up"
                        },
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 切换模式按钮
            TextButton(
                onClick = {
                    isLoginMode = !isLoginMode
                    viewModel.clearMessages()
                }
            ) {
                Text(
                    text = if (isChinese) {
                        if (isLoginMode) "还没有账号？去注册" else "已有账号？去登录"
                    } else {
                        if (isLoginMode) "Don't have an account? Sign up" else "Already have an account? Login"
                    },
                    color = Color.Gray
                )
            }
        }
    }
}
