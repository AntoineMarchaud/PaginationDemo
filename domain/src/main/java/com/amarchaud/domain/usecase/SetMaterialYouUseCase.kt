package com.amarchaud.domain.usecase

import com.amarchaud.domain.repository.SettingsRepository
import javax.inject.Inject

class SetMaterialYouUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend fun run(enabled: Boolean) = repository.setMaterialYouEnabled(enabled)
}
