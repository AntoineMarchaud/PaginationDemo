package com.amarchaud.data.fakes

import com.amarchaud.data.api.PaginationDemoApi
import com.amarchaud.data.models.ResultsDataModel
import retrofit2.Response

class FakePaginationDemoApi(
    /** Called for each request; may throw to simulate a network failure. */
    var respond: (page: Int) -> Response<ResultsDataModel> = { Response.success(ResultsDataModel()) },
) : PaginationDemoApi {

    val requestedPages = mutableListOf<Int>()

    override suspend fun getRandomUsers(seed: String, results: Int, page: Int): Response<ResultsDataModel> {
        requestedPages += page
        return respond(page)
    }
}
