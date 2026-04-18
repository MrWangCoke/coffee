package com.mrwang.coffeeapp.data.network


import retrofit2.http.GET

interface CoffeeApi {
    // 这里我给你准备了一个真实的测试接口，它会返回 3 张咖啡 Banner 的高清网络链接
    @GET("v3/d7f8d689-53e3-4b68-8094-1b32f91fa8cc")
    suspend fun getBanners(): List<String>
}