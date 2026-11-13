package com.amarchaud.ui.screen.detail.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amarchaud.ui.theme.spacing
import androidx.annotation.DrawableRes
import androidx.compose.ui.res.painterResource

internal data class InfoItem(
    @param:DrawableRes val icon: Int,
    val label: String,
    val value: String,
    val onClick: (() -> Unit)? = null,
)

/** Titled card of label / value rows. Rows without a value are skipped. */
@Composable
internal fun InfoSection(
    title: String,
    items: List<InfoItem>,
    modifier: Modifier = Modifier,
) {
    val visibleItems = items.filter { it.value.isNotBlank() }
    if (visibleItems.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(start = MaterialTheme.spacing.xs, bottom = MaterialTheme.spacing.s),
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        ) {
            Column {
                visibleItems.forEachIndexed { index, item ->
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 72.dp),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                    }
                    InfoRow(item)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(item: InfoItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (item.onClick != null) Modifier.clickable(onClick = item.onClick) else Modifier)
            .padding(horizontal = MaterialTheme.spacing.l, vertical = MaterialTheme.spacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(item.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Spacer(Modifier.width(MaterialTheme.spacing.l))
        Column {
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
