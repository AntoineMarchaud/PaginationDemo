package com.amarchaud.data.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import com.amarchaud.domain.repository.SettingsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val preferences: SharedPreferences
) : SettingsRepository {

    override val materialYouEnabled: Flow<Boolean> = callbackFlow {
        trySend(preferences.getBoolean(KEY_MATERIAL_YOU, false))
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == KEY_MATERIAL_YOU) trySend(prefs.getBoolean(KEY_MATERIAL_YOU, false))
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.distinctUntilChanged()

    override suspend fun setMaterialYouEnabled(enabled: Boolean) {
        preferences.edit { putBoolean(KEY_MATERIAL_YOU, enabled) }
    }

    private companion object {
        const val KEY_MATERIAL_YOU = "material_you"
    }
}
