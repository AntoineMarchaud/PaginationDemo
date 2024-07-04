package com.amarchaud.domain.usecase

import com.amarchaud.domain.repository.PaginationDemoRepository

class GetRandomUsersUseCase(
    private val repository: PaginationDemoRepository
) {
    fun run() = repository.getRandomUsers()
}