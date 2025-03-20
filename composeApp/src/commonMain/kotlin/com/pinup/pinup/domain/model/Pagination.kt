package com.pinup.pinup.domain.model

class Pagination {
    var totalPage: Int? = null
    val isLast: Boolean
        get() = totalPage?.let {
            pageNum == it - 1
        } ?: false
    var pageNum = 0
        private set

    fun nextPage(): Int {
        return pageNum.inc()
    }

    fun initialize() {
        pageNum = 0
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}