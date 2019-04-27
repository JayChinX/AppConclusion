package com.qxj.conclusion.api

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qxj.commonbase.network.Result
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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
    })
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
                .delay(withDelay, TimeUnit.MILLISECONDS)//超时参数设置
                .observeOn(AndroidSchedulers.mainThread())//线程切换

//`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
fun <T> Observable<T>.bindLifecycle(owner: LifecycleOwner): ObservableSubscribeProxy<T> =
        this.`as`(AutoDispose
                .autoDisposable(AndroidLifecycleScopeProvider
                        .from(owner, Lifecycle.Event.ON_DESTROY)))//生命周期绑定

//endregion

