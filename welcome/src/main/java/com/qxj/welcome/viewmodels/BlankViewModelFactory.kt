package com.qxj.welcome.viewmodels

import androidx.lifecycle.ViewModel
import com.qxj.commonbase.mvvm.Repository
import com.qxj.commonbase.mvvm.ViewModelFactory
import com.qxj.welcome.data.BlankRepository

class BlankViewModelFactory(private val repository: Repository) : ViewModelFactory() {
    override fun getViewModel(): ViewModel = BlankViewModel(repository as BlankRepository)
}