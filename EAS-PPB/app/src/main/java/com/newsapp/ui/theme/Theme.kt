package com.newsapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightGreenColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,

    primaryContainer = GreenPrimary,
    onPrimaryContainer = Color.White,

    background = LightGreenBackground,
    onBackground = DarkText,

    surface = WhiteSurface,
    onSurface = DarkText,

    surfaceVariant = SecondaryText,
    onSurfaceVariant = Color.White
)

@Composable
fun NewsAppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightGreenColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes(),
        content = content
    )
}