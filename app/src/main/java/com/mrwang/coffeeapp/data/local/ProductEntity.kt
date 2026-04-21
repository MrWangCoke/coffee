package com.mrwang.coffeeapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mrwang.coffeeapp.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "merchant_id") val merchantId: String?,
    val name: String,
    val category: String,
    val description: String?,
    val price: Double,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "is_available") val isAvailable: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: String?
)

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    merchantId = merchantId,
    name = name,
    category = category,
    description = description,
    price = price,
    imageUrl = imageUrl,
    isAvailable = isAvailable,
    createdAt = createdAt
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    merchantId = merchantId,
    name = name,
    category = category,
    description = description,
    price = price,
    imageUrl = imageUrl,
    isAvailable = isAvailable,
    createdAt = createdAt
)
