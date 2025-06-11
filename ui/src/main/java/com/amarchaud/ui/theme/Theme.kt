package com.amarchaud.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = Purple500,
    secondary = Teal200,
    onPrimary = Color.White,
    onSecondary = Color.White,
    error = Color.Red,
    onError = Color(0xFFF12000),
)

@Composable
fun PaginationDemoTheme(content: @Composable () -> Unit) {

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = LightColorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}