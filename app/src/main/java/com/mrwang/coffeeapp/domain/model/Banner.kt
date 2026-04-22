package com.mrwang.coffeeapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Banner(
    val id: Long,
    @SerialName("merchant_id") val merchantId: String? = null,
    @SerialName("image_url") val imageUrl: String,
    val title: String? = null,
    @SerialName("target_link") val targetLink: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("sort_order") val sortOrder: Int = 0,
    @SerialName("created_at") val createdAt: String? = null
)
