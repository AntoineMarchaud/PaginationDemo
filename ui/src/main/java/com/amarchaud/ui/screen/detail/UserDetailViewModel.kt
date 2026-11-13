package com.amarchaud.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amarchaud.domain.usecase.GetOneUserUseCase
import com.amarchaud.ui.screen.detail.mappers.toDetailUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = UserDetailViewModel.UserViewModelFactory::class)
class UserDetailViewModel @AssistedInject constructor(
    private val getOneUserUseCase: GetOneUserUseCase,
    @Assisted private val localId: Long
) : ViewModel() {

    @AssistedFactory
    interface UserViewModelFactory {
        fun create(localId: Long): UserDetailViewModel
    }

    private val _state = MutableStateFlow(UserDetailState())
    val state: StateFlow<UserDetailState> = _state.asStateFlow()

    private val _events = Channel<UserDetailEvent>(Channel.BUFFERED)
    internal val events: Flow<UserDetailEvent> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            getOneUserUseCase.run(localId)?.let { userModel ->
                _state.update { it.copy(user = userModel.toDetailUiModel()) }
            }
        }
    }

    fun onAction(action: UserDetailAction) {
        val user = _state.value.user
        when (action) {
            UserDetailAction.BackClicked -> _events.trySend(UserDetailEvent.NavigateBack)
            UserDetailAction.EmailClicked -> openUri(user.emailUri)
            UserDetailAction.PhoneClicked -> openUri(user.phoneUri)
            UserDetailAction.AddressClicked -> openUri(user.mapsUri)
        }
    }

    private fun openUri(uri: String) {
        if (uri.isNotEmpty()) _events.trySend(UserDetailEvent.OpenUri(uri))
    }
}
