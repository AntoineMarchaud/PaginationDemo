package com.amarchaud.domain.usecase

import com.amarchaud.domain.repository.PaginationDemoRepository

class GetOneUserUseCase(
    private val repository: PaginationDemoRepository
) {
    suspend fun run(localId: Long) = repository.getOneUser(localId)
}