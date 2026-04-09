@file:OptIn(KoinExperimentalAPI::class)

package com.moisesai.home.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moisesai.home.di.SongModule
import com.moisesai.navigation.routes.Routes
import com.moisesai.home.presentation.ui.album.AlbumScreen
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

fun NavGraphBuilder.albumNavigation(navController: NavHostController) {
    composable(Routes.ALBUM) {
        BackHandler(true) {
            navController.navigateUp()
        }
        rememberKoinModules {
            listOf(SongModule().songsModule)
        }
        AlbumScreen(navController = navController)
    }
}
