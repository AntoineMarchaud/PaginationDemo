package com.amarchaud.domain.usecase

import com.amarchaud.domain.FakePaginationDemoRepository
import com.amarchaud.domain.models.UserModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetOneUserUseCaseTest {

    @Test
    fun returnsUserFromRepository() = runTest {
        val user = UserModel(localId = 42, email = "a@b.c")
        val repository = FakePaginationDemoRepository(users = mapOf(42L to user))

        val result = GetOneUserUseCase(repository).run(42)

        assertEquals(user, result)
        assertEquals(listOf(42L), repository.requestedIds)
    }

    @Test
    fun returnsNullWhenUserIsAbsent() = runTest {
        val repository = FakePaginationDemoRepository()

        assertNull(GetOneUserUseCase(repository).run(7))
    }
}
