package com.amarchaud.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.amarchaud.ui.R

private val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
)

private fun style(size: Int, line: Int, weight: FontWeight, letterSpacing: Double = 0.0) = TextStyle(
    fontFamily = Poppins,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = line.sp,
    letterSpacing = letterSpacing.sp,
)

internal val AppTypography = Typography(
    displaySmall = style(36, 44, FontWeight.SemiBold, -0.5),
    headlineLarge = style(32, 40, FontWeight.SemiBold, -0.5),
    headlineMedium = style(28, 36, FontWeight.SemiBold, -0.25),
    headlineSmall = style(24, 32, FontWeight.SemiBold),
    titleLarge = style(20, 28, FontWeight.SemiBold),
    titleMedium = style(16, 24, FontWeight.SemiBold),
    titleSmall = style(14, 20, FontWeight.Medium),
    bodyLarge = style(16, 24, FontWeight.Normal),
    bodyMedium = style(14, 20, FontWeight.Normal),
    bodySmall = style(12, 16, FontWeight.Normal),
    labelLarge = style(14, 20, FontWeight.Medium),
    labelMedium = style(12, 16, FontWeight.Medium),
    labelSmall = style(11, 16, FontWeight.Medium, 0.2),
)
