package com.amarchaud.data.api

import com.amarchaud.data.models.ResultsDataModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface PaginationDemoApi {
    suspend fun getRandomUsers(
        seed: String = "PaginationDemo",
        results: Int = 20,
        page: Int
    ): Result<ResultsDataModel>
}

// version KTOR
class PaginationDemoApiKtorImpl(private val client: HttpClient) : PaginationDemoApi {
    override suspend fun getRandomUsers(
        seed: String,
        results: Int,
        page: Int
    ): Result<ResultsDataModel> = runCatching {
        client.get("/api/1.3/") {
            parameter("seed", seed)
            parameter("results", results)
            parameter("page", page)
        }.body()
    }
}