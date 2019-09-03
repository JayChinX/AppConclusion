package com.hymnal.welcome.ui.home.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.hymnal.base.data.Resource
import com.hymnal.welcome.ui.home.data.model.OneData
import java.util.concurrent.Executor

/**
 * 具体的数据请求类
 */
class OneDataSource<T>(private val retryExecutor: Executor) : ItemKeyedDataSource<String, T>() {

    private val TAG = OneDataSource::class.java.simpleName

    private var retry : (() -> Any)? = null
    private var startPosition: Int = 0

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    val initialLoad by lazy {
        MutableLiveData<Resource<String>>()
    }

    val networkState by lazy {
        MutableLiveData<Resource<String>>()
    }

    //初始化 并加载第一页
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<T>) {
        Log.d(TAG, "loadInitial")
        //状态变化 正在初始化
        initialLoad.postValue(Resource.loading(null))
        //状态变化 正在请求网络
        networkState.postValue(Resource.loading(null))
        /**
         * 初始化操作
         */
        val list = loadData(startPosition, params.requestedLoadSize)
        retry = null


        /**
         * 网络请求操作
         */

        /**
         *初始化和网络请求完成
         */


        initialLoad.postValue(Resource.success(null))
        networkState.postValue(Resource.success(null))
        //通知 调用者去加载数据
        callback.onResult(list)
        //记录下一次的起始页码
        startPosition += list.size

    }

    private fun loadData(startPosition: Int, limit: Int): List<T> {
        Log.d(TAG, "请求 从 $startPosition 开始 的 $limit 条数据")
        val dataList = ArrayList<T>()
        for (i in 0 until limit) {
            val position = startPosition + i
            dataList.add(OneData(position, "数据$position") as T)
        }
        Thread.sleep(1000)//模拟延时
        return dataList
    }

    /**
     * 用于加载之后的每一页数据
     */
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<T>) {
        Log.d(TAG, "loadAfter $startPosition")
        //状态变化 正在请求网络
        networkState.postValue(Resource.loading(null))

        val list: List<T>
        if (startPosition < 40) {
            Log.d(TAG, "loadAfter 正在 loadData")
            list = loadData(startPosition, params.requestedLoadSize)
            retry = null
            networkState.postValue(Resource.success(null))

            //通知 调用者去加载数据
            callback.onResult(list)
            //记录下一次的起始页码
            startPosition += list.size
        } else {
            retry = null
            Log.d(TAG, "loadAfter 没有更多了")
            networkState.postValue(Resource.error("没有更多了"))
        }

    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<T>) {
        Log.d(TAG, "loadBefore")
    }

    override fun getKey(item: T): String = item.let {
        Log.d(TAG, "getKey")
        it.toString()
    }
}