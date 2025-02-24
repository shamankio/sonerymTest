package com.rustan.sonerimtest

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.rustan.detail.navigation.detailScreen
import com.rustan.detail.navigation.navigateToDetail
import com.rustan.home.navigation.homeRoute
import com.rustan.home.navigation.homeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = homeRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(
            onNavigateToDetail = { navController.navigateToDetail(it) }
        )
        detailScreen(onNavigateToHome = { navController.navigateUp() })
    }
}