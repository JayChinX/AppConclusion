package com.qxj.welcome.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qxj.welcome.data.HomeRepository

class HomeViewModel internal constructor(
        repository: HomeRepository
) : ViewModel() {

    private val TAG = HomeViewModel::class.java.simpleName

    private val defaultPager = repository.getDefaultFragment()
    val fragments = repository.getFragments()
    private val fragmentNames = repository.getFragmentNames()

    var pagerItem: MutableLiveData<Int> = MutableLiveData(defaultPager)
    var title = fragmentNames[defaultPager]

    fun pagerChange(position: Int) {
        if (position == pagerItem.value) return
        title = fragmentNames[position]
        pagerItem.value = position
        Log.d(TAG, "切换到页面$position -> $title")
    }

}

class HomeViewModelFactory(
        private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //在这里注入 repository
        return HomeViewModel(repository) as T
    }
}