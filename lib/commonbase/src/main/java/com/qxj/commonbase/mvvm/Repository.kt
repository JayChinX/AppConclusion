package com.qxj.commonbase.mvvm

interface Repository {

    //状态保存
//    fun <T> getDataList(pageSize: Int = 0): Listing<T>

    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}