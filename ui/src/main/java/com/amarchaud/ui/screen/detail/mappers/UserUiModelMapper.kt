package com.amarchaud.ui.screen.detail.mappers

import com.amarchaud.domain.models.UserModel
import com.amarchaud.ui.screen.detail.models.UserDetailUiModel
import com.amarchaud.ui.screen.mappers.joinNonBlank
import com.amarchaud.ui.screen.mappers.toGenericUiModel
import kotlinx.datetime.LocalDateTime

internal fun UserModel.toDetailUiModel(): UserDetailUiModel {
    val latitude = this.location?.coordinates?.latitude?.toDoubleOrNull()
    val longitude = this.location?.coordinates?.longitude?.toDoubleOrNull()
    val hasCoordinates = latitude != null && longitude != null
    val phoneDigits = this.phone.orEmpty().filter { it.isDigit() || it == '+' }
    // randomuser.me coordinates, streets and states are random (a street of Lviv in the Kyiv region...):
    // only the city and the country are real, anything else makes maps find nothing or the wrong city
    val mapsQuery = joinNonBlank(", ", this.location?.city, this.location?.country)

    return UserDetailUiModel(
        mainInfo = this.toGenericUiModel(),
        mainImageUrl = this.picture?.large ?: this.picture?.medium.orEmpty(),
        coordinates = if (hasCoordinates) latitude to longitude else 0.0 to 0.0,
        address = joinNonBlank(
            " ",
            this.location?.street?.number?.toString(),
            this.location?.street?.name,
            this.location?.city,
        ),
        phoneNumber = this.phone.orEmpty(),
        birthday = this.dob?.date?.toUiDate().orEmpty(),
        emailUri = this.email?.takeIf { it.isNotBlank() }?.let { "mailto:$it" }.orEmpty(),
        phoneUri = if (phoneDigits.isNotEmpty()) "tel:$phoneDigits" else "",
        mapsUri = if (mapsQuery.isNotEmpty()) "https://www.google.com/maps/search/?api=1&query=${mapsQuery.encodeUrlQuery()}" else "",
    )
}

/** Percent-encodes (UTF-8) everything but the URL unreserved characters: `A-Z a-z 0-9 - . _ ~`. */
private fun String.encodeUrlQuery(): String = buildString {
    encodeToByteArray().forEach { byte ->
        val code = byte.toInt() and 0xFF
        val char = code.toChar()
        if (char in 'A'..'Z' || char in 'a'..'z' || char in '0'..'9' || char in "-._~") {
            append(char)
        } else {
            append('%').append(HEX_DIGITS[code shr 4]).append(HEX_DIGITS[code and 0xF])
        }
    }
}

private const val HEX_DIGITS = "0123456789ABCDEF"

/**
 * "1993-04-23T23:34:12.674" -> "April 23, 1993".
 * A birth date is a calendar date: no time zone conversion, otherwise the day could shift.
 */
private fun String.toUiDate(): String = runCatching {
    val date = LocalDateTime.parse(this).date
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    "$month ${date.day}, ${date.year}"
}.getOrDefault("")
