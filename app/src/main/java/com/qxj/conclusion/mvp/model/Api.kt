package com.qxj.conclusion.mvp.model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.google.gson.Gson
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.IllegalStateException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

interface Api {

    companion object {
        val HOST = "http://10.206.24.100:9911/location/"
    }

    /**
     * get
     */
    @GET("User")//  User?userId="..."
    fun getUser(@Query("userId") userId: String): Observable<Result<UserBean>>

    @GET("changePwd")//  changePwd?{..., ...}
    fun toChangePassword(@QueryMap options: Map<String, String>): Observable<Result<UserBean>>

    @GET("group/{id}/users")//  group/1/users?sort="2"
    fun getGroup(@Path("id") group: Int, @Query("userId") userId: String): Observable<Result<UserBean>>

    /**
     * post
     */
    //指定一个对象作为http请求体
    @Headers("Content-Type: application/json")
    @POST("UserNowLocation")//添加Body请求体
    fun getUserLocation(@Body body : RequestBody): Observable<Result<List<LocationBean>>>

    //    @Headers("apikey:81bf9da930c7f9825a3c3383f1d8d766")
    @FormUrlEncoded//post 上传表单
    @POST("user")
    fun login(@Field("first_name") first: String, @Field("last_name") last: String): Observable<Result<UserBean>>

    //Header头部
    @Headers("Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App")
    @GET("user")
    fun getUser(): Observable<Result<UserBean>>

    @GET("")
    fun getPassword(@Header("Author") header: String): Observable<Result<UserBean>>

}


/**
 * 通用返回参数
 */
//data class Result<T>(var ecdoe: Int, var message: String, var data: T?)

data class Result<T>(var ecdoe: Int, var message: T?)


object ApiService : Api by NetworkManger.getInstance().apiService

/**
 * 返回数据拦截器
 */

fun <T> ObservableSubscribeProxy<T>.subscribes(success: (T) -> Unit, failure: (Int) -> Unit) {
    this.subscribe(object : Observer<T> {
        override fun onComplete() {

        }

        override fun onSubscribe(d: Disposable) {
            //开始网络请求
        }

        override fun onNext(t: T) {
            //成功
            success(t)
        }

        override fun onError(e: Throwable) {
            //失败
            failure(0)
        }
    }
    )
}
abstract class Response<T> : Observer<Result<T>> {

    abstract fun success(data: T)

    abstract fun failure(statusCode: Int)

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: Result<T>) {


    }

    override fun onComplete() {


    }

    override fun onError(e: Throwable) {

    }

}
/**
 * 实例代码
 */

//ApiService.getUserLocation("H2409761")
//                .async(100)//扩展函数
//                .doOnSubscribe {
//                    //开始网络请求 showLoading()
//                }
//                .doAfterTerminate {
//                    //网络请求完毕 hideLoading()
//                }
//                .bindLifecycle(owner)//扩展函数绑定生命周期
//                .subscribe({ data ->
//                    data?.let {
//                        it.data.let {
//
//                        }
//                    }
//                }, {
//                    e ->
//
//                })
//        ApiService.login(name, password)
//                .async(100)//扩展函数
//                .doOnSubscribe {
//                    //开始网络请求
//                    mView.get()?.showLoading()
//                }
//                .doAfterTerminate {
//                    //网络请求完毕
//                    mView.get()?.hideLoading()
//                }
//                .bindLifecycle(owner)//扩展函数绑定生命周期
//                .subscribe(object : Response<UserBean>() {
//                    override fun success(data: UserBean) {
//                    }
//
//                    override fun failure(statusCode: Int) {
//                    }
//                })//自定义范围数据拦截器

/**
 * 构造错误类型
 */
enum class ErrorType(s: String) {
    INT_ERROR(""),

}

//fun <T> Observable<T>.compose(): ObservableTransformer<T, T> {
//    return ObservableTransformer {
//        it.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//    }
//}

fun <T> Observable<T>.async(withDelay: Long = 0): Observable<T> =
        this.subscribeOn(Schedulers.io())
                .delay(withDelay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())

//`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
fun <T> Observable<T>.bindLifecycle(owner: LifecycleOwner): ObservableSubscribeProxy<T> =
        this.`as`(AutoDispose
                .autoDisposable(AndroidLifecycleScopeProvider
                        .from(owner, Lifecycle.Event.ON_DESTROY)))

fun Gson.toRequestBody(map: HashMap<String, String>): RequestBody =
        RequestBody.create(okhttp3.MediaType.parse("Content-Type:application/json"),Gson().toJson(map))

fun OkHttpClient.Builder.getCertificates(vararg input: InputStream): OkHttpClient.Builder{
    try {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)
        var index = 0
        input.forEach {
            index++
            val certificateAlias = index.toString()
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(it))
            try {
                it.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val tr = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tr.init(keyStore)
        val trustManagers = tr.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers: ${Arrays.toString(trustManagers)}")
        }

        val trustManager = trustManagers[0]
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tr.trustManagers, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        this.sslSocketFactory(sslSocketFactory, trustManager as X509TrustManager)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return this
}