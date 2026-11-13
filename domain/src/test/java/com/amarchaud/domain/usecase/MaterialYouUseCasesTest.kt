package com.amarchaud.domain.usecase

import com.amarchaud.domain.FakeSettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MaterialYouUseCasesTest {

    @Test
    fun observeReturnsStoredValue() = runTest {
        val repository = FakeSettingsRepository(materialYou = true)

        assertTrue(ObserveMaterialYouUseCase(repository).run().first())
    }

    @Test
    fun setStoresValue() = runTest {
        val repository = FakeSettingsRepository(materialYou = false)

        SetMaterialYouUseCase(repository).run(enabled = true)

        assertTrue(repository.materialYouEnabled.value)
    }

    @Test
    fun disabledByDefault() = runTest {
        assertFalse(ObserveMaterialYouUseCase(FakeSettingsRepository()).run().first())
    }
}
