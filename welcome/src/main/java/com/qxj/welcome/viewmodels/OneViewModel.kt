package com.qxj.welcome.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.qxj.welcome.data.Repository

class OneViewModel(app: Application, repository: Repository) : AndroidViewModel(app) {

    private val TAG = OneViewModel::class.java.simpleName

    /**
     * LiveData 合并数据观察者
     */

    private val data = MutableLiveData<String>()
    //过滤出来 repository 的 getDataList方法返回的 PageListing(状态保存者) 观察者
    private val repoResult = Transformations.map(data) {
        Log.d(TAG, "repository.getDataList")
        repository.getDataList<Data>(20)
    }

    //从 PageListing 中分离出 具体的数据PagedList<Data> paged数据
    val posts = Transformations.switchMap(repoResult) {
        it.pagedList
    }
    //从 PageListing 中分离出 具体的网络请求状态 数据观察者
    val networkState = Transformations.switchMap(repoResult) {
        it.networkState
    }
    //从 PageListing 中分离出 具体的刷新状态 数据观察者
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