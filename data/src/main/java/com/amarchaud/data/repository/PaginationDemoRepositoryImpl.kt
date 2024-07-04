package com.amarchaud.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.amarchaud.data.api.PaginationDemoApi
import com.amarchaud.data.db.PaginationDemoDao
import com.amarchaud.data.mappers.toDomain
import com.amarchaud.data.repository.remotemediator.RandomUsersRemoteMediator
import com.amarchaud.domain.models.UserModel
import com.amarchaud.domain.repository.PaginationDemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PaginationDemoRepositoryImpl(
    private val paginationDemoDao: PaginationDemoDao,
    private val paginationDemoApi: PaginationDemoApi
) : PaginationDemoRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getRandomUsers(): Flow<PagingData<UserModel>> = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        remoteMediator = RandomUsersRemoteMediator(
            paginationDemoDao = paginationDemoDao,
            paginationDemoApi = paginationDemoApi
        ),
        pagingSourceFactory = {
            paginationDemoDao.getUsersPagingSource()
        }
    ).flow.map {
        it.map { oneUserEntity -> oneUserEntity.toDomain() }
    }

    override suspend fun getOneUser(localId: Long) =
        paginationDemoDao.getUserFromCache(localId)?.toDomain()
}