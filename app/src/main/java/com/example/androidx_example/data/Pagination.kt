package com.example.androidx_example.data

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
        get() = hashMapOf<String, Any>(
            "limit" to limit,
            "offset" to offset
        )

    fun updateTotal(nowTotal: Int) {
        total = if (nowTotal > 0) nowTotal else 0
        isActive = true
    }

    fun nextPage(): Int {
        if (hasNext) {
            currentPageNum++
        }
        return currentPageNum
    }

    fun prevPage(): Int {
        if (hasPrev) {
            currentPageNum--
        }
        return currentPageNum
    }

    fun resetPagination() {
        isLoading = false
        currentPageNum = START_PAGE
        total = 0
        isActive = false
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val START_PAGE = 0
        fun create(limit: Int): Pagination {
            val page = Pagination()
            page.limit = limit
            return page
        }
    }
}