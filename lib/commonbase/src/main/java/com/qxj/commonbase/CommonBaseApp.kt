package com.qxj.commonbase

import android.app.Application
import android.util.Log

class CommonBaseApp : ApplicationImpl {
    private val TAG = CommonBaseApp::class.java.simpleName

    companion object {
        lateinit var INSTANCE: Application
            private set
    }

    override fun onCreate(application: Application) {
        Log.d(TAG, "CommonBaseApp init")
        INSTANCE = application
    }

    override fun onDestroy() {
    }
}