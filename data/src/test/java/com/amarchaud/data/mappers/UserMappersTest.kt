package com.amarchaud.data.mappers

import com.amarchaud.data.models.DobDataModel
import com.amarchaud.data.models.DobEntityModel
import com.amarchaud.data.models.NameDataModel
import com.amarchaud.data.models.NameEntityModel
import com.amarchaud.data.models.PictureDataModel
import com.amarchaud.data.models.PictureEntityModel
import com.amarchaud.data.models.UserDataModel
import com.amarchaud.data.models.UserEntityModel
import com.amarchaud.domain.models.NameModel
import com.amarchaud.domain.models.PictureModel
import kotlinx.datetime.LocalDateTime
import org.junit.Test
import org.junit.Assert.assertEquals

class UserMappersTest {

    private val birthDate = LocalDateTime(1993, 7, 20, 9, 44, 18)

    private val dataModel = UserDataModel(
        gender = "male",
        name = NameDataModel(title = "Mr", first = "Jo", last = "Doe"),
        email = null,
        dob = DobDataModel(date = birthDate, age = 31),
        phone = "0102030405",
        picture = PictureDataModel(large = "l.jpg", medium = "m.jpg", thumbnail = "t.jpg"),
        nat = "FR",
    )

    @Test
    fun userDataModelToEntityMapsFields() {
        val entity = dataModel.toEntity()

        assertEquals(0L, entity._id)
        assertEquals("", entity.email)
        assertEquals("male", entity.gender)
        assertEquals(NameEntityModel(title = "Mr", first = "Jo", last = "Doe"), entity.name)
        assertEquals(DobEntityModel(date = birthDate, age = 31), entity.dob)
        assertEquals("0102030405", entity.phone)
        assertEquals(PictureEntityModel(large = "l.jpg", medium = "m.jpg", thumbnail = "t.jpg"), entity.picture)
        assertEquals("FR", entity.nat)
    }

    @Test
    fun userEntityModelToDomainKeepsLocalId() {
        val entity = UserEntityModel(
            _id = 7,
            email = "jo@doe.fr",
            name = NameEntityModel(title = "Mr", first = "Jo", last = "Doe"),
            dob = DobEntityModel(date = birthDate, age = 31),
        )

        val domain = entity.toDomain()

        assertEquals(7L, domain.localId)
        assertEquals("jo@doe.fr", domain.email)
        assertEquals(NameModel(title = "Mr", first = "Jo", last = "Doe"), domain.name)
        assertEquals(birthDate.toString(), domain.dob?.date)
        assertEquals(31L, domain.dob?.age)
    }

    @Test
    fun userDataModelToDomainUsesLocalIdMinusOne() {
        val domain = dataModel.toDomain()

        assertEquals(-1L, domain.localId)
        assertEquals(NameModel(title = "Mr", first = "Jo", last = "Doe"), domain.name)
        assertEquals(PictureModel(large = "l.jpg", medium = "m.jpg", thumbnail = "t.jpg"), domain.picture)
    }
}
