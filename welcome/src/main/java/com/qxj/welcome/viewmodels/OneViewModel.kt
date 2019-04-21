package com.qxj.welcome.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.qxj.welcome.data.Repository

class OneViewModel(app: Application, repository: Repository) : AndroidViewModel(app) {

    private val TAG = OneViewModel::class.java.simpleName

    /**
     * LiveData 合并数据观察者
     */


    /**
     * LiveData的两个实现类：
     *      MutableLiveData和MediatorLiveData。
     * 1.MutableLiveData
     *      继承自LiveData,其主要是对外公布了setValue和postValue(注意方法的调用时所属线程)。
     * 2.MediatorLiveData
     *      又继承自MutableLiveData类，也就是说该类也公开了setValue和postValue方法。
     *      该类该又增加了监测其他LiveData实例值个更改并处理它们发出的事件
     *      addSource(LiveData<S> source, Observer<S> onChanged)：添加监控的LiveData实例值及LiveData的值变化时处理的Observe
     *      removeSource(LiveData<S> toRemote):移除监控的LiveData实例值
     */

    private val data = MutableLiveData<String>()
    //map 装换函数，map(
    //            @NonNull LiveData<X> source,//转换前的数据源
    //            @NonNull final Function<X, Y> mapFunction)//装换的函数
    private val repoResult = Transformations.map(data) {
        Log.d(TAG, "repository.getDataList")
        repository.getDataList<Data>(20)
    }


    //从 Listing 中分离出 具体的数据PagedList<Data> paged数据
    //switchMap 同 map 将LiveData的值更改信息后传递给子LiveData
    //***不同的是，传递给它的函数必须返回一个LiveData对象
    @Suppress("UNCHECKED_CAST")
    val posts = Transformations.switchMap(repoResult) {
        it.dataList as LiveData<PagedList<Data>>
    }
    //从 Listing 中分离出 具体的网络请求状态 数据观察者
    val networkState = Transformations.switchMap(repoResult) {
        it.networkState
    }
    //从 Listing 中分离出 具体的刷新状态 数据观察者
    val refreshState = Transformations.switchMap(repoResult) {
        it.refreshState
    }

    //执行刷新方法
    fun refresh() {
        Log.d(TAG, "开始刷新")
        repoResult.value?.refresh?.invoke()
    }

    fun showData(subreddit: String): Boolean {
        //只加载一次某一板块
        Log.d(TAG, "加载 ${data.value} $subreddit")
        if (data.value == subreddit) return false
        Log.d(TAG, "开始加载 ${data.value} $subreddit")
        //通知data 数据变化 从这里出发 repoResult的一系列活动
        data.value = subreddit
        return true
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }

    fun currentData() = data.value

}

class OneViewModelFactory constructor(
        private val app: Application,
        private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return OneViewModel(app, repository) as T
    }
}

data class OneData(override var id: Int, var name: String) : Data()

abstract class Data {
    abstract var id: Int
}