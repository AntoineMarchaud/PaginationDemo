package com.amarchaud.data.adapters


import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        // Sérialiser LocalDateTime au format ISO 8601 avec "Z"
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val value = decoder.decodeString()
        // Convertir une chaîne ISO 8601 avec "Z" en LocalDateTime
        return try {
            Instant.parse(value).toLocalDateTime(TimeZone.UTC)
        } catch (e: Exception) {
            Clock.System.now().toLocalDateTime(TimeZone.UTC)
        }
    }
}

object PostcodeSerializer : KSerializer<Any> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Postcode", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is Int -> encoder.encodeString(value.toString())
            is String -> encoder.encodeString(value)
            else -> throw IllegalArgumentException("Unsupported type for postcode")
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        val value = decoder.decodeString()
        return value.toIntOrNull() ?: value
    }
}
