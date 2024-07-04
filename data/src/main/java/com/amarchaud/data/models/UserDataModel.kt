package com.amarchaud.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ResultsDataModel(
    @field:Json(name = "results")
    val users: List<UserDataModel> = emptyList(),
    @field:Json(name = "info")
    val info: InfoDataModel? = null,
)

@JsonClass(generateAdapter = true)
data class UserDataModel(
    @field:Json(name = "gender")
    val gender: String? = null,
    @field:Json(name = "name")
    val name: NameDataModel? = null,
    @field:Json(name = "location")
    val location: LocationDataModel? = null,
    @field:Json(name = "email")
    val email: String? = null,
    @field:Json(name = "login")
    val login: LoginDataModel? = null,
    @field:Json(name = "dob")
    val dob: DobDataModel? = null,
    @field:Json(name = "registered")
    val registered: RegisteredDataModel? = null,
    @field:Json(name = "phone")
    val phone: String? = null,
    @field:Json(name = "cell")
    val cell: String? = null,
    @field:Json(name = "id")
    val id: IdDataModel? = null,
    @field:Json(name = "picture")
    val picture: PictureDataModel? = null,
    @field:Json(name = "nat")
    val nat: String? = null,
)

@JsonClass(generateAdapter = true)
data class NameDataModel(
    @field:Json(name = "title")
    val title: String? = null,
    @field:Json(name = "first")
    val first: String? = null,
    @field:Json(name = "last")
    val last: String? = null,
)

@JsonClass(generateAdapter = true)
data class LocationDataModel(
    @field:Json(name = "street")
    val street: StreetDataModel? = null,
    @field:Json(name = "city")
    val city: String? = null,
    @field:Json(name = "state")
    val state: String? = null,
    @field:Json(name = "country")
    val country: String? = null,
    @field:Json(name = "postcode")
    val postcode: String? = null,
    @field:Json(name = "coordinates")
    val coordinates: CoordinatesDataModel? = null,
    @field:Json(name = "timezone")
    val timezone: TimezoneDataModel? = null,
)

@JsonClass(generateAdapter = true)
data class StreetDataModel(
    @field:Json(name = "number")
    val number: Long? = null,
    @field:Json(name = "name")
    val name: String? = null,
)

@JsonClass(generateAdapter = true)
data class CoordinatesDataModel(
    @field:Json(name = "latitude")
    val latitude: String? = null,
    @field:Json(name = "longitude")
    val longitude: String? = null,
)

@JsonClass(generateAdapter = true)
data class TimezoneDataModel(
    @field:Json(name = "offset")
    val offset: String? = null,
    @field:Json(name = "description")
    val description: String? = null,
)

@JsonClass(generateAdapter = true)
data class LoginDataModel(
    @field:Json(name = "uuid")
    val uuid: String? = null,
    @field:Json(name = "username")
    val username: String? = null,
    @field:Json(name = "password")
    val password: String? = null,
    @field:Json(name = "salt")
    val salt: String? = null,
    @field:Json(name = "md5")
    val md5: String? = null,
    @field:Json(name = "sha1")
    val sha1: String? = null,
    @field:Json(name = "sha256")
    val sha256: String? = null,
)

@JsonClass(generateAdapter = true)
data class DobDataModel(
    @field:Json(name = "date")
    val date: LocalDateTime? = null,
    @field:Json(name = "age")
    val age: Long? = null,
)

@JsonClass(generateAdapter = true)
data class RegisteredDataModel(
    @field:Json(name = "date")
    val date: LocalDateTime? = null,
    @field:Json(name = "age")
    val age: Long? = null,
)

@JsonClass(generateAdapter = true)
data class IdDataModel(
    @field:Json(name = "name")
    val name: String? = null,
    @field:Json(name = "value")
    val value: String? = null,
)

@JsonClass(generateAdapter = true)
data class PictureDataModel(
    @field:Json(name = "large")
    val large: String? = null,
    @field:Json(name = "medium")
    val medium: String? = null,
    @field:Json(name = "thumbnail")
    val thumbnail: String? = null,
)

// current page
@JsonClass(generateAdapter = true)
data class InfoDataModel(
    @field:Json(name = "seed")
    val seed: String? = null,
    @field:Json(name = "results")
    val results: Long? = null,
    @field:Json(name = "page")
    val page: Int? = null,
    @field:Json(name = "version")
    val version: String? = null,
)
