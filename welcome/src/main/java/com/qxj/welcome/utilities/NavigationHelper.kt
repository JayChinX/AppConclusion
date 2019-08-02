package com.qxj.welcome.utilities

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.facade.service.PathReplaceService
import com.alibaba.android.arouter.facade.service.SerializationService
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.qxj.welcome.BuildConfig
import java.lang.reflect.Type


fun Application.initARouter() {
    if (BuildConfig.DEBUG) {
        ARouter.openLog()//打印日志
        ARouter.openDebug()//开启调试模式
        Log.d("BaseApp", "ARouter open Log and Debug")
    }
    ARouter.init(this)
    Log.d("BaseApp", "ARouter init")
}

fun Activity.injectARouter() {
    ARouter.getInstance().inject(this)
}

fun Fragment.injectARouter() {
    ARouter.getInstance().inject(this)
}

fun getFragment(path: String) =
        ARouter.getInstance()
                .build(path).navigation() as Fragment

fun ViewModel.startActivity(path: String) {
    ARouter.getInstance().build(path).navigation()
}

/**
 * 路径拦截替换服务
 */
@Route(path = "/home/service/PathReplaceServiceImpl")
class PathReplaceServiceImpl : PathReplaceService {

    private val TAG = PathReplaceServiceImpl::class.java.simpleName

    lateinit var mContext: Context

    //
    override fun forString(path: String): String {
        Log.d(TAG, "此 path = $path 是否需要替换 false")
        return path//处理后返回的path
    }

    override fun forUri(uri: Uri): Uri {
        Log.d(TAG, "此 uri = $uri 是否需要变换 false")
        return uri//
    }

    override fun init(context: Context) {
        this.mContext = context
        Log.d(TAG, "路径替换拦截器初始化")
    }
}

@Route(path = "/home/service/json")
class JsonServiceImpl : SerializationService {
    override fun <T : Any?> json2Object(input: String?, clazz: Class<T>?): T {
        return JSON.parseObject(input, clazz)
    }

    override fun init(context: Context?) {

    }

    override fun object2Json(instance: Any?): String {
        return JSON.toJSONString(instance)
    }

    override fun <T : Any?> parseObject(input: String?, clazz: Type?): T {
        return JSON.parseObject(input, clazz)
    }

}

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

/**
 * 跳转失败，降级容错服务
 */
@Route(path = "/home/service/DegradeServiceImpl")//自定义全局降级策略
class DegradeServiceImpl : DegradeService {

    private val TAG = DegradeServiceImpl::class.java.simpleName

    private lateinit var mContext: Context

    override fun onLost(context: Context, postcard: Postcard) {
        Log.d(TAG, "跳转路径出错 ${postcard.path}")
    }

    override fun init(context: Context) {
        this.mContext = context
        Log.d(TAG, "降级拦截器初始化")
    }
}