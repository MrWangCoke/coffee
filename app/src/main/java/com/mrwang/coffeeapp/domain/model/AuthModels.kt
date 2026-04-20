package com.mrwang.coffeeapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 发送给服务器的账号密码
@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

// 服务器返回的登录结果（包含 Token 和用户信息）
@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String? = null,
    val user: SupabaseUser? = null
)

@Serializable
data class SupabaseUser(
    val id: String,
    val email: String? = null
)
