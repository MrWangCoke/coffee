package com.mrwang.coffeeapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class  Routes {
    @Serializable
    object WelcomeScreen: Routes()

    @Serializable
    object HomeScreen: Routes()

    @Serializable
    data class DetailScreen(val productId:Int): Routes()

    @Serializable
    object CartScreen: Routes()

    @Serializable
    object FavouritesScreen: Routes()

    @Serializable
    object ProfileScreen: Routes()

    @Serializable
    object SettingsScreen: Routes()

    @Serializable
    object MerchantOrdersScreen: Routes()

    @Serializable
    object LoginScreen: Routes() // 👈 检查这里是否漏写了，或者名字是不是叫 Login

}
