package com.mrwang.coffeeapp.data.repository

import com.mrwang.coffeeapp.data.local.AppDatabase
import com.mrwang.coffeeapp.data.local.BannerEntity
import com.mrwang.coffeeapp.data.local.toDomain
import com.mrwang.coffeeapp.data.local.toEntity
import com.mrwang.coffeeapp.data.network.NetworkManager
import com.mrwang.coffeeapp.data.network.SupabaseConfig
import com.mrwang.coffeeapp.domain.model.Product
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CoffeeRepository(
    private val database: AppDatabase
) {
    val productsFlow: Flow<List<Product>> =
        database.productDao().observeProducts().map { entities ->
            entities.map { it.toDomain() }
        }

    val bannerUrlsFlow: Flow<List<String>> =
        database.bannerDao().observeBanners().map { banners ->
            banners.map { it.imageUrl }
        }

    suspend fun refreshProducts(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val remoteProducts = NetworkManager.api.getProducts(
                apiKey = SupabaseConfig.ANON_KEY,
                authorization = SupabaseConfig.authorizationHeader
            )
            database.withTransaction {
                database.productDao().clearProducts()
                database.productDao().upsertProducts(remoteProducts.map { it.toEntity() })
            }
        }
    }

    suspend fun refreshBanners(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val remoteBanners = NetworkManager.api.getBanners()
            database.withTransaction {
                database.bannerDao().clearBanners()
                database.bannerDao().insertBanners(
                    remoteBanners.mapIndexed { index, imageUrl ->
                        BannerEntity(imageUrl = imageUrl, sortOrder = index)
                    }
                )
            }
        }
    }

    suspend fun getCachedProductById(productId: Int): Product? = withContext(Dispatchers.IO) {
        database.productDao().getProductById(productId)?.toDomain()
    }

    suspend fun refreshProductById(productId: Int): Result<Product?> = withContext(Dispatchers.IO) {
        runCatching {
            val product = NetworkManager.api.getProductById(
                apiKey = SupabaseConfig.ANON_KEY,
                authorization = SupabaseConfig.authorizationHeader,
                idFilter = "eq.$productId"
            ).firstOrNull()

            if (product != null) {
                database.productDao().upsertProducts(listOf(product.toEntity()))
            }

            product
        }
    }
}
