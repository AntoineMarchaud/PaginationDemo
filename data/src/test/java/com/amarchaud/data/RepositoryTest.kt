package com.amarchaud.data

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.amarchaud.data.api.PaginationDemoApi
import com.amarchaud.data.db.PaginationDemoDao
import com.amarchaud.data.mappers.toDomain
import com.amarchaud.data.models.ResultsDataModel
import com.amarchaud.data.models.UserDataModel
import com.amarchaud.data.repository.PaginationDemoRepositoryImpl
import com.amarchaud.database.UsersEntity
import com.amarchaud.domain.models.ErrorApiModel
import com.amarchaud.domain.models.UserModel
import com.amarchaud.domain.repository.PaginationDemoRepository
import io.ktor.server.plugins.BadRequestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RepositoryTest {

    private val apiMock: PaginationDemoApi = mock()
    private val daoMock: PaginationDemoDao = mock()

    private lateinit var repository: PaginationDemoRepository

    private val mockEntityUser = UsersEntity(
        _id = 0,
        email = "example@gmail.com"
    )

    private val mockApiUser = UserDataModel(
        email = "example@gmail.com"
    )

    private val mockPagingSource = object : PagingSource<Int, UsersEntity>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UsersEntity> {
            // Simulate loading data from a data source
            return LoadResult.Page(
                data = List(3) { mockEntityUser },
                prevKey = null,
                nextKey = null
            )
        }

        override fun getRefreshKey(state: PagingState<Int, UsersEntity>): Int? {
            // Return null because the refresh key is not needed for this mock implementation
            return null
        }
    }

    @Before
    fun setUp() {
        repository = PaginationDemoRepositoryImpl(
            paginationDemoApi = apiMock,
            paginationDemoDao = daoMock
        )
    }

    @Test
    fun `check getRandomUsersRoom and map it to domain`() = runTest {
        whenever(daoMock.getUsersPagingSource()).thenReturn(mockPagingSource)
        whenever(apiMock.getRandomUsers(any(), any(), any())).thenReturn(
            Result.success(
                ResultsDataModel(
                    users = List(3) { mockApiUser }
                )
            )
        )

        val items: Flow<PagingData<UserModel>> = repository.getRandomUsers()
        val snapshot = items.asSnapshot()

        Assert.assertTrue(snapshot.size == 3)
        with(snapshot.last()) {
            Assert.assertTrue(this == mockEntityUser.toDomain())
        }
    }

    @Test
    fun `check getRandomUsersRoom KO`() = runTest {
        whenever(daoMock.getUsersPagingSource()).thenReturn(mockPagingSource)
        whenever(apiMock.getRandomUsers(any(), any(), any())).thenReturn(
            Result.failure(
                BadRequestException("{ \"error\": \"error\" }")
            )
        )

        val items: Flow<PagingData<UserModel>> = repository.getRandomUsers()
        try {
            items.asSnapshot()
        } catch (e: Throwable) {
            Assert.assertTrue(e is ErrorApiModel.ApiServerErrorWithCode)
        }
    }

    @Test
    fun `check getUserFromCache and map it to domain`() = runTest {
        whenever(daoMock.getUserFromCache(localId = 0)).thenReturn(mockEntityUser)

        val res = repository.getOneUser(localId = 0)

        Assert.assertTrue(res == mockEntityUser.toDomain())
    }
}