package com.qxj.welcome.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxj.commonbase.mvvm.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class BlankViewModel internal constructor (repository: Repository) : ViewModel() {
    /**
     * 开始一个kotlin协程
     */
    fun get() {
        viewModelScope.launch {

        }
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
