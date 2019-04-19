package com.qxj.welcome.utilities

import android.content.Context
import com.qxj.welcome.AppWelcome
import com.qxj.welcome.CustomApplication
import com.qxj.welcome.data.HomeRepository
import com.qxj.welcome.data.OneServiceLocator
import com.qxj.welcome.data.Repository
import com.qxj.welcome.viewmodels.HomeViewModelFactory
import com.qxj.welcome.viewmodels.OneViewModelFactory

object InjectorUtils {

    private fun getHomeRepository(context: Context): HomeRepository {
        return HomeRepository.getInstance()
    }

    fun provideHomeViewModelFactory(context: Context): HomeViewModelFactory {
        val repository = getHomeRepository(context)
        return HomeViewModelFactory(repository)
    }

    private fun getOneRepository(context: Context): Repository {
        return OneServiceLocator.getInstance()
                .getRepository(Repository.Type.IN_MEMORY_BY_ITEM)
    }

    fun provideOneViewModelFactory(context: Context): OneViewModelFactory {
        val repository = getOneRepository(context)
        return OneViewModelFactory(AppWelcome.INSTANCE, repository)
    }

}