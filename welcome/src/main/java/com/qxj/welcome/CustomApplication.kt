package com.qxj.welcome

import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.commonbase.BaseApplication
import com.qxj.commonbase.CommonBaseApp
import com.qxj.commondata.CommonDataApp
import com.qxj.commonsdk.CommonSDKApp

class CustomApplication : BaseApplication() {

    private val TAG = CustomApplication::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()//打印日志
            ARouter.openDebug()//开启调试模式
            Log.d(TAG, "ARouter open Log and Debug")
        }
        ARouter.init(this)
        Log.d(TAG, "ARouter init")
    }

    override fun registerModule(): List<String> {
        return arrayListOf(AppWelcome::class.java.name,
                CommonBaseApp::class.java.name,
                CommonDataApp::class.java.name,
                CommonSDKApp::class.java.name)
    }
}