package com.amarchaud.data.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ResultsDataModel(
    @SerialName("results")
    val users: List<UserDataModel> = emptyList(),
    val info: InfoDataModel? = null,
)

@Serializable
data class UserDataModel(
    val gender: String? = null,
    val name: NameDataModel? = null,
    val location: LocationDataModel? = null,
    val email: String? = null,
    val login: LoginDataModel? = null,
    val dob: DobDataModel? = null,
    val registered: RegisteredDataModel? = null,
    val phone: String? = null,
    val cell: String? = null,
    val id: IdDataModel? = null,
    val picture: PictureDataModel? = null,
    val nat: String? = null,
)

@Serializable
data class NameDataModel(
    val title: String? = null,
    val first: String? = null,
    val last: String? = null,
)

data class ForceString(val string : String)

object OneStringSerializer : KSerializer<ForceString> {
    override val descriptor = PrimitiveSerialDescriptor("ForceString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ForceString) {
        encoder.encodeString(value.string)
    }

    override fun deserialize(decoder: Decoder): ForceString {
        return try {
            ForceString(decoder.decodeString())
        } catch (e: Exception) {
            ForceString(decoder.decodeInt().toString())
        }
    }
}

@Serializable
data class LocationDataModel(
    val street: StreetDataModel? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    @Serializable(with = OneStringSerializer::class)
    val postcode: ForceString? = null, // erreur coté serveur, ça devrait etre string
    val coordinates: CoordinatesDataModel? = null,
    val timezone: TimezoneDataModel? = null,
)

@Serializable
data class StreetDataModel(
    val number: Long? = null,
    val name: String? = null,
)

@Serializable
data class CoordinatesDataModel(
    val latitude: String? = null,
    val longitude: String? = null,
)

@Serializable
data class TimezoneDataModel(
    val offset: String? = null,
    val description: String? = null,
)

@Serializable
data class LoginDataModel(
    val uuid: String? = null,
    val username: String? = null,
    val password: String? = null,
    val salt: String? = null,
    val md5: String? = null,
    val sha1: String? = null,
    val sha256: String? = null,
)

@Serializable
data class DobDataModel(
    val date: String? = null, // todo how to have LocalDateTime ?
    val age: Long? = null,
)

@Serializable
data class RegisteredDataModel(
    val date: String? = null,
    val age: Long? = null,
)

@Serializable
data class IdDataModel(
    val name: String? = null,
    val value: String? = null,
)

@Serializable
data class PictureDataModel(
    val large: String? = null,
    val medium: String? = null,
    val thumbnail: String? = null,
)

// current page
@Serializable
data class InfoDataModel(
    val seed: String? = null,
    val results: Long? = null,
    val page: Int? = null,
    val version: String? = null,
)
