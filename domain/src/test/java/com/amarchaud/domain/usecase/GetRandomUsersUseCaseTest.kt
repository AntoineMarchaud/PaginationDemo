package com.amarchaud.domain.usecase

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.amarchaud.domain.FakePaginationDemoRepository
import com.amarchaud.domain.models.ErrorApiModel
import com.amarchaud.domain.models.UserModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class GetRandomUsersUseCaseTest {

    private val users = List(3) { UserModel(localId = it.toLong(), email = "user$it@test.fr") }

    @Test
    fun returnsRepositoryPagingData() = runTest {
        val repository = FakePaginationDemoRepository(pagingData = PagingData.from(users))

        val snapshot = GetRandomUsersUseCase(repository).run().asSnapshot()

        assertEquals(users, snapshot)
        assertEquals(1, repository.getRandomUsersCalls)
    }

    @Test
    fun passesRefreshErrorStateThrough() = runTest {
        val pagingData = PagingData.from(
            data = users,
            sourceLoadStates = LoadStates(
                refresh = LoadState.Error(ErrorApiModel.GenericError()),
                prepend = LoadState.NotLoading(endOfPaginationReached = false),
                append = LoadState.NotLoading(endOfPaginationReached = false),
            ),
        )
        val repository = FakePaginationDemoRepository(pagingData = pagingData)

        // the use case must not swallow nor rewrap the paging data carrying the error state
        assertSame(pagingData, GetRandomUsersUseCase(repository).run().first())
    }
}
