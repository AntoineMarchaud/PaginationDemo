@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.amarchaud.ui.screen.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.amarchaud.ui.R
import com.amarchaud.ui.composables.ImageLoaderSubCompose
import com.amarchaud.ui.composables.ShimmerAnimationItem
import com.amarchaud.ui.screen.detail.composables.IdentityCard
import com.amarchaud.ui.screen.detail.models.UserDetailUiModel
import com.amarchaud.ui.screen.mainList.composables.OneUserSharedId
import com.amarchaud.ui.screen.mainList.mockUser
import com.amarchaud.ui.theme.PaginationDemoTheme
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun UserDetailComposable(
    viewModel: UserDetailViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit
) {
    val user by viewModel.userDetailUiModel.collectAsState()

    BackHandler {
        onBack()
    }

    UserDetailScreen(
        user = user,
        onBack = onBack,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}

@Composable
private fun UserDetailScreen(
    user: UserDetailUiModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            with(sharedTransitionScope) {
                ImageLoaderSubCompose(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "${OneUserSharedId}-${user.mainInfo.localId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                        )
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    data = user.mainImageUrl,
                    loading = {
                        ShimmerAnimationItem()
                    },
                    failure = {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(ratio = 1.0f),
                            painter = painterResource(id = R.drawable.ic_error_24dp),
                            contentDescription = "error loading"
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            IdentityCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                user = user
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (LocalInspectionMode.current) { // preview
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio = 1f)
                        .background(color = Color.Black),
                    text = "this is a map",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            } else {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio = 1f)
                        .clipToBounds(),
                    factory = { context ->
                        // Creates the view
                        MapView(context).apply {
                            setTileSource(TileSourceFactory.USGS_TOPO)
                        }
                    },
                    update = { view ->
                        view.controller.setCenter(user.coordinates)
                        view.controller.setZoom(8.0)

                        Marker(view).apply {
                            this.position = user.coordinates
                            this.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        }.also {
                            view.overlays.add(it)
                        }
                    }
                )
            }
        }

        OutlinedButton(
            onClick = onBack,
            shape = CircleShape,
            border = BorderStroke(
                2.dp,
                Color.White,
            ),
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 16.dp, top = 16.dp)
                .size(56.dp),
            colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Gray),
            contentPadding = PaddingValues(8.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "back",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun SharedTransitionScope.UserDetailScreenForPreview(
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    UserDetailScreen(
        user = UserDetailUiModel(
            mainInfo = mockUser,
        ),
        onBack = {},
        sharedTransitionScope = this,
        animatedVisibilityScope = animatedVisibilityScope
    )
}

@Preview
@Composable
private fun UserDetailScreenPreview() {
    PaginationDemoTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                UserDetailScreenForPreview(
                    animatedVisibilityScope = this
                )
            }
        }
    }
}