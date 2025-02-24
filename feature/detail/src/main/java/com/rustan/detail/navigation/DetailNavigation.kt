package com.rustan.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.rustan.detail.DetailsScreen
import com.rustan.model.ImageItem


fun NavController.navigateToDetail(item: ImageItem) {
    this.navigate(item)
}

fun NavGraphBuilder.detailScreen(onNavigateToHome: () -> Unit) {
    composable<ImageItem> { backStackEntry ->
        val item: ImageItem = backStackEntry.toRoute()
        DetailsScreen(item = item, onNavigateToHome = onNavigateToHome)
    }
}