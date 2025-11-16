package com.demo.tictactoe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

private val LightColors = lightColorScheme(
    primary = NeonPurple,
    secondary = NeonCyan,
    surface = SurfaceDim,
    background = NeonDeep,
    onPrimary = TextPrimary,
    onBackground = TextPrimary
)

private val DarkColors = LightColors // we will use the same neon palette

@Composable
fun TicTacToeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}
