package com.qxj.welcome.data

interface Repository {

    //状态保存
    fun <T> getDataList(pageSize: Int): Listing<T>

    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}