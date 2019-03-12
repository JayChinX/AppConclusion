package com.qxj.commonsdk

import android.app.Application
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
        InitializeService.startActionInit(application, null, null)
    }
}