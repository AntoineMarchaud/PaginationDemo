package com.amarchaud.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amarchaud.domain.usecase.ObserveMaterialYouUseCase
import com.amarchaud.domain.usecase.SetMaterialYouUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** App-wide settings (activity scope): currently the "Material You" theme option. */
@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    observeMaterialYouUseCase: ObserveMaterialYouUseCase,
    private val setMaterialYouUseCase: SetMaterialYouUseCase,
) : ViewModel() {

    val state: StateFlow<AppSettingsState> = observeMaterialYouUseCase.run()
        .map { AppSettingsState(materialYou = it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppSettingsState())

    fun onAction(action: AppSettingsAction) {
        when (action) {
            is AppSettingsAction.MaterialYouToggled -> viewModelScope.launch {
                setMaterialYouUseCase.run(action.enabled)
            }
        }
    }
}
