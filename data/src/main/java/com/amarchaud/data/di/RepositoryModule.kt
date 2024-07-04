package com.amarchaud.data.di

import com.amarchaud.data.repository.PaginationDemoRepositoryImpl
import com.amarchaud.domain.repository.PaginationDemoRepository
import org.koin.dsl.module

val repoModule = module {
    single<PaginationDemoRepository> {
        PaginationDemoRepositoryImpl(
            paginationDemoDao = get(),
            paginationDemoApi = get()
        )
    }
}