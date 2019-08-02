package com.qxj.welcome.utilities

import com.qxj.commonbase.mvvm.Repository
import com.qxj.commonbase.mvvm.ViewModelFactory
import com.qxj.welcome.base.App
import com.qxj.welcome.ui.home.data.BlankRepository
import com.qxj.welcome.ui.home.data.HomeRepository
import com.qxj.welcome.ui.home.data.OneServiceLocator
import com.qxj.welcome.ui.home.fragment.BlankViewModel
import com.qxj.welcome.ui.home.HomeViewModel
import com.qxj.welcome.ui.home.fragment.OneViewModel

//viewModel 的 repository 注入类
object InjectorUtils {

    fun provideHomeViewModelFactory(): ViewModelFactory {
        val repository = getHomeRepository()
        return HomeViewModel.HomeViewModelFactory(App.INSTANCE, repository)
    }

    fun provideOneViewModelFactory(): ViewModelFactory {
        val repository = getOneRepository()
        return OneViewModel.OneViewModelFactory(repository)
    }

    fun provideBlankViewModelFactory(): ViewModelFactory {
        val repository = getBlankRepository()
        return BlankViewModel.BlankViewModelFactory(repository)
    }

    //home
    private fun getHomeRepository(): Repository {
        return HomeRepository.getInstance()
    }

    //one
    private fun getOneRepository(): Repository {
        return OneServiceLocator.getInstance()
                .getRepository(Repository.Type.IN_MEMORY_BY_ITEM)
    }

    //B
    private fun getBlankRepository(): Repository {
        return BlankRepository()
    }

}