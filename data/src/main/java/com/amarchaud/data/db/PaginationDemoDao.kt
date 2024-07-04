package com.amarchaud.data.db

import androidx.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import com.amarchaud.data.models.PageEntityModel
import com.amarchaud.database.PaginationDemoDatabase
import com.amarchaud.database.UsersEntity
import kotlinx.coroutines.Dispatchers

interface PaginationDemoDao {
    fun getUsersPagingSource(): PagingSource<Int, UsersEntity>
    suspend fun getUserFromCache(localId: Long): UsersEntity?
    suspend fun insertOneUser(user: UsersEntity)
    suspend fun insertAll(users: List<UsersEntity>)
    suspend fun clearAll()
    suspend fun getLastPage(): Int?
    suspend fun insertPage(page: PageEntityModel)
    suspend fun clearPage()
}

class PaginationDemoDaoImpl(
    database: PaginationDemoDatabase
) : PaginationDemoDao {

    private val queries = database.paginationDemoDatabaseQueries

    override fun getUsersPagingSource(): PagingSource<Int, UsersEntity> {
        return QueryPagingSource(
            countQuery = queries.countUsers(),
            transacter = queries,
            context = Dispatchers.IO,
            queryProvider = queries::getPagedUsers,
            initialOffset = 0,
        )
    }

    override suspend fun getUserFromCache(localId: Long): UsersEntity? {
        return queries.getUserFromCache(localId).executeAsOneOrNull()
    }

    override suspend fun insertOneUser(user: UsersEntity) {
        queries.insertUser(
            gender = user.gender,
            name_title = user.name_title,
            name_first = user.name_first,
            name_last = user.name_last,
            location_street_number = user.location_street_number,
            location_street_name = user.location_street_name,
            location_city = user.location_city,
            location_state = user.location_state,
            location_country = user.location_country,
            location_postcode = user.location_postcode,
            location_coordinates_latitude = user.location_coordinates_latitude,
            location_coordinates_longitude = user.location_coordinates_longitude,
            location_timezone_offset = user.location_timezone_offset,
            location_timezone_description = user.location_timezone_description,
            email = user.email,
            dob_date = user.dob_date,
            dob_age = user.dob_age,
            registered_date = user.registered_date,
            registered_age = user.registered_age,
            id_name = user.id_name,
            id_value = user.id_value,
            picture_large = user.picture_large,
            picture_medium = user.picture_medium,
            picture_thumbnail = user.picture_thumbnail,
            nat = user.nat,
        )
    }

    override suspend fun insertAll(users: List<UsersEntity>) {
        users.forEach {
            insertOneUser(it)
        }
    }

    override suspend fun clearAll() {
        queries.clearAll()
    }

    override suspend fun getLastPage(): Int? {
        return queries.getLastPage().executeAsOneOrNull()?.MAX?.toInt()
    }

    override suspend fun insertPage(page: PageEntityModel) {
        queries.insertPage(page = page.page.toLong())
    }

    override suspend fun clearPage() {
        queries.clearPage()
    }
}
