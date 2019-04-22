package com.qxj.welcome.utilities

import com.qxj.commonbase.mvvm.Repository
import com.qxj.commonbase.mvvm.ViewModelFactory
import com.qxj.welcome.data.BlankRepository
import com.qxj.welcome.data.HomeRepository
import com.qxj.welcome.data.OneServiceLocator
import com.qxj.welcome.viewmodels.BlankViewModelFactory
import com.qxj.welcome.viewmodels.HomeViewModelFactory
import com.qxj.welcome.viewmodels.OneViewModelFactory

//viewModel 的 repository 注入类
object InjectorUtils {

    fun provideHomeViewModelFactory(): ViewModelFactory {
        val repository = getHomeRepository()
        return HomeViewModelFactory(repository)
    }

    fun provideOneViewModelFactory(): ViewModelFactory {
        val repository = getOneRepository()
        return OneViewModelFactory(repository)
    }

    fun provideBlankViewModelFactory(): ViewModelFactory {
        val repository = getBlankRepository()
        return BlankViewModelFactory(repository)
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