package com.qxj.commondata.paging

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

interface StudentRepository {
    fun getStudentList(pageSize: Int): Listing<Student>

    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}

data class Listing<T>(
        val pagedList: LiveData<PagedList<T>>,
        val networkState: LiveData<Resource<String>>,
        val refreshState: LiveData<Resource<String>>,
        val refresh: () -> Unit,
        val retry: () -> Unit)