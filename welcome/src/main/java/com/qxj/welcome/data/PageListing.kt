package com.qxj.welcome.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PageListing<T> (val pagedList: LiveData<PagedList<T>>,
                           val networkState: LiveData<Resource<String>>,
                           val refreshState: LiveData<Resource<String>>,
                           val refresh: () -> Unit,
                           val retry: () -> Unit)