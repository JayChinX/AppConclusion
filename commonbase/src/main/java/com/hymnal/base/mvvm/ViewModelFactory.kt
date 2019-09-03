package com.hymnal.base.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //注入ViewModel
        return getViewModel() as T
    }

    abstract fun getViewModel(): ViewModel
}