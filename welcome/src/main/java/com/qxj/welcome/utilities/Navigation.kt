package com.qxj.welcome.utilities

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.welcome.BuildConfig

/**
 * ARouter 的代理类
 */
class Navigation {

    companion object {
        @Volatile
        private var instance: Navigation? = null
        fun getInstance() =
                instance ?: synchronized(this) {
                    instance
                            ?: Navigation().also {
                        instance = it
                    }
                }
    }

    fun init(application: Application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()//打印日志
            ARouter.openDebug()//开启调试模式
            Log.d("BaseApp", "ARouter open Log and Debug")
        }
        ARouter.init(application)
        Log.d("BaseApp", "ARouter init")
    }

    fun inject(thiz: Any) {
        ARouter.getInstance().inject(thiz)
    }

    fun getFragment(path: String) =
            ARouter.getInstance()
                    .build(path).navigation() as Fragment

    fun toOtherActivity(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

}