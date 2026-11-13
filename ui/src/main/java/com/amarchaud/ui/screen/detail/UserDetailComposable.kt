package com.amarchaud.ui.screen.detail

import com.amarchaud.ui.R
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amarchaud.ui.mvi.ObserveAsEvents
import com.amarchaud.ui.screen.detail.composables.InfoItem
import com.amarchaud.ui.screen.detail.composables.InfoSection
import com.amarchaud.ui.screen.detail.composables.ProfileHeader
import com.amarchaud.ui.screen.detail.composables.QuickActions
import com.amarchaud.ui.screen.detail.models.UserDetailUiModel
import com.amarchaud.ui.screen.mainList.composables.previewUser
import com.amarchaud.ui.theme.PaginationDemoTheme
import com.amarchaud.ui.theme.spacing
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun UserDetailComposable(
    viewModel: UserDetailViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            UserDetailEvent.NavigateBack -> onBack()
            // no app able to handle the link (no mail app on a simulator...): ignore
            is UserDetailEvent.OpenUri -> runCatching { uriHandler.openUri(event.uri) }
        }
    }

    UserDetailScreen(
        user = state.user,
        onAction = viewModel::onAction,
    )
}

/** Max width of the actions and sections on large screens. */
private val MaxContentWidth = 640.dp

@Composable
private fun UserDetailScreen(
    user: UserDetailUiModel,
    onAction: (UserDetailAction) -> Unit,
    scrollState: ScrollState = rememberScrollState(),
) {
    val spacing = MaterialTheme.spacing

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ProfileHeader(user = user, scrollState = scrollState)

            // tablets, foldables, landscape: readable width, centered under the full-width header
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .widthIn(max = MaxContentWidth)
                    .fillMaxWidth()
            ) {
                Spacer(Modifier.height(spacing.xl))
                QuickActions(user = user, onAction = onAction)

                Spacer(Modifier.height(spacing.xl))
                InfoSection(
                    modifier = Modifier.padding(horizontal = spacing.l),
                    title = stringResource(R.string.section_contact),
                    items = listOf(
                        InfoItem(
                            icon = R.drawable.ic_mail,
                            label = stringResource(R.string.label_email),
                            value = user.mainInfo.email,
                            onClick = { onAction(UserDetailAction.EmailClicked) },
                        ),
                        InfoItem(
                            icon = R.drawable.ic_phone,
                            label = stringResource(R.string.label_phone),
                            value = user.phoneNumber,
                            onClick = { onAction(UserDetailAction.PhoneClicked) },
                        ),
                    ),
                )

                Spacer(Modifier.height(spacing.xl))
                InfoSection(
                    modifier = Modifier.padding(horizontal = spacing.l),
                    title = stringResource(R.string.section_about),
                    items = listOf(
                        InfoItem(
                            icon = R.drawable.ic_place,
                            label = stringResource(R.string.label_address),
                            value = user.address,
                            onClick = { onAction(UserDetailAction.AddressClicked) },
                        ),
                        InfoItem(
                            icon = R.drawable.ic_cake,
                            label = stringResource(R.string.label_birthday),
                            value = user.birthday,
                        ),
                    ),
                )
            }

            Spacer(Modifier.height(spacing.xl))
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }

        BackButton(
            modifier = Modifier
                .statusBarsPadding()
                .padding(spacing.l),
            onClick = { onAction(UserDetailAction.BackClicked) },
        )
    }
}

@Composable
private fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.size(44.dp),
        onClick = onClick,
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.35f),
        contentColor = Color.White,
    ) {
        Icon(
            modifier = Modifier.padding(10.dp),
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = stringResource(R.string.back),
        )
    }
}

@Preview
@Composable
private fun UserDetailScreenPreview() {
    PaginationDemoTheme {
        UserDetailScreen(
            user = UserDetailUiModel(
                mainInfo = previewUser.copy(email = "leta.chapman@example.com"),
                address = "1913 Oak Lawn Ave Clarksville",
                phoneNumber = "(568)-263-5288",
                birthday = "April 23, 1993",
                emailUri = "mailto:leta.chapman@example.com",
                phoneUri = "tel:5682635288",
                mapsUri = "https://www.google.com/maps/search/?api=1&query=0,0",
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun UserDetailScreenDarkPreview() {
    PaginationDemoTheme(darkTheme = true) {
        UserDetailScreen(
            user = UserDetailUiModel(
                mainInfo = previewUser,
                address = "1913 Oak Lawn Ave Clarksville",
                birthday = "April 23, 1993",
            ),
            onAction = {},
        )
    }
}
