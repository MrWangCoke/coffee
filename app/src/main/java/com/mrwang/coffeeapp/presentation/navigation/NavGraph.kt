package com.mrwang.coffeeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mrwang.coffeeapp.presentation.screens.cartscreen.CartScreen
import com.mrwang.coffeeapp.presentation.screens.detailsscreen.DetailsScreen
import com.mrwang.coffeeapp.presentation.screens.favouritesscreen.FavouritesScreen
import com.mrwang.coffeeapp.presentation.screens.homescreen.HomeScreen
import com.mrwang.coffeeapp.presentation.screens.profilescreen.ProfileScreen
import com.mrwang.coffeeapp.presentation.screens.welcomescreen.WelcomeScreen


@Preview
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController=navController, startDestination = Routes.WelcomeScreen){
        composable<Routes.WelcomeScreen> {
            WelcomeScreen(navController)
        }

        composable<Routes.HomeScreen> {
            HomeScreen(navController)
        }

        composable<Routes.DetailScreen> {backStackEntry->
            val args = backStackEntry.toRoute<Routes.DetailScreen>()
            DetailsScreen(productId = args.productId,navController)

        }

        composable<Routes.CartScreen>{
            CartScreen(navController)
        }

        composable<Routes.FavouritesScreen> {
            FavouritesScreen(navController)
        }

        composable<Routes.ProfileScreen> {
            ProfileScreen(navController)
        }
    }
}