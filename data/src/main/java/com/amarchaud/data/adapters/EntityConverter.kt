package com.amarchaud.data.adapters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object RoomConverter {

    data object LocalDateRoomConverter {

        private var formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

        @TypeConverter
        fun localDateToString(localDate: LocalDateTime): String? { // call when insert data in db
            return localDate.let { formatter.format(it) }
        }

        @TypeConverter
        fun stringToLocalDate(str: String?): LocalDateTime? { // call when loading db
            return str?.let { LocalDateTime.parse(it, formatter) }
        }
    }
}
