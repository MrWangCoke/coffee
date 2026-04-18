package com.mrwang.coffeeapp.data.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object NetworkManager {

    // 1. 创建“海关检查员”（日志拦截器）
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Level.BODY 意味着打印最全的信息：请求头、请求网址、返回的状态码以及完整的 JSON 数据！
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 2. 创建“物流货车”（OkHttpClient）并将检查员安插进去
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // 3. 配置 JSON 解析器 (忽略 JSON 里有但 Kotlin 类里没有的字段，防止崩溃)
    private val networkJson = Json { ignoreUnknownKeys = true }

    // 4. 创建“采购经理”（Retrofit 实例）
    private val retrofit = Retrofit.Builder()
        // 基础网址（前半部分），注意必须以 / 结尾
        .baseUrl("https://run.mocky.io/")
        .client(okHttpClient)
        // 告诉 Retrofit 用 Kotlin Serialization 来解析 JSON
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()

    // 5. 暴露出我们刚才写的 API 接口供外部使用
    val api: CoffeeApi = retrofit.create(CoffeeApi::class.java)
}