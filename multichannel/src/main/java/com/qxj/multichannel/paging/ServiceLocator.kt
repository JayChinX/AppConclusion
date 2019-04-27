package com.qxj.multichannel.paging

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.qxj.commonbase.network.Api
import com.qxj.multichannel.ApiService
import com.qxj.multichannel.paging.StudentRepository.Type.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                            app = context.applicationContext as Application,
                            useInMemoryDb = false)
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getRepository(type: StudentRepository.Type): StudentRepository

    fun getNetworkExecutor(): Executor

    fun getDiskIOExecutor(): Executor

    fun getRedditApi(): Api
}

open class DefaultServiceLocator(val app: Application, val useInMemoryDb: Boolean) : ServiceLocator {
    // thread pool used for disk access
    @Suppress("PrivatePropertyName")
    private val DISK_IO = Executors.newSingleThreadExecutor()

    // thread pool used for network requests
    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    //    private val db by lazy {
//        RedditDb.create(app, useInMemoryDb)
//    }
//
    private val api by lazy {
        ApiService
    }

    override fun getRepository(type: StudentRepository.Type): StudentRepository {
        //这里可以返回不同的数据源
        return when(type) {
            IN_MEMORY_BY_ITEM -> StudentDataRepository(
                    api = getRedditApi(),
                    executor = getNetworkExecutor())
            IN_MEMORY_BY_PAGE -> TODO()
            DB -> TODO()
        }
    }

    override fun getNetworkExecutor(): Executor = NETWORK_IO

    override fun getDiskIOExecutor(): Executor = DISK_IO

    override fun getRedditApi(): Api = api
}