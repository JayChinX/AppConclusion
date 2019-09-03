package com.hymnal.welcome.utilities

import com.hymnal.base.mvvm.Repository
import com.hymnal.base.mvvm.ViewModelFactory
import com.hymnal.welcome.base.App
import com.hymnal.welcome.ui.home.data.BlankRepository
import com.hymnal.welcome.ui.home.data.HomeRepository
import com.hymnal.welcome.ui.home.data.OneServiceLocator
import com.hymnal.welcome.ui.home.fragment.BlankViewModel
import com.hymnal.welcome.ui.home.HomeViewModel
import com.hymnal.welcome.ui.home.fragment.OneViewModel

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