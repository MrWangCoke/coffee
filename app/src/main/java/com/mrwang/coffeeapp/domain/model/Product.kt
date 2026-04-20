package com.mrwang.coffeeapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    // @SerialName 作用是：把数据库里的下划线命名(image_url) 映射成 Kotlin 的驼峰命名(imageUrl)
    @SerialName("merchant_id") val merchantId: String? = null,
    val name: String,
    val category: String, // 👈 新增分类字段
    val description: String? = null,
    val price: Double,
    @SerialName("image_url") val imageUrl: String? = null, // 👈 替换掉原来的 imageRes
    @SerialName("is_available") val isAvailable: Boolean = true,
    @SerialName("created_at") val createdAt: String? = null
)