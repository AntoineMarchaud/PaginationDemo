package com.amarchaud.data.fakes

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amarchaud.data.db.PaginationDemoDao
import com.amarchaud.data.models.PageEntityModel
import com.amarchaud.data.models.UserEntityModel

class FakePaginationDemoDao : PaginationDemoDao {

    val users = mutableListOf<UserEntityModel>()
    val pages = mutableListOf<Int>()

    override fun getUsersPagingSource(): PagingSource<Int, UserEntityModel> =
        object : PagingSource<Int, UserEntityModel>() {
            override fun getRefreshKey(state: PagingState<Int, UserEntityModel>): Int? = null

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntityModel> =
                LoadResult.Page(data = users.toList(), prevKey = null, nextKey = null)
        }

    override suspend fun getUserFromCache(localId: Long): UserEntityModel? =
        users.firstOrNull { it._id == localId }

    override suspend fun insertAll(users: List<UserEntityModel>) {
        this.users += users
    }

    override suspend fun clearAll() {
        users.clear()
    }

    override suspend fun getLastPage(): Int? = pages.maxOrNull()

    override suspend fun insertPage(page: PageEntityModel) {
        pages += page.page
    }

    override suspend fun clearPage() {
        pages.clear()
    }
}
