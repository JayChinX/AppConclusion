package com.qxj.commonsdk.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkManger private constructor() {

    private val TAG = NetworkManger::class.java.simpleName
    private var retrofit: Retrofit
    private var isLogging = true

    companion object {
        val instance: NetworkManger by lazy { NetworkManger() }//lazy 懒加载
    }

    init {
        val okHttpClient: OkHttpClient = if (isLogging) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            //建造者模式
            OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)//连接超时 5s
                    .writeTimeout(10, TimeUnit.SECONDS)//写
                    .readTimeout(10, TimeUnit.SECONDS)//读
                    /**
                     * 还有很多的用法，比如：封装一些公共的参数等等。
                     * 参考如下博客：http://blog.csdn.net/jdsjlzx/article/details/52063950
                     */
                    .addInterceptor(logging)//添加拦截器 log拦截
//                    /**
//                     * 统一通用header
//                     */
//                    .addInterceptor {
//                        val data = it.request()
//                                .newBuilder()
//                                .addHeader("mac", "")
//                                .addHeader("uuid", "")
//                                .addHeader("", "")
//                                .build()
//                        it.proceed(data)
//                    }
                    /**
                     * 添加证书pinning
                     */
//                    .certificatePinner(
//                            CertificatePinner.Builder()
//                            .add("YOU API.com", "sha1/DmxUShsZuNiqPQsX2Oi9uv2sCnw=")
//                            .build()
//                    )
                    /**
                     * 添加https支持
                     * 1.找后台要证书 保存在assets目录下
                     */
//                    .getCertificates()
//                    .hostnameVerifier { hostname, session ->
//                        true
//                    }
                    .build()
        } else {
            OkHttpClient()
        }
        retrofit = Retrofit.Builder()//建造者模式
                .client(okHttpClient)
                .baseUrl(Api.HOST)
                /**
                 * 这个是用来决定你的返回值是Observable还是Call。
                 * // 使用call的情况  Call<String> checkLogin();
                 * // 使用Observable的情况  Observable<String> checkLogin();
                 */
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//适配器模式
                /**
                 * 这个配置是将服务器返回的json字符串转化为对象。
                 * 这个是可以自定义Converter来应对服务器返回的不同的数据的
                 * https://www.jianshu.com/p/5b8b1062866b
                 */
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val apiService: Api
        get() = retrofit.create(Api::class.java)//动态代理创建请求

}


