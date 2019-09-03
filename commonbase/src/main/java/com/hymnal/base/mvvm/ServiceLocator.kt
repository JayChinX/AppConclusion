package com.hymnal.base.mvvm

import com.hymnal.base.mvvm.Repository
import java.util.concurrent.Executor

interface ServiceLocator {

    fun getRepository(type: Repository.Type): Repository

    fun getNetworkExecutor(): Executor

    fun getDiskIOExecutor(): Executor

    fun getApi()
}