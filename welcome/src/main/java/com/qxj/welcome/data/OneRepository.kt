package com.qxj.welcome.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import java.util.concurrent.Executor

class OneRepository(private val executor: Executor) : Repository {

    private val TAG = OneRepository::class.java.simpleName

    override fun <T> getDataList(pageSize: Int): Listing<T> {
        /**
         * 数据工厂
         */

        Log.d(TAG, "创建数据工厂")
        //创建数据提供者的观察者  OneDataSource<T> 为具体的数据提供者
        val sourceLiveData = MutableLiveData<OneDataSource<T>>()

        //数据工厂
        val sourceFactory = object : DataSource.Factory<String, T>() {
            override fun create(): DataSource<String, T> {
                //刷新会重新创建
                Log.d(TAG, "初始化一个数据提供者")
                val source = OneDataSource<T>(retryExecutor = executor)
                //并推送当前状态到ui 如：刷新状态
                sourceLiveData.postValue(source)
                return source
            }

        }

        /**
         * 数据加载配置
         */
        Log.d(TAG, "初始化数据加载配置")
        val pagedListConfig = PagedList.Config.Builder()
                //.setPrefetchDistance(pageSize)//预加载的数目默认为setPageSize
                .setEnablePlaceholders(false)//当item为null是否使用PlaceHolder展示
                .setInitialLoadSizeHint(pageSize * 2)//定义第一页加载的数目
                .setPageSize(pageSize)//定义从DataSource 每一次加载的数目
                .build()

        /**
         * dataList
         */
        Log.d(TAG, "初始化数据观察者")
        val pagedList = LivePagedListBuilder(sourceFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build()

        return Listing(dataList = pagedList,
                networkState = Transformations.switchMap(sourceLiveData) {
                    //网络状态观察者
                    it.networkState
                },
                retry = {
                    sourceLiveData.value?.retryAllFailed()
                },
                refresh = {
                    //刷新方法的传递和执行者
                    sourceLiveData.value?.invalidate()
                },
                refreshState = Transformations.switchMap(sourceLiveData) {
                    Log.d(TAG, "刷新状态观察者 刷新时重新初始化")
                    it.initialLoad
                }
        )
    }
}

