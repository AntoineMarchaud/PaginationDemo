package com.amarchaud.data.di

import com.amarchaud.domain.repository.PaginationDemoRepository
import com.amarchaud.domain.usecase.GetRandomUsersUseCase
import com.amarchaud.domain.usecase.GetOneUserUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetRandomUsersUseCase> {
        GetRandomUsersUseCase(
            repository = get<PaginationDemoRepository>()
        )
    }

    factory<GetOneUserUseCase> {
        GetOneUserUseCase(
            repository = get<PaginationDemoRepository>()
        )
    }
}