package com.mrwang.coffeeapp.data.network


import com.mrwang.coffeeapp.domain.model.AuthRequest
import com.mrwang.coffeeapp.domain.model.AuthResponse
import com.mrwang.coffeeapp.domain.model.Banner
import com.mrwang.coffeeapp.domain.model.CreateOrderItemRequest
import com.mrwang.coffeeapp.domain.model.OrderItem
import com.mrwang.coffeeapp.domain.model.Product
import com.mrwang.coffeeapp.domain.model.UpdateOrderItemQuantityRequest
import com.mrwang.coffeeapp.domain.model.UpdateOrderItemStatusRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface CoffeeApi {
    @GET("rest/v1/banners?select=*&is_active=eq.true&order=sort_order.desc,created_at.desc")
    suspend fun getBanners(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String
    ): List<Banner>

    // Supabase 自动生成的查询所有产品的接口
    @GET("rest/v1/products?select=*")
    suspend fun getProducts(
        // 每次请求必须带上你的专属钥匙，Supabase 才知道是你
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String
    ): List<Product>

    @GET("rest/v1/products?select=*")
    suspend fun getProductById(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("id") idFilter: String
    ): List<Product>

    @POST("auth/v1/signup")
    suspend fun signUp(
        @Header("apikey") apiKey: String,
        @Body request: AuthRequest
    ): AuthResponse

    // 2. 登录接口
    @POST("auth/v1/token?grant_type=password")
    suspend fun login(
        @Header("apikey") apiKey: String,
        @Body request: AuthRequest
    ): AuthResponse

    @GET("rest/v1/order_items?select=*,products(*)&status=eq.cart")
    suspend fun getCartItems(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("user_id") userIdFilter: String
    ): List<OrderItem>

    @GET("rest/v1/order_items?select=*,products(*)&status=eq.completed&order=created_at.desc")
    suspend fun getCompletedOrderItems(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("user_id") userIdFilter: String
    ): List<OrderItem>

    @GET("rest/v1/order_items?select=*,products(*)&status=eq.pending&order=created_at.desc")
    suspend fun getPendingOrderItems(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("user_id") userIdFilter: String
    ): List<OrderItem>

    @GET("rest/v1/order_items?select=*,products(*)&status=eq.completed&order=created_at.desc")
    suspend fun getMerchantCompletedOrderItems(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("merchant_id") merchantIdFilter: String
    ): List<OrderItem>

    @POST("rest/v1/order_items")
    suspend fun addCartItem(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Header("Prefer") prefer: String = "return=minimal",
        @Body request: CreateOrderItemRequest
    )

    @PATCH("rest/v1/order_items")
    suspend fun updateCartItemQuantity(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Header("Prefer") prefer: String = "return=minimal",
        @Query("id") idFilter: String,
        @Body request: UpdateOrderItemQuantityRequest
    )

    @PATCH("rest/v1/order_items")
    suspend fun updateOrderItemStatus(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Header("Prefer") prefer: String = "return=minimal",
        @Query("id") idFilter: String,
        @Body request: UpdateOrderItemStatusRequest
    )

    @DELETE("rest/v1/order_items")
    suspend fun deleteCartItem(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("id") idFilter: String
    )
}
