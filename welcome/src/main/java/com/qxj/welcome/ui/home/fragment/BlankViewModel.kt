package com.qxj.welcome.ui.home.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qxj.commonbase.mvvm.Repository
import com.qxj.commonbase.mvvm.ViewModelFactory
import com.qxj.welcome.ui.home.data.BlankRepository
import kotlinx.coroutines.*
import java.lang.Exception


class BlankViewModel internal constructor (private val repository: BlankRepository) : ViewModel() {


    val data = MutableLiveData<String>()

    /**
     * 开始一个kotlin协程
     */

    fun get() {
        viewModelScope.launch {
            delay(timeMillis = 3000)
            try {
                data.value = repository.getUserName()
            } catch (e: Exception) {
                data.value = e.message
            }

            repository.getStudents()
        }



    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    internal class BlankViewModelFactory(private val repository: Repository) : ViewModelFactory() {
        override fun getViewModel(): ViewModel = BlankViewModel(repository as BlankRepository)
    }
}
