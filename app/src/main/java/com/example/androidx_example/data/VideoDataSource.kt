package com.example.androidx_example.data

import androidx.paging.PageKeyedDataSource
import com.example.androidx_example.until.debugInfo
import com.example.androidx_example.until.getSuccess
import com.example.androidx_example.until.getWithSaveSuccess

class VideoDataSource : PageKeyedDataSource<Pagination, Video>() {

    override fun loadInitial(params: LoadInitialParams<Pagination>, callback: LoadInitialCallback<Pagination, Video>) {
        val page = Pagination.create(params.requestedLoadSize)
        debugInfo("加载数据量" + params.requestedLoadSize)
        getWithSaveSuccess(
            apiName = "videos",
            params = page.pageParams,
            successDo = { res ->
                val pageData = res.getPageData(Video::class.java)
                page.updateTotal(pageData.total)
                callback.onResult(pageData.rows, page, page)
            },
            completeDo = { isOk ->
                if (!isOk) page.resetPagination()
            }
        )
    }

    override fun loadBefore(params: LoadParams<Pagination>, callback: LoadCallback<Pagination, Video>) {
        val page = params.key
        if (page.hasPrev) {
            page.prevPage()
            getWithSaveSuccess(
                apiName = "videos",
                params = page.pageParams,
                successDo = { res ->
                    val pageData = res.getPageData(Video::class.java)
                    page.updateTotal(pageData.total)
                    callback.onResult(pageData.rows, page)
                },
                completeDo = { isOk ->
                    if (!isOk) page.nextPage()
                }
            )
        }
    }

    override fun loadAfter(params: LoadParams<Pagination>, callback: LoadCallback<Pagination, Video>) {
        val page = params.key
        if (page.hasNext) {
            page.nextPage()
            getWithSaveSuccess(
                apiName = "videos",
                params = page.pageParams,
                successDo = { res ->
                    val pageData = res.getPageData(Video::class.java)
                    page.updateTotal(pageData.total)
                    callback.onResult(pageData.rows, page)
                },
                completeDo = { isOk ->
                    if (!isOk) page.prevPage()
                }
            )
        }
    }
}