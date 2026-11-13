package com.amarchaud.ui.screen.mappers

import com.amarchaud.ui.R
import androidx.compose.runtime.Composable
import com.amarchaud.domain.models.ErrorApiModel
import com.amarchaud.ui.screen.mainList.models.ErrorApiUiModel
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource

/** A localized message: the string resource and its format arguments. */
internal data class ErrorMessage(
    @param:StringRes val resource: Int,
    val args: List<Any> = emptyList(),
)

/** Errors coming from Paging's `LoadState.Error` are plain throwables. */
internal fun Throwable.toErrorUiModel(): ErrorApiUiModel =
    (this as? ErrorApiModel)?.toErrorUiModel() ?: ErrorApiUiModel.GenericError

internal fun ErrorApiUiModel.toErrorMessage(): ErrorMessage = when (this) {
    is ErrorApiUiModel.ApiServerErrorWithCode -> ErrorMessage(R.string.error_ApiServerErrorWithCode, listOf(responseCode))
    ErrorApiUiModel.ApiNullBody -> ErrorMessage(R.string.error_ApiNullBody)
    ErrorApiUiModel.ApiGenericServerError -> ErrorMessage(R.string.error_ApiGenericServerError)
    ErrorApiUiModel.SocketTimeOutError -> ErrorMessage(R.string.error_SocketTimeOutError)
    ErrorApiUiModel.NoInternetError -> ErrorMessage(R.string.error_NoInternetError)
    ErrorApiUiModel.GenericError -> ErrorMessage(R.string.error_GenericError)
}

@Composable
internal fun Throwable.errorMessage(): String {
    val message = toErrorUiModel().toErrorMessage()
    return stringResource(message.resource, *message.args.toTypedArray())
}
