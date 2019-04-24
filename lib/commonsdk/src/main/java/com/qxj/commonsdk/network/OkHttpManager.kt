package com.qxj.commonsdk.network

import android.util.Log
import com.qxj.commonsdk.CommonSDKApp
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class OkHttpManager private constructor() {

    private val TAG = OkHttpManager::class.java.simpleName
    private var isLogging = true
    var okHttpClient: OkHttpClient

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            OkHttpManager()
        }

        val okHttpClient by lazy {
            this.instance.okHttpClient
        }
        //单例等同写法
//        @Volatile
//        private var instance: OkHttpManager? = null
//        fun getInstance() =
//                instance ?: synchronized(this) {
//                    instance ?: OkHttpManager().also {
//                        instance = it
//                    }
//                }
    }

    init {
        okHttpClient = if (isLogging) {
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
                    /**
                     * 统一通用header
                     */
                    .addInterceptor {
                        val data = it.request()
                                .newBuilder()
                                .addHeader("mac", "")
                                .addHeader("uuid", "")
                                .addHeader("", "")
                                .build()
                        it.proceed(data)
                    }
                    /**
                     * 添加Token管理
                     */
                    .addInterceptor(TokenInterceptorImpl())
                    /**
                     * 持久化Cookie
                     */
                    .cookieJar(CookieJarImpl())

                    /**
                     * 两种添加证书的方法 进行添加https支持
                     * 1.找后台要证书 保存在assets目录下 进行读取
                     */
                    //证书列表
                    .addCertificates(CommonSDKApp.INSTANCE.assets.open("xx.cer"),
                            CommonSDKApp.INSTANCE.assets.open("xx.cer"))
                    /**
                     * 2.固定证书pinning
                     */
                    .certificatePinner(
                            CertificatePinner.Builder()
                                    .add("YOU API.com", "sha1/DmxUShsZuNiqPQsX2Oi9uv2sCnw=")
                                    .build()
                    )
                    /**
                     * 对服务器返回数据进行校验
                     *  用于客户端判断所连接的服务端是否可信
                     *  通常默认return true,或者简单校验hostname是否正确
                     *  hostname 请求地址
                     *  sSLSession 服务器返回的证书链
                     */
                    .hostnameVerifier { hostname, sSLSession ->
                        //获取所有证书
                        val localCertificate = sSLSession.peerCertificates
                        for (c in localCertificate) {
                            Log.d(TAG, "证书 ${c.toString()}")
                        }
                        true
                    }
                    .build()
        } else {
            OkHttpClient()
        }
    }
}