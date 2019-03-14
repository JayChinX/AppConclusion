package com.qxj.commonsdk

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.commonbase.ApplicationImpl
import com.qxj.commonbase.BaseApplication
import com.qxj.commonsdk.router.InitializeService

class CommonSDKApp : ApplicationImpl {
    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate(application: BaseApplication) {
        instance = application
        ARouter.openLog()//打印日志
        ARouter.openDebug()//开启调试模式
        ARouter.init(application)
//        InitializeService.startActionInit(application.applicationContext, null, null)
    }
}