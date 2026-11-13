package com.amarchaud.ui.settings

import com.amarchaud.domain.repository.SettingsRepository
import com.amarchaud.domain.usecase.ObserveMaterialYouUseCase
import com.amarchaud.domain.usecase.SetMaterialYouUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppSettingsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private class FakeSettingsRepository(materialYou: Boolean) : SettingsRepository {
        private val _materialYouEnabled = MutableStateFlow(materialYou)
        override val materialYouEnabled: StateFlow<Boolean> = _materialYouEnabled
        override suspend fun setMaterialYouEnabled(enabled: Boolean) {
            _materialYouEnabled.value = enabled
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(repository: SettingsRepository) = AppSettingsViewModel(
        observeMaterialYouUseCase = ObserveMaterialYouUseCase(repository),
        setMaterialYouUseCase = SetMaterialYouUseCase(repository),
    )

    @Test
    fun stateReflectsStoredValue() = runTest(dispatcher) {
        val viewModel = viewModel(FakeSettingsRepository(materialYou = true))

        advanceUntilIdle()

        assertEquals(AppSettingsState(materialYou = true), viewModel.state.value)
    }

    @Test
    fun toggleStoresAndUpdatesState() = runTest(dispatcher) {
        val repository = FakeSettingsRepository(materialYou = false)
        val viewModel = viewModel(repository)
        advanceUntilIdle()

        viewModel.onAction(AppSettingsAction.MaterialYouToggled(enabled = true))
        advanceUntilIdle()

        assertTrue(repository.materialYouEnabled.value)
        assertEquals(AppSettingsState(materialYou = true), viewModel.state.value)
    }
}
