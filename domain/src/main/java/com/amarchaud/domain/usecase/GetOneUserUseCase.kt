package com.amarchaud.domain.usecase

import com.amarchaud.domain.repository.PaginationDemoRepository
import javax.inject.Inject

class GetOneUserUseCase @Inject constructor(
    private val repository: PaginationDemoRepository
) {
    suspend fun run(localId: Long) = repository.getOneUser(localId)
}