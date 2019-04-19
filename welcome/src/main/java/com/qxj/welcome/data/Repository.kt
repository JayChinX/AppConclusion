package com.qxj.welcome.data

interface Repository {

    fun <T> getDataList(pageSize: Int): PageListing<T>

    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}