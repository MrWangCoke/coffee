package com.mrwang.coffeeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val imageUrl: String,
    val sortOrder: Int
)
