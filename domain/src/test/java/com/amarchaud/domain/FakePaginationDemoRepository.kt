package com.amarchaud.domain

import androidx.paging.PagingData
import com.amarchaud.domain.models.UserModel
import com.amarchaud.domain.repository.PaginationDemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePaginationDemoRepository(
    var pagingData: PagingData<UserModel> = PagingData.empty(),
    var users: Map<Long, UserModel> = emptyMap(),
) : PaginationDemoRepository {

    var getRandomUsersCalls = 0
        private set

    val requestedIds = mutableListOf<Long>()

    override fun getRandomUsers(): Flow<PagingData<UserModel>> {
        getRandomUsersCalls++
        return flowOf(pagingData)
    }

    override suspend fun getOneUser(localId: Long): UserModel? {
        requestedIds += localId
        return users[localId]
    }
}
