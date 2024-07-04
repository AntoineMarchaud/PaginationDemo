package com.amarchaud.data.mappers

import com.amarchaud.data.models.CoordinatesDataModel
import com.amarchaud.data.models.DobDataModel
import com.amarchaud.data.models.IdDataModel
import com.amarchaud.data.models.LocationDataModel
import com.amarchaud.data.models.LoginDataModel
import com.amarchaud.data.models.NameDataModel
import com.amarchaud.data.models.PictureDataModel
import com.amarchaud.data.models.RegisteredDataModel
import com.amarchaud.data.models.StreetDataModel
import com.amarchaud.data.models.TimezoneDataModel
import com.amarchaud.data.models.UserDataModel
import com.amarchaud.database.UsersEntity
import com.amarchaud.domain.models.CoordinatesModel
import com.amarchaud.domain.models.DobModel
import com.amarchaud.domain.models.IdModel
import com.amarchaud.domain.models.LocationModel
import com.amarchaud.domain.models.LoginModel
import com.amarchaud.domain.models.NameModel
import com.amarchaud.domain.models.PictureModel
import com.amarchaud.domain.models.RegisteredModel
import com.amarchaud.domain.models.StreetModel
import com.amarchaud.domain.models.TimezoneModel
import com.amarchaud.domain.models.UserModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// DataModel to domain

internal fun UserDataModel.toDomain() = UserModel(
    localId = -1, // only when passing from entity to domain
    gender = this.gender,
    name = this.name?.toDomain(),
    location = this.location?.toDomain(),
    email = this.email,
    login = this.login?.toDomain(),
    dob = this.dob?.toDomain(),
    registered = this.registered?.toDomain(),
    phone = this.phone,
    cell = this.cell,
    id = this.id?.toDomain(),
    picture = this.picture?.toDomain(),
    nat = this.nat,
)

internal fun NameDataModel.toDomain() = NameModel(
    title = this.title,
    first = this.first,
    last = this.last
)

internal fun LocationDataModel.toDomain() = LocationModel(
    street = this.street?.toDomain(),
    city = this.city,
    state = this.state,
    country = this.country,
    //postcode = this.postcode.toString(),
    coordinates = this.coordinates?.toDomain(),
    timezone = this.timezone?.toDomain(),
)

internal fun StreetDataModel.toDomain() = StreetModel(
    number = this.number,
    name = this.name,
)

internal fun CoordinatesDataModel.toDomain() = CoordinatesModel(
    latitude = this.latitude,
    longitude = this.longitude,
)

internal fun TimezoneDataModel.toDomain() = TimezoneModel(
    offset = this.offset,
    description = this.description,
)

internal fun LoginDataModel.toDomain() = LoginModel(
    uuid = this.uuid,
    username = this.username,
    password = this.password,
    salt = this.salt,
    md5 = this.md5,
    sha1 = this.sha1,
    sha256 = this.sha256,
)

internal fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    return LocalDateTime.parse(this, formatter)
}

internal fun DobDataModel.toDomain() = DobModel(
    date = this.date?.toString(),
    age = this.age,
)

internal fun RegisteredDataModel.toDomain() = RegisteredModel(
    date = this.date?.toString(),
    age = this.age,
)

internal fun IdDataModel.toDomain() = IdModel(
    name = this.name,
    value = this.value
)

internal fun PictureDataModel.toDomain() = PictureModel(
    large = this.large,
    medium = this.medium,
    thumbnail = this.thumbnail,
)

// DataModel (api) to EntityModel

internal fun UserDataModel.toEntity() = UsersEntity(
    gender = this.gender,
    name_title = this.name?.title,
    name_first = this.name?.first,
    name_last = this.name?.last,
    location_street_name = this.location?.street?.name,
    location_street_number = this.location?.street?.number,
    location_city = this.location?.city,
    location_state = this.location?.state,
    location_country = this.location?.country,
    location_postcode = this.location?.postcode.toString(),
    location_coordinates_longitude = this.location?.coordinates?.longitude,
    location_coordinates_latitude = this.location?.coordinates?.latitude,
    location_timezone_offset = this.location?.timezone?.offset,
    location_timezone_description = this.location?.timezone?.description,
    email = this.email.orEmpty(),
    dob_date = this.dob?.date?.toString(),
    dob_age = this.dob?.age,
    registered_age = this.registered?.age,
    registered_date = this.registered?.date?.toString(),
    id_value = this.id?.value,
    id_name = this.id?.name,
    picture_thumbnail = this.picture?.thumbnail,
    picture_medium = this.picture?.medium,
    picture_large = this.picture?.large,
    nat = this.nat,
    _id = 0
)
