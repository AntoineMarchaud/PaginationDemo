package com.amarchaud.ui.screen.mainList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amarchaud.domain.usecase.GetRandomUsersUseCase
import com.amarchaud.ui.screen.mainList.mappers.toGenericUiModel
import com.amarchaud.ui.screen.mainList.models.UserGenericUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainListViewModel(
    getRandomUsersUseCase: GetRandomUsersUseCase
) : ViewModel() {
    val users: Flow<PagingData<UserGenericUiModel>> = getRandomUsersUseCase
        .run()
        .map { pagingData ->
            pagingData.map { user ->
                user.toGenericUiModel()
            }
        }
        .cachedIn(viewModelScope)
}