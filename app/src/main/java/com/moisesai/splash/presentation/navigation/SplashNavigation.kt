package com.moisesai.splash.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moisesai.navigation.routes.Routes
import com.moisesai.splash.presentation.screens.SplashScreen

fun NavGraphBuilder.splashNavGraph(navController: NavHostController) {
    composable(Routes.SPLASH) {
        SplashScreen(
            onAnimationFinished = {
                navController.navigate(Routes.HOME)
            }
        )
    }
}