@file:OptIn(KoinExperimentalAPI::class)

package com.moisesai.player.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moisesai.navigation.routes.Routes
import com.moisesai.player.di.PlayerModule
import com.moisesai.player.presentation.ui.PlayerScreen
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

fun NavGraphBuilder.playerNavigation(navController: NavHostController) {
    composable(Routes.PLAYER) {
        BackHandler(true) {}
        rememberKoinModules {
            listOf(PlayerModule().playerModule)
        }
        PlayerScreen(navController = navController)

    }
}
