package com.qxj.welcome.data

import com.qxj.commonbase.mvvm.Repository
import com.qxj.commonbase.mvvm.ServiceLocator
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

    /**
     * 线程池 Executors
     * 1.newCachedThreadPool
     *      创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
     * 2.newFixedThreadPool
     *      创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     * 3.newScheduledThreadPool
     *      创建一个定长线程池，支持定时及周期性任务执行。
     * 4.newSingleThreadExecutor
     *      创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
     */

    override fun getNetworkExecutor(): Executor =
            //创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
            Executors.newFixedThreadPool(5)

    override fun getDiskIOExecutor(): Executor =
            //单线程池
            Executors.newSingleThreadExecutor()

    override fun getApi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}