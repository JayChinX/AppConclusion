package com.qxj.commonsdk

import android.app.Application
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.commonbase.ApplicationImpl

class CommonSDKApp : ApplicationImpl {

    private val TAG = CommonSDKApp::class.java.simpleName

    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate(application: Application) {
        instance = application
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "ARouter open Log and Debug")
            ARouter.openLog()//打印日志
            ARouter.openDebug()//开启调试模式
        }
        Log.d(TAG, "ARouter init ")
        ARouter.init(application)
    }

    override fun onDestroy() {
        Log.d(TAG, "ARouter destroy ")
        ARouter.getInstance().destroy()
    }

}