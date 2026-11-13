package com.amarchaud.ui.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

/** Provided by the app around its NavHost; null in previews. */
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

/** Provided by each navigation destination; null in previews. */
val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/** The avatar morphs from the list card to the detail header. */
@Composable
internal fun Modifier.sharedUserAvatar(localId: Long): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    val visibilityScope = LocalNavAnimatedVisibilityScope.current ?: return this
    return with(sharedScope) {
        this@sharedUserAvatar.sharedElement(
            sharedContentState = rememberSharedContentState(key = "user-avatar-$localId"),
            animatedVisibilityScope = visibilityScope,
        )
    }
}
