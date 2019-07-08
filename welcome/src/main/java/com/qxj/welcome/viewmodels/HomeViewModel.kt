package com.qxj.welcome.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.qxj.commonbase.mvvm.Repository
import com.qxj.commonbase.mvvm.ViewModelFactory
import com.qxj.welcome.data.HomeRepository
import com.qxj.welcome.utilities.Navigation

class HomeViewModel internal constructor(repository: HomeRepository) : ViewModel() {

    private val TAG = HomeViewModel::class.java.simpleName

    private val data = MutableLiveData<String>()
    private val repoResult = Transformations.map(data) {
        repository.getDataList()
    }
    val posts = Transformations.switchMap(repoResult) {
        it
    }

    private val gardenList by lazy { posts.value }
    private val defaultPager = 0
    val pager = MutableLiveData<Int>(defaultPager)

    val title = Transformations.map(pager) {
        gardenList?.get(it)?.name
    }


    fun showData() {
        Log.d(TAG, "开始加载数据")
        data.value = ""
    }

    fun pagerChange(position: Int) {
        if (position == pager.value) return
        pager.value = position
        Log.d(TAG, "切换到页面$position -> $title")
    }

    fun toOtherActivity() {
        Navigation.getInstance().toOtherActivity("/home/activity/SecondActivity")
    }

    internal class HomeViewModelFactory(private val repository: Repository) : ViewModelFactory() {

        override fun getViewModel(): ViewModel = HomeViewModel(repository as HomeRepository)

    }
}



