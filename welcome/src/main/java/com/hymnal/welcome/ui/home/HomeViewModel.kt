package com.hymnal.welcome.ui.home

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.hymnal.base.mvvm.Repository
import com.hymnal.base.mvvm.ViewModelFactory
import com.hymnal.welcome.service.SocketService
import com.hymnal.welcome.ui.home.data.HomeRepository
import com.hymnal.welcome.utilities.startActivity
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

    fun startActivity(res: Int) {
        startActivity("/home/activity/SecondActivity")
    }

    private fun startSocket() {
        val startService = Intent(getApplication(), SocketService::class.java)
        getApplication<Application>().startService(startService)

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



