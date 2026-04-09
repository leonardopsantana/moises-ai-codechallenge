@file:OptIn(KoinExperimentalAPI::class)

package com.moisesai.home.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moisesai.home.di.SongModule
import com.moisesai.home.presentation.ui.songs.CategoriesScreen
import com.moisesai.navigation.routes.Routes
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

fun NavGraphBuilder.homeNavigation(navController: NavHostController) {
    composable(Routes.HOME) {
        BackHandler(true) {}
        rememberKoinModules {
            listOf(SongModule().songsModule)
        }
        CategoriesScreen(navController = navController)

    }
}
