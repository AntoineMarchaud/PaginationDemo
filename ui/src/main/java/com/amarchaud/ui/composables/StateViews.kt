package com.amarchaud.ui.composables

import com.amarchaud.ui.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amarchaud.ui.theme.PaginationDemoTheme
import com.amarchaud.ui.theme.spacing
import androidx.annotation.DrawableRes
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

/** Centered empty / error state with an optional call to action. */
@Composable
internal fun FullScreenMessage(
    @DrawableRes icon: Int,
    title: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = MaterialTheme.spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Spacer(Modifier.height(MaterialTheme.spacing.xl))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(MaterialTheme.spacing.s))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(MaterialTheme.spacing.xl))
        Button(onClick = onAction) {
            Text(text = actionLabel, style = MaterialTheme.typography.labelLarge)
        }
    }
}

/** Compact error banner shown above or below existing content. */
@Composable
internal fun InlineError(
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
    ) {
        Row(
            modifier = Modifier.padding(start = MaterialTheme.spacing.l, end = MaterialTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_cloud_off),
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = MaterialTheme.spacing.m, vertical = MaterialTheme.spacing.m),
                text = message,
                style = MaterialTheme.typography.bodyMedium,
            )
            TextButton(onClick = onAction) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FullScreenMessagePreview() {
    PaginationDemoTheme {
        FullScreenMessage(
            icon = R.drawable.ic_cloud_off,
            title = "Couldn't load people",
            message = "No internet connection",
            actionLabel = "Try again",
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun InlineErrorPreview() {
    PaginationDemoTheme {
        InlineError(message = "No internet connection", actionLabel = "Retry", onAction = {})
    }
}
