package com.qxj.welcome.viewmodels

import androidx.lifecycle.ViewModel
import com.qxj.commonbase.mvvm.Repository
import com.qxj.commonbase.mvvm.ViewModelFactory

class BlankViewModelFactory(private val repository: Repository) : ViewModelFactory() {
    override fun getViewModel(): ViewModel = BlankViewModel(repository)
}