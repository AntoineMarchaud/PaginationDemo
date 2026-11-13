package com.amarchaud.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import com.amarchaud.data.fakes.FakePaginationDemoApi
import com.amarchaud.data.fakes.FakePaginationDemoDao
import com.amarchaud.data.models.InfoDataModel
import com.amarchaud.data.models.ResultsDataModel
import com.amarchaud.data.models.UserDataModel
import com.amarchaud.data.models.UserEntityModel
import com.amarchaud.domain.models.ErrorApiModel
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import repository.remotemediator.RandomUsersRemoteMediator
import retrofit2.Response
import java.net.UnknownHostException

@OptIn(ExperimentalPagingApi::class)
class RandomUsersRemoteMediatorTest {

    private val dao = FakePaginationDemoDao()
    private val api = FakePaginationDemoApi()
    private val mediator = RandomUsersRemoteMediator(PaginationDemoDao = dao, PaginationDemoApi = api)
    private val state = PagingState<Int, UserEntityModel>(
        pages = emptyList(),
        anchorPosition = null,
        config = PagingConfig(pageSize = 20),
        leadingPlaceholderCount = 0,
    )

    private fun apiUsers(count: Int, page: Int): (Int) -> Response<ResultsDataModel> = {
        Response.success(
            ResultsDataModel(
                users = List(count) { UserDataModel(email = "user$it@test.fr") },
                info = InfoDataModel(page = page),
            )
        )
    }

    @Test
    fun refreshSuccessClearsTablesAndInsertsPageAndUsers() = runTest {
        dao.users += UserEntityModel(_id = 99, email = "old@test.fr")
        dao.pages += 5
        api.respond = apiUsers(count = 2, page = 1)

        val result = mediator.load(LoadType.REFRESH, state)

        assertTrue(result is MediatorResult.Success)
        assertFalse((result as MediatorResult.Success).endOfPaginationReached)
        assertEquals(listOf(1), api.requestedPages)
        assertEquals(listOf("user0@test.fr", "user1@test.fr"), dao.users.map { it.email })
        assertEquals(listOf(1), dao.pages)
    }

    @Test
    fun appendRequestsLastPagePlusOne() = runTest {
        dao.pages += 3
        api.respond = apiUsers(count = 1, page = 4)

        val result = mediator.load(LoadType.APPEND, state)

        assertFalse((result as MediatorResult.Success).endOfPaginationReached)
        assertEquals(listOf(4), api.requestedPages)
        assertEquals(listOf(3, 4), dao.pages)
    }

    @Test
    fun appendWithoutStoredPageEndsPagination() = runTest {
        val result = mediator.load(LoadType.APPEND, state)

        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
        assertTrue(api.requestedPages.isEmpty())
    }

    @Test
    fun prependEndsPagination() = runTest {
        val result = mediator.load(LoadType.PREPEND, state)

        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
        assertTrue(api.requestedPages.isEmpty())
    }

    @Test
    fun emptyResponseEndsPagination() = runTest {
        api.respond = apiUsers(count = 0, page = 1)

        val result = mediator.load(LoadType.REFRESH, state)

        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun httpErrorIsMappedToDomainError() = runTest {
        api.respond = { Response.error(503, "".toResponseBody()) }

        val result = mediator.load(LoadType.REFRESH, state)

        assertTrue(result is MediatorResult.Error)
        val error = (result as MediatorResult.Error).throwable
        assertTrue(error is ErrorApiModel.ApiServerErrorWithCode)
        assertEquals(503, (error as ErrorApiModel.ApiServerErrorWithCode).responseCode)
    }

    @Test
    fun networkFailureIsNoInternetError() = runTest {
        api.respond = { throw UnknownHostException("randomuser.me") }

        val result = mediator.load(LoadType.REFRESH, state)

        assertTrue((result as MediatorResult.Error).throwable is ErrorApiModel.NoInternetError)
    }

    @Test
    fun refreshFailureKeepsCachedUsers() = runTest {
        dao.users += List(3) { UserEntityModel(_id = it.toLong(), email = "cached$it@test.fr") }
        dao.pages += 1
        api.respond = { throw UnknownHostException("offline") }

        mediator.load(LoadType.REFRESH, state)

        assertEquals(3, dao.users.size)
        assertEquals(listOf(1), dao.pages)
    }
}
