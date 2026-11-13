package com.amarchaud.ui.screen.mainList.models

data class UserGenericUiModel(
    val localId: Long = -1,
    val imageUrl: String = "",
    val email: String = "",
    val title: String = "",
    val firstName: String = "",
    /** First and last name, without title. */
    val fullName: String = "",
    val age: Int? = null,
    /** "City, Country" */
    val location: String = "",
    /** Country flag emoji built from the nationality code, empty if unknown. */
    val flag: String = "",
)
