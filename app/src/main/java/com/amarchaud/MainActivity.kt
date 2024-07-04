package com.amarchaud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.amarchaud.ui.screen.detail.UserDetailComposable
import com.amarchaud.ui.screen.detail.UserDetailViewModel
import com.amarchaud.ui.screen.mainList.MainListComposable
import com.amarchaud.ui.theme.PaginationDemoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Serializable
    data object Home : NavKey

    @Serializable
    data class Detail(val id: Long) : NavKey

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PaginationDemoTheme {
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
                    entryProvider = entryProvider {
                        entry<Home> {
                            MainListComposable(
                                onUserClick = { userId ->
                                    backStack.add(Detail(userId))
                                }
                            )
                        }

                        entry<Detail> { key ->
                            // ViewModel with assisted injection for navigation args
                            val viewModel = hiltViewModel<UserDetailViewModel, UserDetailViewModel.UserViewModelFactory>(
                                creationCallback = { factory ->
                                    factory.create(localId = key.id)
                                }
                            )

                            UserDetailComposable(
                                viewModel = viewModel,
                                onBack = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}
