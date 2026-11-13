package com.amarchaud.ui.screen.mainList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amarchaud.domain.usecase.GetRandomUsersUseCase
import com.amarchaud.ui.screen.mainList.models.UserGenericUiModel
import com.amarchaud.ui.screen.mappers.toGenericUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor(
    getRandomUsersUseCase: GetRandomUsersUseCase
) : ViewModel() {

    val users: Flow<PagingData<UserGenericUiModel>> = getRandomUsersUseCase
        .run()
        .map { paging -> paging.map { user -> user.toGenericUiModel() } }
        .cachedIn(viewModelScope)

    private val _events = Channel<MainListEvent>(Channel.BUFFERED)
    internal val events: Flow<MainListEvent> = _events.receiveAsFlow()

    fun onAction(action: MainListAction) {
        when (action) {
            is MainListAction.UserClicked -> _events.trySend(MainListEvent.NavigateToDetail(action.localId))
            MainListAction.Refresh -> _events.trySend(MainListEvent.RefreshList)
        }
    }
}
