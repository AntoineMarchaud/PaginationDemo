package com.amarchaud.ui.screen.mainList.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amarchaud.ui.R
import com.amarchaud.ui.composables.ImageLoaderSubCompose
import com.amarchaud.ui.composables.ShimmerAnimationItem
import com.amarchaud.ui.screen.mainList.heightOneCell
import com.amarchaud.ui.screen.mainList.mockUser
import com.amarchaud.ui.screen.mainList.models.UserGenericUiModel
import com.amarchaud.ui.theme.PaginationDemoTheme

const val OneUserSharedId = "oneUser_"

@Composable
internal fun OneUser(
    modifier: Modifier = Modifier,
    user: UserGenericUiModel,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImageLoaderSubCompose(
            modifier = Modifier
                .height(heightOneCell)
                .aspectRatio(ratio = 1f)
                .clip(shape = CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            data = user.imageUrl,
            loading = {
                ShimmerAnimationItem()
            },
            failure = {
                Image(
                    painter = painterResource(id = R.drawable.ic_error_24dp),
                    contentDescription = "error loading image"
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = user.completeName)

        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(start = 32.dp),
        )
    }
}

@Preview
@Composable
private fun OneUserPreview() {
    PaginationDemoTheme {
        OneUser(
            modifier = Modifier,
            user = mockUser,
            onClick = {}
        )
    }
}