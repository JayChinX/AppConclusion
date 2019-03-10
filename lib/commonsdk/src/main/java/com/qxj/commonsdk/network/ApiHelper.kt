package com.qxj.commonsdk.network

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
import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

//region api service
object ApiService : Api by NetworkManger.instance.apiService
//endregion
//region 拦截器
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
 * 构造错误类型
 */
//enum class ErrorType(s: String) {
//    INT_ERROR(""),
//
//}
//endregion

//fun <T> Observable<T>.compose(): ObservableTransformer<T, T> {
//    return ObservableTransformer {
//        it.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//    }
//}

//region rxjava 扩展函数

fun <T> Observable<T>.async(withDelay: Long = 0): Observable<T> =
        this.subscribeOn(Schedulers.io())
                .delay(withDelay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())

//`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
fun <T> Observable<T>.bindLifecycle(owner: LifecycleOwner): ObservableSubscribeProxy<T> =
        this.`as`(AutoDispose
                .autoDisposable(AndroidLifecycleScopeProvider
                        .from(owner, Lifecycle.Event.ON_DESTROY)))

//endregion

//region json字符串转换
fun Gson.toRequestBody(map: HashMap<String, String>): RequestBody =
        RequestBody.create(okhttp3.MediaType.parse("Content-Type:application/json"), Gson().toJson(map))

//endregion

//region https加密适配
fun OkHttpClient.Builder.addCertificates(vararg input: InputStream): OkHttpClient.Builder {
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

//endregion