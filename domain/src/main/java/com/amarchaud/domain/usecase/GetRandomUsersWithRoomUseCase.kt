package com.amarchaud.domain.usecase

import com.amarchaud.domain.repository.PaginationDemoRepository

class GetRandomUsersWithRoomUseCase(
    private val repository: PaginationDemoRepository
) {
    fun run() = repository.getRandomUsersRoom()
}