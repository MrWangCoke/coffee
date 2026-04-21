package com.mrwang.coffeeapp.presentation.screens.loginscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrwang.coffeeapp.data.network.NetworkManager
import com.mrwang.coffeeapp.data.network.SupabaseConfig
import com.mrwang.coffeeapp.domain.model.AuthRequest
import com.mrwang.coffeeapp.domain.model.AuthResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException

data class LoginUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false, // 用来通知 UI 跳转
    val authResponse: AuthResponse? = null
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val errorJson = Json { ignoreUnknownKeys = true }

    fun loginOrSignUp(email: String, password: String, isLogin: Boolean) {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email and password cannot be empty") }
            return
        }

        if (!trimmedEmail.contains("@")) {
            _uiState.update { it.copy(errorMessage = "Please enter a valid email address") }
            return
        }

        if (!isLogin && password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Sign up failed: password must be at least 6 characters") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    successMessage = null,
                    errorMessage = null,
                    isLoginSuccess = false,
                    authResponse = null
                )
            }
            try {
                val request = AuthRequest(trimmedEmail, password)
                // 根据模式选择调用登录还是注册接口
                val response = if (isLogin) {
                    NetworkManager.api.login(SupabaseConfig.ANON_KEY, request)
                } else {
                    NetworkManager.api.signUp(SupabaseConfig.ANON_KEY, request)
                }

                if (response.accessToken.isNullOrBlank()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = if (isLogin) null else "Sign up successful. Please verify your email before logging in.",
                            errorMessage = if (isLogin) "Login failed. Please check your email and password." else null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                            successMessage = if (isLogin) "Welcome back!" else "Sign up successful!",
                            authResponse = response
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = friendlyAuthError(
                            isLogin = isLogin,
                            serverMessage = (e as? HttpException)?.parseSupabaseError()
                        )
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                isLoginSuccess = false,
                authResponse = null
            )
        }
    }

    private fun HttpException.parseSupabaseError(): String? {
        val errorBody = response()?.errorBody()?.string()?.takeIf { it.isNotBlank() } ?: return null

        return runCatching {
            val jsonObject = errorJson.parseToJsonElement(errorBody).jsonObject
            listOf("msg", "message", "error_description", "error").firstNotNullOfOrNull { key ->
                jsonObject[key]?.jsonPrimitive?.contentOrNull?.takeIf { it.isNotBlank() }
            }
        }.getOrNull() ?: errorBody.take(120)
    }

    private fun friendlyAuthError(isLogin: Boolean, serverMessage: String?): String {
        val message = serverMessage.orEmpty()

        return when {
            message.contains("already registered", ignoreCase = true) ->
                "Sign up failed: this email is already registered. You can log in directly."

            message.contains("rate limit", ignoreCase = true) ->
                "Too many sign-up attempts. Supabase is rate limiting verification emails, please try again later."

            message.contains("password", ignoreCase = true) &&
                message.contains("at least", ignoreCase = true) ->
                "Sign up failed: password must be at least 6 characters"

            message.contains("weak", ignoreCase = true) ->
                "Sign up failed: password is too weak. Please use a stronger password."

            message.contains("invalid login credentials", ignoreCase = true) ->
                "Login failed: email or password is incorrect"

            message.contains("email", ignoreCase = true) &&
                message.contains("invalid", ignoreCase = true) ->
                "Invalid email format"

            !serverMessage.isNullOrBlank() ->
                if (isLogin) "Login failed: $serverMessage" else "Sign up failed: $serverMessage"

            isLogin ->
                "Login failed. Please check your email and password."

            else ->
                "Sign up failed. Please wait a moment and try again, or check whether the email already exists and the password is at least 6 characters."
        }
    }
}
