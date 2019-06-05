package com.example.androidx_example.data

import androidx.paging.PageKeyedDataSource
import com.example.androidx_example.until.api.Pagination
import com.example.androidx_example.until.debugInfo
import com.example.androidx_example.until.getSuccess


class VideoDataSource : PageKeyedDataSource<Pagination, Video>() {

    override fun loadInitial(params: LoadInitialParams<Pagination>, callback: LoadInitialCallback<Pagination, Video>) {
        val page = Pagination.create(params.requestedLoadSize)
        getSuccess(
            apiName = "list",
            params = page.pageParams,
            successDo = { res ->
                val pageData = res.getPageData(Video::class.java)
                page.updateTotal(pageData.total)
                callback.onResult(pageData.rows, page, page)
            }
        )
    }

    override fun loadBefore(params: LoadParams<Pagination>, callback: LoadCallback<Pagination, Video>) {
        val page = params.key
        if (page.hasPrev) {
            page.prevPage()
            getSuccess(
                apiName = "list",
                params = page.pageParams,
                successDo = { res ->
                    val pageData = res.getPageData(Video::class.java)
                    page.updateTotal(pageData.total)
                    callback.onResult(pageData.rows, page)
                }
            )
        }
    }

    override fun loadAfter(params: LoadParams<Pagination>, callback: LoadCallback<Pagination, Video>) {
        val page = params.key
        if (page.hasNext) {
            page.nextPage()
            getSuccess(
                apiName = "list",
                params = page.pageParams,
                successDo = { res ->
                    val pageData = res.getPageData(Video::class.java)
                    page.updateTotal(pageData.total)
                    callback.onResult(pageData.rows, page)
                }
            )
        }
    }
}