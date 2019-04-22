package com.qxj.welcome.viewmodels

import androidx.lifecycle.ViewModel
import com.qxj.welcome.base.AppWelcome
import com.qxj.commonbase.mvvm.Repository
import com.qxj.commonbase.mvvm.ViewModelFactory

class OneViewModelFactory(private val repository: Repository) : ViewModelFactory() {

    override fun getViewModel(): ViewModel = OneViewModel(AppWelcome.INSTANCE, repository)

}