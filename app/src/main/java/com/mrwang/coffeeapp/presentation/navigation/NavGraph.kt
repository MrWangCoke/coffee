package com.mrwang.coffeeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mrwang.coffeeapp.presentation.settings.AppSettingsViewModel
import com.mrwang.coffeeapp.presentation.screens.cartscreen.CartScreen
import com.mrwang.coffeeapp.presentation.screens.detailsscreen.DetailsScreen
import com.mrwang.coffeeapp.presentation.screens.favouritesscreen.FavouritesScreen
import com.mrwang.coffeeapp.presentation.screens.homescreen.HomeScreen
import com.mrwang.coffeeapp.presentation.screens.loginscreen.LoginScreen
import com.mrwang.coffeeapp.presentation.screens.profilescreen.ProfileScreen
import com.mrwang.coffeeapp.presentation.screens.profilescreen.SettingsScreen
import com.mrwang.coffeeapp.presentation.screens.profilescreen.UserViewModel
import com.mrwang.coffeeapp.presentation.screens.shop.ShopViewModel
import com.mrwang.coffeeapp.presentation.screens.welcomescreen.WelcomeScreen


@Preview
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()
    val shopViewModel: ShopViewModel = viewModel()
    val appSettingsViewModel: AppSettingsViewModel = viewModel()

    NavHost(navController=navController, startDestination = Routes.WelcomeScreen){
        composable<Routes.WelcomeScreen> {
            WelcomeScreen(navController)
        }

        composable<Routes.HomeScreen> {
            HomeScreen(
                navController = navController,
                shopViewModel = shopViewModel,
                userViewModel = userViewModel,
                appSettingsViewModel = appSettingsViewModel
            )
        }

        composable<Routes.DetailScreen> {backStackEntry->
            val args = backStackEntry.toRoute<Routes.DetailScreen>()
            DetailsScreen(
                productId = args.productId,
                navController = navController,
                shopViewModel = shopViewModel,
                userViewModel = userViewModel,
                appSettingsViewModel = appSettingsViewModel
            )

        }

        composable<Routes.CartScreen>{
            CartScreen(
                navController = navController,
                shopViewModel = shopViewModel,
                userViewModel = userViewModel,
                appSettingsViewModel = appSettingsViewModel
            )
        }

        composable<Routes.FavouritesScreen> {
            FavouritesScreen(
                navController = navController,
                shopViewModel = shopViewModel,
                appSettingsViewModel = appSettingsViewModel
            )
        }

        composable<Routes.ProfileScreen> {
            ProfileScreen(
                navController = navController,
                userViewModel = userViewModel,
                appSettingsViewModel = appSettingsViewModel
            )
        }

        composable<Routes.SettingsScreen> {
            SettingsScreen(
                navController = navController,
                userViewModel = userViewModel,
                appSettingsViewModel = appSettingsViewModel
            )
        }

        composable<Routes.LoginScreen> {
            LoginScreen(
                navController = navController,
                userViewModel = userViewModel,
                appSettingsViewModel = appSettingsViewModel
            )
        }
    }
}
