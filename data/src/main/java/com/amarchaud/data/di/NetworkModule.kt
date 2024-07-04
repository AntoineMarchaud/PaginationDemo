package com.amarchaud.data.di

import com.amarchaud.data.api.PaginationDemoApi
import com.amarchaud.data.api.PaginationDemoApiKtorImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    // Provide the Ktor HttpClient
    single<HttpClient> {
        HttpClient(Android) {
            install(DefaultRequest) {
                url("https://randomuser.me/")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                json(Json)
            }
        }
    }

    // Provide the API implementation
    single<PaginationDemoApi> {
        PaginationDemoApiKtorImpl(get())
    }
}
