package com.amarchaud.data.adapters

import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class RoomConverterTest {

    private val converter = RoomConverter.LocalDateRoomConverter

    @Test
    fun nullDateRoundTrip() {
        assertNull(converter.fromLocalDateTime(null))
        assertNull(converter.toLocalDateTime(null))
    }

    @Test
    fun invalidStoredDateDoesNotThrow() {
        // "nullZ" is what the previous version stored for a missing date
        assertNotNull(converter.toLocalDateTime("nullZ"))
    }

    @Test
    fun dateRoundTrip() {
        val date = LocalDateTime(1993, 7, 20, 9, 44, 18)

        assertEquals(date, converter.toLocalDateTime(converter.fromLocalDateTime(date)))
    }
}
