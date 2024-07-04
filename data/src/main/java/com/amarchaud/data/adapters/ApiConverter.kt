package com.amarchaud.data.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ApiConverter {
    data object LocalDateTimeConverter {

        private var formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

        @ToJson
        fun toJson(localDate: LocalDateTime): String? {
            return formatter.format(localDate)
        }

        @FromJson
        fun fromJson(json: String): LocalDateTime { // from api to me
            return LocalDateTime.parse(json, formatter)
        }
    }
}
