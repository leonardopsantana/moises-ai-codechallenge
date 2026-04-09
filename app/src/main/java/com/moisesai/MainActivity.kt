package com.moisesai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.moisesai.core.ui.theme.MoisesAiTheme
import com.moisesai.navigation.NavigationGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            MoisesAIApp()
        }
    }
}

@Composable
fun MoisesAIApp() {
    MoisesAiTheme {
        val navController = rememberNavController()
        NavigationGraph(navController = navController)
    }
}