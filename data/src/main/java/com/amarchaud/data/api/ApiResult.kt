package com.amarchaud.data.api

import com.amarchaud.data.models.ErrorApiDataModel
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Runs the Retrofit [call] and turns its outcome into a typed [Result].
 * The call is executed inside the try block so that network failures
 * (no connection, timeout) are mapped too, not only HTTP errors.
 */
suspend inline fun <reified T> apiResult(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call()
        when (response.isSuccessful) {
            true -> {
                if ((response.code() == 200 || response.code() == 201) && response.body() != null) {
                    Result.success(response.body()!!)
                } else if (response.code() == 204) {
                    when (T::class) {
                        Unit::class -> Result.success(Unit as T)
                        Boolean::class -> Result.success(true as T)
                        else -> Result.failure(ErrorApiDataModel.ApiNullBody())
                    }
                } else {
                    Result.failure(ErrorApiDataModel.ApiNullBody())
                }
            }

            false -> {
                Result.failure(
                    ErrorApiDataModel.ApiServerErrorWithCode(
                        responseCode = response.code(),
                        responseMessage = response.message()
                    )
                )
            }
        }
    } catch (e: CancellationException) {
        // never swallow coroutine cancellation
        throw e
    } catch (e: Throwable) {
        Result.failure(
            when (e) {
                // timeouts are IOExceptions too: check them first
                is SocketTimeoutException -> ErrorApiDataModel.SocketTimeOutError()
                is IOException -> ErrorApiDataModel.NoInternetError()
                else -> ErrorApiDataModel.ApiGenericServerError()
            }
        )
    }
}
