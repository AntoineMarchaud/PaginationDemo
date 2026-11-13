package com.amarchaud.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Neutral palette with a single indigo accent.
internal object AppColors {
    val Indigo50 = Color(0xFFEEF2FF)
    val Indigo100 = Color(0xFFE0E7FF)
    val Indigo300 = Color(0xFFA5B4FC)
    val Indigo400 = Color(0xFF818CF8)
    val Indigo600 = Color(0xFF4F46E5)
    val Indigo900 = Color(0xFF312E81)

    val Ink950 = Color(0xFF0B0D12)
    val Ink900 = Color(0xFF111318)
    val Ink850 = Color(0xFF14171E)
    val Ink800 = Color(0xFF1C2029)
    val Ink700 = Color(0xFF262B36)
    val Ink500 = Color(0xFF5B6170)
    val Ink300 = Color(0xFFA0A7B5)
    val Ink100 = Color(0xFFE2E5EB)
    val Ink50 = Color(0xFFF6F7F9)
    val Ink25 = Color(0xFFEEF0F4)

    val Red600 = Color(0xFFDC2626)
    val Red400 = Color(0xFFF87171)
    val Red50 = Color(0xFFFEF2F2)
    val Red950 = Color(0xFF3B0D0D)
}

internal val LightColors = lightColorScheme(
    primary = AppColors.Indigo600,
    onPrimary = Color.White,
    primaryContainer = AppColors.Indigo100,
    onPrimaryContainer = AppColors.Indigo900,
    secondary = AppColors.Ink500,
    onSecondary = Color.White,
    secondaryContainer = AppColors.Indigo50,
    onSecondaryContainer = AppColors.Indigo600,
    background = AppColors.Ink50,
    onBackground = AppColors.Ink900,
    surface = Color.White,
    onSurface = AppColors.Ink900,
    surfaceVariant = AppColors.Ink25,
    onSurfaceVariant = AppColors.Ink500,
    surfaceContainer = Color.White,
    surfaceContainerHigh = AppColors.Ink25,
    outline = AppColors.Ink100,
    outlineVariant = AppColors.Ink100,
    error = AppColors.Red600,
    onError = Color.White,
    errorContainer = AppColors.Red50,
    onErrorContainer = AppColors.Red600,
)

internal val DarkColors = darkColorScheme(
    primary = AppColors.Indigo400,
    onPrimary = AppColors.Ink950,
    primaryContainer = AppColors.Indigo900,
    onPrimaryContainer = AppColors.Indigo50,
    secondary = AppColors.Ink300,
    onSecondary = AppColors.Ink950,
    secondaryContainer = AppColors.Ink800,
    onSecondaryContainer = AppColors.Indigo300,
    background = AppColors.Ink950,
    onBackground = AppColors.Ink50,
    surface = AppColors.Ink850,
    onSurface = AppColors.Ink50,
    surfaceVariant = AppColors.Ink800,
    onSurfaceVariant = AppColors.Ink300,
    surfaceContainer = AppColors.Ink850,
    surfaceContainerHigh = AppColors.Ink800,
    outline = AppColors.Ink700,
    outlineVariant = AppColors.Ink700,
    error = AppColors.Red400,
    onError = AppColors.Ink950,
    errorContainer = AppColors.Red950,
    onErrorContainer = AppColors.Red400,
)
