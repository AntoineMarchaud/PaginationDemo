package com.amarchaud.ui.settings

data class AppSettingsState(
    val materialYou: Boolean = false,
)

sealed interface AppSettingsAction {
    data class MaterialYouToggled(val enabled: Boolean) : AppSettingsAction
}
