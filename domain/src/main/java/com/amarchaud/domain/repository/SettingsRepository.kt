package com.amarchaud.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    /** "Material You": use the system dynamic colors instead of the app palette. */
    val materialYouEnabled: Flow<Boolean>
    suspend fun setMaterialYouEnabled(enabled: Boolean)
}
