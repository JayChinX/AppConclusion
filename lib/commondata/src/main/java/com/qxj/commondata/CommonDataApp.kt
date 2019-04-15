package com.qxj.commondata

import android.app.Application
import com.qxj.commonbase.ApplicationImpl

class CommonDataApp : ApplicationImpl {

    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate(application: Application) {
        instance = application
    }

    override fun onDestroy() {
    }
}