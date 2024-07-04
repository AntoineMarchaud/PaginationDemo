package com.amarchaud.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amarchaud.domain.usecase.GetOneUserUseCase
import com.amarchaud.ui.screen.detail.models.UserDetailUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import toDetailUiModel
import javax.inject.Inject

@HiltViewModel(assistedFactory = UserDetailViewModel.UserViewModelFactory::class)
class UserDetailViewModel @AssistedInject constructor(
    private val stateHandle: SavedStateHandle,
    private val getOneUserUseCase: GetOneUserUseCase,
    @Assisted private val localId: Long
) : ViewModel() {

    @AssistedFactory
    interface UserViewModelFactory {
        fun create(localId: Long): UserDetailViewModel
    }

    /*
    // new way in navigation 2.8
    private val localId =
        stateHandle.get<Long>("id") ?: throw NullPointerException("localId can't be null")*/

    private val _userDetailUiModel = MutableStateFlow(UserDetailUiModel())
    val userDetailUiModel = _userDetailUiModel.asStateFlow()

    init {
        viewModelScope.launch {
            getOneUserUseCase.run(localId)?.let { userModel ->
                _userDetailUiModel.update {
                    userModel.toDetailUiModel()
                }
            }
        }
    }
}