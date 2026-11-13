package com.amarchaud.ui.screen.mappers

import com.amarchaud.domain.models.ErrorApiModel
import com.amarchaud.ui.R
import com.amarchaud.ui.screen.mainList.models.ErrorApiUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorMessageMapperTest {

    @Test
    fun domainErrorThrowableIsMappedToUiModel() {
        val error: Throwable = ErrorApiModel.NoInternetError()

        assertEquals(ErrorApiUiModel.NoInternetError, error.toErrorUiModel())
    }

    @Test
    fun unknownThrowableBecomesGenericError() {
        assertEquals(ErrorApiUiModel.GenericError, IllegalStateException("boom").toErrorUiModel())
    }

    @Test
    fun serverErrorMessageCarriesTheCode() {
        assertEquals(
            ErrorMessage(R.string.error_ApiServerErrorWithCode, listOf(503)),
            ErrorApiUiModel.ApiServerErrorWithCode(503, "Service Unavailable").toErrorMessage(),
        )
    }

    @Test
    fun noInternetHasItsOwnMessage() {
        assertEquals(ErrorMessage(R.string.error_NoInternetError), ErrorApiUiModel.NoInternetError.toErrorMessage())
    }

    @Test
    fun genericErrorHasItsOwnMessage() {
        assertEquals(ErrorMessage(R.string.error_GenericError), ErrorApiUiModel.GenericError.toErrorMessage())
    }
}
