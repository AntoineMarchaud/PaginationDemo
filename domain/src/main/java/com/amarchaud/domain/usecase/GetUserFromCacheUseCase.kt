package com.amarchaud.domain.usecase

import com.amarchaud.domain.repository.PaginationDemoRepository

class GetUserFromCacheUseCase (
    private val repository: PaginationDemoRepository
) {
    suspend fun run(localId: Long) = repository.getUserFromCache(localId)
}