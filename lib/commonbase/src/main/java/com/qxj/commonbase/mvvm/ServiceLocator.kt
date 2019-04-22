package com.qxj.commonbase.mvvm

import java.util.concurrent.Executor

interface ServiceLocator {

    fun getRepository(type: Repository.Type): Repository

    fun getNetworkExecutor(): Executor

    fun getDiskIOExecutor(): Executor

    fun getApi()
}