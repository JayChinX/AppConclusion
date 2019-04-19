package com.qxj.welcome.data

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class OneServiceLocator : ServiceLocator {

    companion object {
        private var instance: ServiceLocator? = null
        private val LOCK = Any()
        fun getInstance() =
                instance ?: synchronized(LOCK) {
                    instance ?: OneServiceLocator().also {
                        instance = it
                    }
                }
    }

    override fun getRepository(type: Repository.Type): Repository = when(type) {

        Repository.Type.IN_MEMORY_BY_ITEM -> OneRepository(executor = getNetworkExecutor())
        Repository.Type.IN_MEMORY_BY_PAGE -> TODO()
        Repository.Type.DB -> TODO()
    }

    override fun getNetworkExecutor(): Executor = Executors.newFixedThreadPool(5)

    override fun getDiskIOExecutor(): Executor = Executors.newSingleThreadExecutor()

    override fun getApi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}