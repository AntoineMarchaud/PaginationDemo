package com.amarchaud.ui.screen.detail

import com.amarchaud.ui.screen.detail.models.UserDetailUiModel

data class UserDetailState(
    val user: UserDetailUiModel = UserDetailUiModel(),
)

sealed interface UserDetailAction {
    data object BackClicked : UserDetailAction
    data object EmailClicked : UserDetailAction
    data object PhoneClicked : UserDetailAction
    data object AddressClicked : UserDetailAction
}

sealed interface UserDetailEvent {
    data object NavigateBack : UserDetailEvent

    /** Opens the mail app, the dialer or a map (handled by the platform UriHandler). */
    data class OpenUri(val uri: String) : UserDetailEvent
}
