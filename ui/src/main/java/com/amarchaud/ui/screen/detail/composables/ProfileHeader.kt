package com.amarchaud.ui.screen.detail.composables

import com.amarchaud.ui.R
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amarchaud.ui.composables.BlurredBackdrop
import com.amarchaud.ui.composables.UserAvatar
import com.amarchaud.ui.composables.sharedUserAvatar
import com.amarchaud.ui.screen.detail.models.UserDetailUiModel
import com.amarchaud.ui.screen.mappers.joinNonBlank
import com.amarchaud.ui.theme.spacing
import androidx.compose.ui.res.stringResource

private val BackdropHeight = 240.dp
private val AvatarSize = 128.dp

/** Blurred picture backdrop with parallax, overlapped by the avatar, the name and the location. */
@Composable
internal fun ProfileHeader(
    user: UserDetailUiModel,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.colorScheme.background
    val info = user.mainInfo

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(BackdropHeight)
                .clipToBounds()
                .graphicsLayer { translationY = scrollState.value * 0.5f },
        ) {
            BlurredBackdrop(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        scaleX = 1.3f
                        scaleY = 1.3f
                    }
                    .blur(48.dp),
                imageUrl = user.mainImageUrl,
            )
            // readable status bar on top, smooth fade into the page at the bottom
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0f to background.copy(alpha = 0.55f),
                            0.45f to background.copy(alpha = 0.1f),
                            1f to background,
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BackdropHeight - AvatarSize / 2)
                .padding(horizontal = MaterialTheme.spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UserAvatar(
                modifier = Modifier
                    .sharedUserAvatar(info.localId)
                    .size(AvatarSize),
                imageUrl = user.mainImageUrl,
                contentDescription = stringResource(R.string.avatar_description, info.fullName),
                ringWidth = 4.dp,
                ringColor = background,
            )
            Spacer(Modifier.height(MaterialTheme.spacing.l))
            Text(
                text = info.fullName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(MaterialTheme.spacing.xs))
            Text(
                text = joinNonBlank(
                    " · ",
                    info.title,
                    info.age?.let { stringResource(R.string.years_old, it) },
                    info.flag,
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            if (info.location.isNotEmpty()) {
                Text(
                    text = info.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
