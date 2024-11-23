package com.amarchaud.data.di

import com.amarchaud.domain.repository.PaginationDemoRepository
import com.amarchaud.domain.usecase.GetRandomUsersWithRoomUseCase
import com.amarchaud.domain.usecase.GetUserFromCacheUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetUserFromCacheUseCase> {
        GetUserFromCacheUseCase(
            repository = get<PaginationDemoRepository>()
        )
    }

    factory<GetRandomUsersWithRoomUseCase> {
        GetRandomUsersWithRoomUseCase(
            repository = get<PaginationDemoRepository>()
        )
    }
}