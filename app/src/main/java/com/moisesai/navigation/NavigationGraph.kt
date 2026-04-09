package com.moisesai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.moisesai.home.presentation.navigation.homeNavigation
import com.moisesai.splash.presentation.navigation.splashNavGraph
import com.moisesai.navigation.routes.Routes
import com.moisesai.player.presentation.navigation.playerNavigation
import com.moisesai.home.presentation.navigation.albumNavigation
import com.moisesai.home.presentation.navigation.homeNavigation

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        splashNavGraph(navController = navController)
        homeNavigation(navController = navController)
        albumNavigation(navController = navController)
        playerNavigation(navController = navController)
    }
}


