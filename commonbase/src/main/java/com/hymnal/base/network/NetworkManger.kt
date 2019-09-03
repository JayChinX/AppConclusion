package com.hymnal.base.network

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManger private constructor() {

    private val TAG = NetworkManger::class.java.simpleName

    var retrofit: Retrofit

    companion object {
        @Volatile
        private var instance: NetworkManger? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: NetworkManger()
                            .also {
                        instance = it
                    }
                }


    }

    init {
        Log.d(TAG, "NetworkManger 正在初始化")

        retrofit = Retrofit.Builder()//建造者模式
                .client(OkHttpManager.okHttpClient)
                .baseUrl(Api.HOST)
                /**
                 * 这个是用来决定你的返回值是Observable还是Call。
                 * // 使用call的情况  Call<String> checkLogin();
                 * // 使用Observable的情况  Observable<String> checkLogin();
                 */
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//适配器模式
                .addCallAdapterFactory(CoroutineCallAdapterFactory())//kotlin coroutine 协程进行网络请求
                /**
                 * 这个配置是将服务器返回的json字符串转化为对象。
                 * 这个是可以自定义Converter来应对服务器返回的不同的数据的
                 * https://www.jianshu.com/p/5b8b1062866b
                 */
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    //动态代理创建请求
    //object ApiService : Api by NetworkManger.getInstance().getApiService()
    inline fun <reified T> getApiService() : T =  retrofit.create(T::class.java)

}


