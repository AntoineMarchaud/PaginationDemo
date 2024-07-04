package com.amarchaud.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.amarchaud.data.db.PaginationDemoDao
import com.amarchaud.data.db.PaginationDemoDaoImpl
import com.amarchaud.database.PaginationDemoDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val daoModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = PaginationDemoDatabase.Schema,
            context = androidContext(),
            name = "PaginationDemoDatabase.db"
        )
    }
    single {
        PaginationDemoDatabase(
            driver = get()
        )
    }
    single<PaginationDemoDao> {
        PaginationDemoDaoImpl(
            database = get()
        )
    }
}