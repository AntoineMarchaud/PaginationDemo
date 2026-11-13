package com.amarchaud.ui.screen.mainList.models

sealed class ErrorApiUiModel {
    class ApiServerErrorWithCode(val responseCode: Int, val responseMessage: String) :
        ErrorApiUiModel()

    data object ApiNullBody : ErrorApiUiModel()
    data object ApiGenericServerError : ErrorApiUiModel()
    data object SocketTimeOutError : ErrorApiUiModel()
    data object NoInternetError : ErrorApiUiModel()
    data object GenericError : ErrorApiUiModel()
}
