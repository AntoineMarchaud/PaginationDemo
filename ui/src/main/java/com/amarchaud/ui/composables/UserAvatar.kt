package com.amarchaud.ui.composables

import com.amarchaud.ui.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import androidx.compose.ui.res.painterResource

/**
 * Round user picture with a neutral placeholder.
 * The same memory cache key is used everywhere, so the detail screen
 * reuses the bitmap already decoded by the list (no flash during the transition).
 */
@Composable
internal fun UserAvatar(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    ringWidth: Dp = 0.dp,
    ringColor: Color = MaterialTheme.colorScheme.surface,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(if (ringWidth > 0.dp) Modifier.border(ringWidth, ringColor, CircleShape) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.padding(16.dp),
            painter = painterResource(R.drawable.ic_person),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        )
        if (!LocalInspectionMode.current && imageUrl.isNotEmpty()) {
            AsyncImage(
                modifier = Modifier
                    .matchParentSize()
                    .padding(ringWidth)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageUrl)
                    .memoryCacheKey(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
            )
        }
    }
}

/** Same picture, used as a soft blurred backdrop. */
@Composable
internal fun BlurredBackdrop(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        if (!LocalInspectionMode.current && imageUrl.isNotEmpty()) {
            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageUrl)
                    .memoryCacheKey(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}
