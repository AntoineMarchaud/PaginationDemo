package com.amarchaud.data.settings

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImplTest {

    private val preferences = FakeSharedPreferences()

    @Test
    fun materialYouDisabledByDefault() = runTest {
        assertFalse(SettingsRepositoryImpl(preferences).materialYouEnabled.first())
    }

    @Test
    fun materialYouIsPersisted() = runTest {
        SettingsRepositoryImpl(preferences).setMaterialYouEnabled(true)

        // a new instance (next app launch) reads the stored value
        assertTrue(SettingsRepositoryImpl(preferences).materialYouEnabled.first())
    }

    @Test
    fun changesAreEmitted() = runTest {
        val repository = SettingsRepositoryImpl(preferences)
        val emitted = mutableListOf<Boolean>()
        backgroundScope.launch { repository.materialYouEnabled.collect { emitted += it } }
        runCurrent()

        repository.setMaterialYouEnabled(true)
        runCurrent()
        repository.setMaterialYouEnabled(false)
        runCurrent()

        assertEquals(listOf(false, true, false), emitted)
    }
}
