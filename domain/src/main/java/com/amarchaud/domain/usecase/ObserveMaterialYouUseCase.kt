package com.amarchaud.domain.usecase

import com.amarchaud.domain.repository.SettingsRepository
import javax.inject.Inject

class ObserveMaterialYouUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    fun run() = repository.materialYouEnabled
}
