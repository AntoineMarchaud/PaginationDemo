package com.amarchaud.ui.screen.detail.models

import com.amarchaud.ui.screen.mainList.models.UserGenericUiModel

data class UserDetailUiModel(
    // same as the list item
    val mainInfo: UserGenericUiModel = UserGenericUiModel(),
    val mainImageUrl: String = "",
    val coordinates: Pair<Double, Double> = Pair(0.0, 0.0),
    val address: String = "",
    val phoneNumber: String = "",
    val birthday: String = "",
    // ready-to-open URIs, empty when the information is missing
    val emailUri: String = "",
    val phoneUri: String = "",
    val mapsUri: String = "",
)
