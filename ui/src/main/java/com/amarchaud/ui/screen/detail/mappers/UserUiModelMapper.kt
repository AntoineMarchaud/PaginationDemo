package com.amarchaud.ui.screen.detail.mappers

import com.amarchaud.domain.models.UserModel
import com.amarchaud.ui.screen.detail.models.UserDetailUiModel
import com.amarchaud.ui.screen.mainList.mappers.toGenericUiModel
import org.osmdroid.util.GeoPoint

internal fun UserModel.toDetailUiModel() = UserDetailUiModel(
    mainInfo = this.toGenericUiModel(),
    mainImageUrl = this.picture?.large.orEmpty(),
    coordinates = GeoPoint(
        this.location?.coordinates?.latitude?.toDouble() ?: 0.0,
        this.location?.coordinates?.longitude?.toDouble() ?: 0.0
    ),
    address = "${this.location?.street?.number} ${this.location?.street?.name} ${this.location?.city}",
    phoneNumber = "${this.phone}",
    birthday = "${this.dob?.date?.toLocalDate()}"
)