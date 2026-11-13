package com.amarchaud.data.api

import com.amarchaud.data.models.ErrorApiDataModel
import com.amarchaud.data.models.ResultsDataModel
import com.amarchaud.data.models.UserDataModel
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

class ApiResultTest {

    private val body = ResultsDataModel(users = listOf(UserDataModel(email = "a@b.c")))

    @Test
    fun successReturnsBody() = runTest {
        val result = apiResult { Response.success(body) }

        assertEquals(body, result.getOrThrow())
    }

    @Test
    fun httpErrorIsApiServerErrorWithCode() = runTest {
        val error = apiResult<ResultsDataModel> { Response.error(503, "".toResponseBody()) }.exceptionOrNull()

        assertTrue(error is ErrorApiDataModel.ApiServerErrorWithCode)
        assertEquals(503, (error as ErrorApiDataModel.ApiServerErrorWithCode).responseCode)
    }

    @Test
    fun nullBodyIsApiNullBody() = runTest {
        val error = apiResult<ResultsDataModel> { Response.success(null) }.exceptionOrNull()

        assertTrue(error is ErrorApiDataModel.ApiNullBody)
    }

    @Test
    fun networkFailureIsNoInternetError() = runTest {
        val error = apiResult<ResultsDataModel> { throw UnknownHostException("randomuser.me") }.exceptionOrNull()

        assertTrue(error is ErrorApiDataModel.NoInternetError)
    }

    @Test
    fun timeoutIsSocketTimeOutError() = runTest {
        val error = apiResult<ResultsDataModel> { throw SocketTimeoutException("timeout") }.exceptionOrNull()

        assertTrue(error is ErrorApiDataModel.SocketTimeOutError)
    }

    @Test
    fun cancellationIsRethrown() = runTest {
        try {
            apiResult<ResultsDataModel> { throw CancellationException("cancelled") }
            fail("CancellationException must not be turned into an API error")
        } catch (_: CancellationException) {
            // expected: cancellation keeps propagating
        }
    }
}
