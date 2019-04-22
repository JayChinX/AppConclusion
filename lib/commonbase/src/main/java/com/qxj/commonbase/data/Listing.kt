package com.qxj.commonbase.data

import androidx.lifecycle.LiveData

data class Listing<T>(val dataList: LiveData<out List<T>>,
                      val networkState: LiveData<Resource<String>>?,
                      val refreshState: LiveData<Resource<String>>?,
                      val refresh: () -> Unit,
                      val retry: () -> Unit)