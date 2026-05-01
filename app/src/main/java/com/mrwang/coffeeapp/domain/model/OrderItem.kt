package com.mrwang.coffeeapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val id: Long,
    @SerialName("user_id") val userId: String,
    @SerialName("product_id") val productId: Long,
    @SerialName("merchant_id") val merchantId: String? = null,
    val quantity: Int = 1,
    val status: String = "cart",
    @SerialName("created_at") val createdAt: String? = null,
    val products: Product? = null
)

@Serializable
data class CreateOrderItemRequest(
    @SerialName("user_id") val userId: String,
    @SerialName("product_id") val productId: Long,
    @SerialName("merchant_id") val merchantId: String? = null,
    val quantity: Int = 1,
    val status: String = "cart"
)

@Serializable
data class UpdateOrderItemQuantityRequest(
    val quantity: Int
)

@Serializable
data class UpdateOrderItemStatusRequest(
    val status: String
)
