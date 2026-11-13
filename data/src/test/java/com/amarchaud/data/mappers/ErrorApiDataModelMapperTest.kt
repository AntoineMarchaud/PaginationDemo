package com.amarchaud.data.mappers

import com.amarchaud.data.models.ErrorApiDataModel
import com.amarchaud.domain.models.ErrorApiModel
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class ErrorApiDataModelMapperTest {

    @Test
    fun serverErrorWithCodeKeepsCodeAndMessage() {
        val domain = ErrorApiDataModel.ApiServerErrorWithCode(503, "unavailable").toDomain()

        assertTrue(domain is ErrorApiModel.ApiServerErrorWithCode)
        domain as ErrorApiModel.ApiServerErrorWithCode
        assertEquals(503, domain.responseCode)
        assertEquals("unavailable", domain.responseMessage)
    }

    @Test
    fun mapsEveryOtherCase() {
        assertTrue(ErrorApiDataModel.ApiNullBody().toDomain() is ErrorApiModel.ApiNullBody)
        assertTrue(ErrorApiDataModel.ApiGenericServerError().toDomain() is ErrorApiModel.ApiGenericServerError)
        assertTrue(ErrorApiDataModel.SocketTimeOutError().toDomain() is ErrorApiModel.SocketTimeOutError)
        assertTrue(ErrorApiDataModel.NoInternetError().toDomain() is ErrorApiModel.NoInternetError)
    }
}
