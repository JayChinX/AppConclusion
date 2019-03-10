package com.qxj.commondata

import android.app.Application
import com.qxj.commonbase.ApplicationImpl
import com.qxj.commonbase.BaseApplication

class CommonDataApp : ApplicationImpl {
    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate(application: BaseApplication) {
        instance = application
    }
}