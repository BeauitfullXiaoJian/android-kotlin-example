package com.example.httprequest

import com.google.gson.Gson
import com.google.gson.JsonParser

class Pagination {

    // 当前页码，0为启始页
    var currentPageNum = START_PAGE
        private set
    // 数据总量（服务端提供）
    var total = 0
        private set
    // 分页对象是否已经处于活跃状态
    private var isActive = false
    // 当前是否正在加载数据
    var isLoading = false
    // 每页数据量
    var limit = DEFAULT_PAGE_SIZE
    // 最大页码
    val maxPage get() = Math.ceil(total / limit.toDouble())
    // 偏移量
    val offset get() = currentPageNum * limit
    // 是否有数据加载-如果分页对象处于未激活时默认有数据
    val hasData get() = (!isActive) || hasNext
    // 是否有下一页
    val hasNext get() = currentPageNum < maxPage
    // 是否有上一页
    val hasPrev get() = currentPageNum > START_PAGE
    // 是否可以加载上一页
    val canLoadPrev get() = hasPrev && !isLoading
    // 是否可以加载下一页
    val canLoadNext get() = hasNext && !isLoading
    // 分页参数
    val pageParams
        get() = hashMapOf(
            PAGE_LIMIT_KEY_STR to limit,
            PAGE_OFFSET_KEY_STR to offset
        )

    /**
     * 更新数据统计
     */
    fun updateTotal(nowTotal: Int) {
        total = if (nowTotal > 0) nowTotal else 0
        isActive = true
    }

    /**
     * 切换到下一页
     */
    fun nextPage(): Int {
        if (hasNext) {
            currentPageNum++
        }
        return currentPageNum
    }

    /**
     * 切换到下一页
     */
    fun prevPage(): Int {
        if (hasPrev) {
            currentPageNum--
        }
        return currentPageNum
    }

    /**
     * 重置分页对象
     */
    fun resetPagination() {
        isLoading = false
        currentPageNum = START_PAGE
        total = 0
        isActive = false
    }

    data class PageData<T>(
        var total: Int = 0,
        var rows: List<T> = listOf()
    )


    companion object {

        private const val PAGE_LIMIT_KEY_STR = "limit"
        private const val PAGE_OFFSET_KEY_STR = "offset"
        private const val PAGE_DATA_TOTAL_KEY_STR = "total"
        private const val PAGE_DATA_ROW_KEY_STR = "rows"
        private const val DEFAULT_PAGE_SIZE = 10
        private const val START_PAGE = 0

        /**
         * 创建一个分页对象
         */
        fun create(limit: Int): Pagination {
            val page = Pagination()
            page.limit = limit
            return page
        }

        /**
         * 字符串转换为分页数据对象
         */
        fun <T> stringToPageData(data: String, classOfT: Class<T>): PageData<T> {
            return try {
                val json = JsonParser().parse(data).asJsonObject
                val rows = json.get(PAGE_DATA_ROW_KEY_STR).asJsonArray
                PageData(
                    total = json.get(PAGE_DATA_TOTAL_KEY_STR).asInt,
                    rows = rows.map { Gson().fromJson(it.toString(), classOfT) }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                PageData()
            }
        }
    }
}