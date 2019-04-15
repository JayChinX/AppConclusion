package com.qxj.commondata.paging

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.Transformations.switchMap
import android.arch.paging.DataSource
import android.arch.paging.ItemKeyedDataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.util.Log
import com.qxj.commonsdk.network.Api
import com.qxj.commonsdk.network.Result
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor


class StudentDataSourceFactory(private val api: Api, private val retryExecutor: Executor) : DataSource.Factory<String, Student>() {

    val sourceLiveData = MutableLiveData<StudentDataSource>()
    /**
     * pagedList 的使用者
     */
    override fun create(): DataSource<String, Student> {
        //数据源
        val source = StudentDataSource(api = api, retryExecutor = retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}

//数据源  负责加载第一页和后面每一页的数据
/**
 * 1.PageKeyedDataSource<Key, Value>
 *     适用于目标数据根据页信息请求数据，即key字段是页面相关的信息，如：请求参数中包含页数的信息
 * 2.ItemKeyedDataSource<Key, Value>
 *     适用于目标数据的加载依赖特定item的信息，即key字段包含的是item中的信息，如：根据第N项的信息，加载第N+1项的数据，
 *     传入的参数中需要传入第N项的ID
 * 3.PositionalDataSource<T>
 *     适用于目标数据总数固定，从特定位置加载数据，如：从2000条数据中的第1200条数据开始加载
 */
open class StudentDataSource(private val api: Api, private val retryExecutor: Executor) : ItemKeyedDataSource<String, Student>() {

    private var retry: (() -> Any)? = null
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

    val networkState by lazy {
        MutableLiveData<Resource<String>>()
    }

    val initialLoad by lazy {
        MutableLiveData<Resource<String>>()
    }

    //接收初始加载第一页的数据
    // 用于接收初始第一页加载的数据，在这里需要将获取到的数据通过LoadInitialCallback的onResult进行回调，
    // 用于出始化PagedList，并对加载的项目进行计数
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<Student>) {


        api.getStudent<Student>().enqueue(object : retrofit2.Callback<Result<Student>> {
            override fun onFailure(call: Call<com.qxj.commonsdk.network.Result<Student>>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<Result<Student>>, response: Response<Result<Student>>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        //正在请求网络
        networkState.postValue(Resource.loading(null))
        //正在初始化加载
        initialLoad.postValue(Resource.loading(null))

        //模拟耗时操作
        //初始化操作
        val list = loadData(startPosition, params.requestedLoadSize)
        retry = null
        //网络请求成功
        networkState.postValue(Resource.success(null))
        //初始化加载成功
        initialLoad.postValue(Resource.success(null))
        //数据加载完成回调  通知调用者数据加载完成 数据  上一页  下一页
        callback.onResult(list)
        startPosition += list.size
        Log.d("qxj", "loadInitial $startPosition")
    }

    private fun loadData(startPosition: Int, limit: Int): List<Student> {
        val list = ArrayList<Student>()
        for (i in 0 until limit) {
            val position = startPosition + i
            list.add(Student(position, "学生@$position"))
        }
        return list
    }

    //接收加载的数据  加载后面每一页的数据
    // 用于接收后面每一页加载的数据，使用方法和loadInitial一样
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<Student>) {
        //正在请求网络
        networkState.postValue(Resource.loading(null))

        //模拟耗时操作
        val list = loadData(startPosition, params.requestedLoadSize)
        retry = null
        //网络请求成功
        networkState.postValue(Resource.success(null))
        //数据加载完成回调
        callback.onResult(list)
        startPosition += list.size
        Log.d("qxj", "loadAfter $startPosition")
    }

    //指定的密钥之前加载列表数据
    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<Student>) {
        Log.d("qxj", "loadBefore")
    }

    //返回与给定项目关联的密钥
    override fun getKey(item: Student): String = item.id.toString()

}

class StudentDataRepository(private val api: Api, private val executor: Executor) : StudentRepository {

    override fun getStudentList(pageSize: Int): Listing<Student> {

        //数据源工厂  对数据源进行观察
        /**
         * DataSource.Factory<Key, Value>
         */
        val sourceFactory = StudentDataSourceFactory(api = api, retryExecutor = executor)

        /**
         * config
         * 定义加载数据的方式
         */
        val pagedListConfig = PagedList.Config.Builder()
                //.setPrefetchDistance(pageSize)//预加载的数目默认为setPageSize
                .setEnablePlaceholders(false)//当item为null是否使用PlaceHolder展示
                .setInitialLoadSizeHint(pageSize * 2)//定义第一页加载的数目
                .setPageSize(pageSize)//定义从DataSource 每一次加载的数目
                .build()

        /**
         * pagedList
         * 创建pagedList
         *
         */
        val pagedList = LivePagedListBuilder(sourceFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build()

        val refreshState = switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }

        return Listing<Student>(pagedList,
                networkState = switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                },
                retry = {
                    sourceFactory.sourceLiveData.value?.retryAllFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState)
    }

}

class StudentViewModel(app: Application, postRepository: StudentRepository) : AndroidViewModel(app) {

    //合并
    private val data = MutableLiveData<String>()
    private val repoResult = Transformations.map(data) {
        postRepository.getStudentList(10)
    }

    val posts = Transformations.switchMap(repoResult) {
        it.pagedList
    }!!
    val networkState = Transformations.switchMap(repoResult) {
        it.networkState
    }!!
    val refreshState = Transformations.switchMap(repoResult) {
        it.refreshState
    }!!

    //下拉重新加载
    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun showData(subreddit: String): Boolean {
        if (data.value == subreddit) {
            return false
        }
        data.value = subreddit
        return true
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }

    fun currentData(): String? = data.value

}


