package com.amarchaud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.amarchaud.ui.composables.LocalNavAnimatedVisibilityScope
import com.amarchaud.ui.composables.LocalSharedTransitionScope
import com.amarchaud.ui.screen.detail.UserDetailComposable
import com.amarchaud.ui.screen.detail.UserDetailViewModel
import com.amarchaud.ui.screen.mainList.MainListComposable
import com.amarchaud.ui.settings.AppSettingsAction
import com.amarchaud.ui.settings.AppSettingsViewModel
import com.amarchaud.ui.theme.PaginationDemoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
data class Detail(val id: Long) : NavKey

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PaginationDemoApp()
        }
    }
}

private const val TRANSITION_MS = 350

private fun fade(): ContentTransform = fadeIn(tween(TRANSITION_MS)) togetherWith fadeOut(tween(TRANSITION_MS))

@Composable
private fun PaginationDemoApp() {
    // activity-scoped: the theme option applies to every screen
    val settingsViewModel: AppSettingsViewModel = hiltViewModel()
    val settings by settingsViewModel.state.collectAsStateWithLifecycle()

    PaginationDemoTheme(dynamicColor = settings.materialYou) {
        Surface(color = MaterialTheme.colorScheme.background) {
            // the avatar morphs between the list and the detail screen
            SharedTransitionLayout {
                CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                    val backStack = rememberNavBackStack(Home)

                    NavDisplay(
                        backStack = backStack,
                        modifier = Modifier.fillMaxSize(),
                        onBack = { backStack.removeLastOrNull() },
                        entryDecorators = listOf(
                            // Add the default decorators for managing scenes and saving state
                            rememberSaveableStateHolderNavEntryDecorator(),
                            // Then add the view model store decorator
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        transitionSpec = { fade() },
                        popTransitionSpec = { fade() },
                        predictivePopTransitionSpec = { fade() },
                        entryProvider = entryProvider {
                            entry<Home> {
                                NavEntryScope {
                                    MainListComposable(
                                        materialYouEnabled = settings.materialYou,
                                        onMaterialYouToggled = { enabled ->
                                            settingsViewModel.onAction(AppSettingsAction.MaterialYouToggled(enabled))
                                        },
                                        onUserClick = { userId -> backStack.add(Detail(userId)) }
                                    )
                                }
                            }

                            entry<Detail> { key ->
                                NavEntryScope {
                                    // ViewModel with assisted injection for navigation args
                                    val viewModel = hiltViewModel<UserDetailViewModel, UserDetailViewModel.UserViewModelFactory>(
                                        creationCallback = { factory -> factory.create(localId = key.id) }
                                    )

                                    UserDetailComposable(
                                        viewModel = viewModel,
                                        onBack = { backStack.removeLastOrNull() }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

/** Exposes the destination's animation scope to the shared element modifiers of the ui module. */
@Composable
private fun NavEntryScope(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalNavAnimatedVisibilityScope provides LocalNavAnimatedContentScope.current,
        content = content,
    )
}
