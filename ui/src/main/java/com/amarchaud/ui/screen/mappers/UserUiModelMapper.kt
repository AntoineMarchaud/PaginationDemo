package com.amarchaud.ui.screen.mappers

import com.amarchaud.domain.models.UserModel
import com.amarchaud.ui.screen.mainList.models.UserGenericUiModel

internal fun UserModel.toGenericUiModel() = UserGenericUiModel(
    localId = this.localId,
    // randomuser.me pictures are 128px max: always take the largest one
    imageUrl = this.picture?.large ?: this.picture?.medium.orEmpty(),
    email = this.email.orEmpty(),
    title = this.name?.title.orEmpty(),
    firstName = this.name?.first.orEmpty(),
    fullName = joinNonBlank(" ", this.name?.first, this.name?.last),
    age = this.dob?.age?.toInt(),
    location = joinNonBlank(", ", this.location?.city, this.location?.country),
    flag = this.nat.toFlagEmoji(),
)

/** "FR" -> 🇫🇷 (two regional indicator symbols), "" when the code is not two letters. */
internal fun String?.toFlagEmoji(): String {
    val code = this?.uppercase() ?: return ""
    if (code.length != 2 || code.any { it !in 'A'..'Z' }) return ""
    return buildString {
        code.forEach { letter ->
            // regional indicators U+1F1E6..U+1F1FF as UTF-16 surrogate pairs
            append('\uD83C')
            append((0xDDE6 + (letter - 'A')).toChar())
        }
    }
}

internal fun joinNonBlank(separator: String, vararg parts: String?): String =
    parts.filterNotNull().filter { it.isNotBlank() }.joinToString(separator)
