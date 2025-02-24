package com.rustan.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rustan.home.HomeScreen
import com.rustan.model.ImageItem

const val homeRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(onNavigateToDetail: (ImageItem) -> Unit) {
    composable(route = homeRoute) {
        HomeScreen(
            onNavigateToDetail = onNavigateToDetail
        )
    }
}