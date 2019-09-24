package com.example.androidx_example.data

import androidx.paging.PageKeyedDataSource
import com.example.androidx_example.until.tool.debugInfo
import com.example.httprequest.Pagination
import com.example.httprequest.Request

class VideoDataSource : PageKeyedDataSource<Pagination, Video>() {

    override fun loadInitial(
        params: LoadInitialParams<Pagination>,
        callback: LoadInitialCallback<Pagination, Video>
    ) {
        val page = Pagination.create(params.requestedLoadSize)
        debugInfo("加载数据量" + params.requestedLoadSize)
        Request.get(
            apiName = "videos",
            params = page.pageParams,
            allowSave = true,
            successDo = { res ->
                val pageData = res.getPageData(Video::class.java)
                page.updateTotal(pageData.total)
                callback.onResult(pageData.rows, page, page)
            },
            completeDo = { isOk ->
                if (!isOk) {
                    page.resetPagination()
                }
            }
        )
    }

    override fun loadBefore(
        params: LoadParams<Pagination>,
        callback: LoadCallback<Pagination, Video>
    ) {
        val page = params.key
        if (page.hasPrev) {
            page.prevPage()
            Request.get(
                apiName = "videos",
                params = page.pageParams,
                allowSave = true,
                successDo = { res ->
                    val pageData = res.getPageData(Video::class.java)
                    page.updateTotal(pageData.total)
                    callback.onResult(pageData.rows, page)
                },
                completeDo = { isOk ->
                    if (!isOk) {
                        page.nextPage()
                    }
                }
            )
        }
    }

    override fun loadAfter(
        params: LoadParams<Pagination>,
        callback: LoadCallback<Pagination, Video>
    ) {
        val page = params.key
        debugInfo("加载更多")
        if (page.hasNext) {
            page.nextPage()
            Request.get(
                apiName = "videos",
                params = page.pageParams,
                allowSave = true,
                successDo = { res ->
                    val pageData = res.getPageData(Video::class.java)
                    page.updateTotal(pageData.total)
                    callback.onResult(pageData.rows, page)
                },
                completeDo = { isOk ->
                    if (!isOk) {
                        page.prevPage()
                    }
                }
            )
        }
    }
}