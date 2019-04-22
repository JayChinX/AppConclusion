package com.qxj.welcome.utilities

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor

@Interceptor(priority = 1, name = "分享组件拦截器")//priority拦截器优先级，值越小，优先级越高
class InterceptorImpl : IInterceptor {

    private val TAG = InterceptorImpl::class.java.simpleName
    companion object {
        var isRegister: Boolean = true
    }

    private lateinit var mContext: Context

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        if (isRegister) {
            Log.e(TAG, "拦截器拦截到了${postcard.path}")
            //拦截到后继续往下走
            callback.onContinue(postcard)
        } else if ("/share/shareBook" == postcard.path
                || "/share/shareMagazine" == postcard.path) {
            //拦截到后不在往下走
            Log.e(TAG, "拦截器shareBook shareMagazine")
            callback.onInterrupt(null)//中断跳转
            //在拦截器的process()方法中，
            // 如果你即没有调用callback.onContinue(postcard)方法
            // 也没有调用callback.onInterrupt(exception)方法，
            // 那么不再执行后续的拦截器，需等待300s（默认值，可设置改变）的时间，才能抛出拦截器中断
            //拦截器的process()方法以及带跳转的回调中的onInterrupt(Postcard postcard)方法，
            // 均是在分线程中执行的，如果需要做一些页面的操作显示，必须在主线程中执行

        }
    }


    override fun init(context: Context) {
        this.mContext = context
        Log.d(TAG, "拦截器初始化")
    }
}