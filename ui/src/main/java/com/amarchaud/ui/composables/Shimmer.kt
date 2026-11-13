package com.amarchaud.ui.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape

/** Skeleton placeholder: a soft light sweep using the theme's neutral colors. */
@Composable
internal fun Modifier.shimmer(shape: Shape): Modifier {
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.surface
    val progress by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer progress",
    )
    val width = 600f
    return background(
        brush = Brush.linearGradient(
            colors = listOf(base, highlight.copy(alpha = 0.6f), base),
            start = Offset(progress * width, 0f),
            end = Offset(progress * width + width, width),
        ),
        shape = shape,
    )
}
