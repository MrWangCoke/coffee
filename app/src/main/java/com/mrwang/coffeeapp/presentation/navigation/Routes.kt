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
}