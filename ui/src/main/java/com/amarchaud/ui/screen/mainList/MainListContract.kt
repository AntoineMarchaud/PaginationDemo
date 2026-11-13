package com.amarchaud.ui.screen.mainList

// No State class: the screen has no state of its own besides the paging flow
// (animation flags stay local to the composable).

sealed interface MainListAction {
    data class UserClicked(val localId: Long) : MainListAction
    data object Refresh : MainListAction
}

sealed interface MainListEvent {
    data class NavigateToDetail(val localId: Long) : MainListEvent

    // Paging can only be refreshed from the presenter (LazyPagingItems.refresh()):
    // keeps the displayed list and merges duplicate refreshes.
    data object RefreshList : MainListEvent
}
