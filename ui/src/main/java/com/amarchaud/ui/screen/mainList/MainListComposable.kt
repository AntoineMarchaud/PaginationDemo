package com.amarchaud.ui.screen.mainList

import com.amarchaud.ui.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.amarchaud.domain.models.ErrorApiModel
import com.amarchaud.ui.composables.FullScreenMessage
import com.amarchaud.ui.composables.InlineError
import com.amarchaud.ui.mvi.ObserveAsEvents
import com.amarchaud.ui.screen.mainList.composables.UserCard
import com.amarchaud.ui.screen.mainList.composables.UserCardPlaceholder
import com.amarchaud.ui.screen.mainList.composables.previewUser
import com.amarchaud.ui.screen.mainList.models.UserGenericUiModel
import com.amarchaud.ui.screen.mappers.errorMessage
import com.amarchaud.ui.settings.ThemeMenu
import com.amarchaud.ui.theme.PaginationDemoTheme
import com.amarchaud.ui.theme.isDynamicColorSupported
import com.amarchaud.ui.theme.spacing
import kotlinx.coroutines.flow.flowOf
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

private const val PLACEHOLDER_COUNT = 8

@Composable
fun MainListComposable(
    viewModel: MainListViewModel = hiltViewModel(),
    materialYouEnabled: Boolean = false,
    onMaterialYouToggled: (Boolean) -> Unit = {},
    onUserClick: (Long) -> Unit
) {
    val users = viewModel.users.collectAsLazyPagingItems()

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is MainListEvent.NavigateToDetail -> onUserClick(event.localId)
            MainListEvent.RefreshList -> users.refresh()
        }
    }

    MainListScreen(
        users = users,
        onUserClick = { localId -> viewModel.onAction(MainListAction.UserClicked(localId)) },
        onRefresh = { viewModel.onAction(MainListAction.Refresh) },
        materialYouEnabled = materialYouEnabled,
        onMaterialYouToggled = onMaterialYouToggled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainListScreen(
    users: LazyPagingItems<UserGenericUiModel>,
    onUserClick: (Long) -> Unit,
    onRefresh: () -> Unit,
    materialYouEnabled: Boolean = false,
    onMaterialYouToggled: (Boolean) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val refreshState = users.loadState.refresh
    val isEmpty = users.itemCount == 0

    // The app bar scroll connection is set on the scrolling content, INSIDE the pull-to-refresh box:
    // pulling down first expands the "Discover" title, then shows the refresh indicator.
    val appBarScroll = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        // the grid draws behind the navigation bar and pads its content instead
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            LargeTopAppBar(
                // no explicit style: the bar uses headlineMedium expanded and titleLarge collapsed
                title = { Text(text = stringResource(R.string.discover_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                ),
                actions = {
                    // "Material You" (dynamic colors) only exists on Android 12+
                    if (isDynamicColorSupported) {
                        ThemeMenu(
                            materialYouEnabled = materialYouEnabled,
                            onMaterialYouToggled = onMaterialYouToggled,
                        )
                    }
                },
                expandedHeight = 112.dp,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            isRefreshing = refreshState is LoadState.Loading && !isEmpty,
            onRefresh = onRefresh,
        ) {
            if (refreshState is LoadState.Error && isEmpty) {
                FullScreenMessage(
                    // scrollable so that pull-to-refresh also works on this state
                    modifier = appBarScroll
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    icon = R.drawable.ic_cloud_off,
                    title = stringResource(R.string.error_title),
                    message = refreshState.error.errorMessage().replaceFirstChar { it.uppercase() },
                    actionLabel = stringResource(R.string.try_again),
                    onAction = onRefresh,
                )
            } else {
                UsersGrid(
                    modifier = appBarScroll,
                    users = users,
                    onUserClick = onUserClick,
                    onRefresh = onRefresh,
                )
            }
        }
    }
}

@Composable
private fun UsersGrid(
    users: LazyPagingItems<UserGenericUiModel>,
    onUserClick: (Long) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = MaterialTheme.spacing
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(
            start = spacing.l,
            end = spacing.l,
            bottom = spacing.l + bottomInset,
        ),
        horizontalArrangement = Arrangement.spacedBy(spacing.m),
        verticalArrangement = Arrangement.spacedBy(spacing.m),
    ) {
        fullWidthItem {
            Text(
                modifier = Modifier.padding(bottom = spacing.s),
                text = stringResource(R.string.discover_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        val refreshState = users.loadState.refresh
        if (refreshState is LoadState.Error) {
            fullWidthItem {
                InlineError(
                    message = stringResource(R.string.error_refresh, refreshState.error.errorMessage()),
                    actionLabel = stringResource(R.string.retry),
                    onAction = onRefresh,
                )
            }
        }

        if (refreshState is LoadState.Loading && users.itemCount == 0) {
            items(count = PLACEHOLDER_COUNT) { UserCardPlaceholder() }
        } else {
            items(
                count = users.itemCount,
                key = users.itemKey { it.localId },
            ) { index ->
                val user = users[index]
                if (user != null) {
                    UserCard(
                        modifier = Modifier.animateItem(),
                        user = user,
                        onClick = { onUserClick(user.localId) },
                    )
                } else {
                    UserCardPlaceholder()
                }
            }
        }

        when (val appendState = users.loadState.append) {
            is LoadState.Loading -> fullWidthItem {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.l),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
                }
            }

            is LoadState.Error -> fullWidthItem {
                InlineError(
                    message = stringResource(R.string.error_append, appendState.error.errorMessage()),
                    actionLabel = stringResource(R.string.retry),
                    onAction = onRefresh,
                )
            }

            is LoadState.NotLoading -> Unit
        }
    }
}

private fun LazyGridScope.fullWidthItem(content: @Composable () -> Unit) {
    item(span = { GridItemSpan(maxLineSpan) }) { content() }
}

// region Previews

@Composable
private fun previewItems(
    data: List<UserGenericUiModel>,
    refresh: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
    append: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
) = flowOf(
    PagingData.from(
        data = data,
        sourceLoadStates = LoadStates(
            refresh = refresh,
            append = append,
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
        ),
    )
).collectAsLazyPagingItems()

private val previewUsers = List(6) { previewUser.copy(localId = it.toLong()) }

@Preview
@Composable
private fun MainListScreenPreview() {
    PaginationDemoTheme {
        MainListScreen(users = previewItems(previewUsers), onUserClick = {}, onRefresh = {})
    }
}

@Preview
@Composable
private fun MainListScreenLoadingPreview() {
    PaginationDemoTheme {
        MainListScreen(users = previewItems(emptyList(), refresh = LoadState.Loading), onUserClick = {}, onRefresh = {})
    }
}

@Preview
@Composable
private fun MainListScreenErrorPreview() {
    PaginationDemoTheme {
        MainListScreen(
            users = previewItems(emptyList(), refresh = LoadState.Error(ErrorApiModel.NoInternetError())),
            onUserClick = {},
            onRefresh = {},
        )
    }
}

@Preview
@Composable
private fun MainListScreenAppendErrorPreview() {
    PaginationDemoTheme(darkTheme = true) {
        MainListScreen(
            users = previewItems(previewUsers, append = LoadState.Error(ErrorApiModel.SocketTimeOutError())),
            onUserClick = {},
            onRefresh = {},
        )
    }
}

// endregion
