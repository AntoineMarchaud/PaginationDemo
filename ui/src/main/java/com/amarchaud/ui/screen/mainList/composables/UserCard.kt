package com.amarchaud.ui.screen.mainList.composables

import com.amarchaud.ui.R
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amarchaud.ui.composables.UserAvatar
import com.amarchaud.ui.composables.sharedUserAvatar
import com.amarchaud.ui.composables.shimmer
import com.amarchaud.ui.screen.mainList.models.UserGenericUiModel
import com.amarchaud.ui.theme.PaginationDemoTheme
import com.amarchaud.ui.theme.spacing
import androidx.compose.ui.res.stringResource

private val AvatarSize = 84.dp

@Composable
internal fun UserCard(
    user: UserGenericUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) 0.96f else 1f, label = "card press")

    Surface(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        interactionSource = interactionSource,
    ) {
        Box {
            if (user.flag.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(MaterialTheme.spacing.m),
                    text = user.flag,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.m, vertical = MaterialTheme.spacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UserAvatar(
                    modifier = Modifier
                        .sharedUserAvatar(user.localId)
                        .size(AvatarSize),
                    imageUrl = user.imageUrl,
                    contentDescription = stringResource(R.string.avatar_description, user.fullName),
                )
                Spacer(Modifier.height(MaterialTheme.spacing.l))
                Text(
                    text = user.age?.let { stringResource(R.string.card_name_age, user.firstName, it) }
                        ?: user.firstName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = user.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/** Skeleton with the exact geometry of [UserCard]. */
@Composable
internal fun UserCardPlaceholder(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.m, vertical = MaterialTheme.spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.size(AvatarSize).shimmer(CircleShape))
            Spacer(Modifier.height(MaterialTheme.spacing.l))
            Box(
                Modifier
                    .fillMaxWidth(0.6f)
                    .height(18.dp)
                    .shimmer(MaterialTheme.shapes.extraSmall)
            )
            Spacer(Modifier.height(MaterialTheme.spacing.s))
            Box(
                Modifier
                    .fillMaxWidth(0.8f)
                    .height(12.dp)
                    .shimmer(MaterialTheme.shapes.extraSmall)
            )
        }
    }
}

internal val previewUser = UserGenericUiModel(
    localId = 1,
    title = "Ms",
    firstName = "Leta",
    fullName = "Leta Chapman",
    age = 33,
    location = "Clarksville, United States",
    flag = "🇺🇸",
)

@Preview
@Composable
private fun UserCardPreview() {
    PaginationDemoTheme {
        UserCard(modifier = Modifier.size(180.dp, 220.dp), user = previewUser, onClick = {})
    }
}

@Preview
@Composable
private fun UserCardPlaceholderPreview() {
    PaginationDemoTheme {
        UserCardPlaceholder(modifier = Modifier.size(180.dp, 220.dp))
    }
}
