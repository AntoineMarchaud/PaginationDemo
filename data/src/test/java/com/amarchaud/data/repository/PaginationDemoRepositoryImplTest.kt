package com.amarchaud.data.repository

import androidx.paging.testing.asSnapshot
import com.amarchaud.data.fakes.FakePaginationDemoApi
import com.amarchaud.data.fakes.FakePaginationDemoDao
import com.amarchaud.data.models.UserEntityModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class PaginationDemoRepositoryImplTest {

    private val dao = FakePaginationDemoDao()
    private val api = FakePaginationDemoApi()
    private val repository = PaginationDemoRepositoryImpl(paginationDemoApi = api, paginationDemoDao = dao)

    @Test
    fun getOneUserMapsEntity() = runTest {
        dao.users += UserEntityModel(_id = 42, email = "jo@doe.fr")

        val user = repository.getOneUser(42)

        assertNotNull(user)
        assertEquals(42L, user!!.localId)
        assertEquals("jo@doe.fr", user.email)
    }

    @Test
    fun getOneUserReturnsNullWhenAbsent() = runTest {
        assertNull(repository.getOneUser(42))
    }

    @Test
    fun getRandomUsersEmitsMappedUsers() = runTest {
        // the API answers with no user: the mediator ends pagination without clearing the cache
        dao.users += List(2) { UserEntityModel(_id = it + 1L, email = "user$it@test.fr") }

        val snapshot = repository.getRandomUsers().asSnapshot()

        assertEquals(listOf(1L, 2L), snapshot.map { it.localId })
        assertEquals(listOf("user0@test.fr", "user1@test.fr"), snapshot.map { it.email })
    }
}
