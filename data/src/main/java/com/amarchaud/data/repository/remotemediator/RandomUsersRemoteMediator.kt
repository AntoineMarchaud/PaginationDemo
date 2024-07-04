package com.amarchaud.data.repository.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.amarchaud.data.api.PaginationDemoApi
import com.amarchaud.data.db.PaginationDemoDao
import com.amarchaud.data.mappers.toDomain
import com.amarchaud.data.mappers.toEntity
import com.amarchaud.data.models.ErrorApiDataModel
import com.amarchaud.data.models.PageEntityModel
import com.amarchaud.database.UsersEntity
import com.amarchaud.domain.models.ErrorApiModel
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalPagingApi::class)
class RandomUsersRemoteMediator(
    private val paginationDemoDao: PaginationDemoDao,
    private val paginationDemoApi: PaginationDemoApi
) : RemoteMediator<Int, UsersEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UsersEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                paginationDemoDao.getLastPage()?.plus(1) ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }
        }

        runCatching {
            delay(300)
            paginationDemoApi.getRandomUsers(page = page).getOrThrow()
        }.fold(
            onSuccess = {
                if (it.users.isEmpty()) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                if (loadType == LoadType.REFRESH) {
                    paginationDemoDao.clearAll()
                    paginationDemoDao.clearPage()
                }

                // insert users in DB
                it.users.let { allUsersDataModel ->
                    paginationDemoDao.insertPage(page = PageEntityModel(it.info?.page ?: 1))
                    paginationDemoDao.insertAll(
                        allUsersDataModel.map { oneUserDataModel ->
                            oneUserDataModel.toEntity()
                        }
                    )
                }

                return MediatorResult.Success(endOfPaginationReached = false)
            },
            onFailure = {
                return when (it) {
                    is ErrorApiDataModel -> MediatorResult.Error(it.toDomain())
                    is CancellationException -> MediatorResult.Success(endOfPaginationReached = false) // special case when canceling current coroutine
                    else -> MediatorResult.Error(ErrorApiModel.GenericError())
                }
            }
        )
    }
}