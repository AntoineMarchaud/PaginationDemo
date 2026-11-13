package com.amarchaud.domain

import com.amarchaud.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSettingsRepository(materialYou: Boolean = false) : SettingsRepository {

    private val _materialYouEnabled = MutableStateFlow(materialYou)
    override val materialYouEnabled: StateFlow<Boolean> = _materialYouEnabled

    override suspend fun setMaterialYouEnabled(enabled: Boolean) {
        _materialYouEnabled.value = enabled
    }
}
