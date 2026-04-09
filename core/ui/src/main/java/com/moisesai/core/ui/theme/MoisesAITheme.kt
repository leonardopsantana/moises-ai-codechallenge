package com.moisesai.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.moisesai.core.ui.resources.AppTypography
import com.moisesai.core.ui.resources.Colors

@Composable
fun MoisesAiTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = Colors.DarkColors,
        typography = AppTypography,
        content = content,
    )
}

