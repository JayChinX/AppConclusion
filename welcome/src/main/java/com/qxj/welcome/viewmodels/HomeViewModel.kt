package com.qxj.welcome.viewmodels

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.qxj.commonbase.mvvm.Repository
import com.qxj.commonbase.mvvm.ViewModelFactory
import com.qxj.welcome.CustomApplication
import com.qxj.welcome.activity.HomeActivity
import com.qxj.welcome.base.AppWelcome
import com.qxj.welcome.data.HomeRepository
import com.qxj.welcome.service.SocketService
import com.qxj.welcome.utilities.Navigation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeViewModel internal constructor(application: Application, private val repository: HomeRepository) : AndroidViewModel(application) {

    private val TAG = HomeViewModel::class.java.simpleName

    private val data = MutableLiveData<String>()

    private val repoResult = Transformations.map(data) {
        repository.getDataList()
    }
    val posts = Transformations.switchMap(repoResult) { it }

    val pager = repository.getPagerNum()

    val title = Transformations.map(pager) { posts.value?.get(it)?.name }


    fun showData() {
        Log.d(TAG, "开始加载数据")
        data.value = ""
        startSocket()
    }

    fun pagerChange(position: Int) {
        if (position == pager.value) return
        pager.value = position
        Log.d(TAG, "切换到页面$position -> $title")
    }

    fun toOtherActivity() {
        Navigation.getInstance().toOtherActivity("/home/activity/SecondActivity")
    }

    private fun startSocket() {
        val startService = Intent(getApplication(), SocketService::class.java)
        getApplication<CustomApplication>().startService(startService)

    }

    fun sendMsg() {
        viewModelScope.launch {
            repository.send()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }



    internal class HomeViewModelFactory(private val application: Application, private val repository: Repository) : ViewModelFactory() {

        override fun getViewModel(): ViewModel = HomeViewModel(application, repository as HomeRepository)

    }
}



