package com.amarchaud.domain.usecase

import com.amarchaud.domain.repository.PaginationDemoRepository
import javax.inject.Inject

class GetRandomUsersUseCase @Inject constructor(
    private val repository: PaginationDemoRepository
) {
    fun run() = repository.getRandomUsers()
}