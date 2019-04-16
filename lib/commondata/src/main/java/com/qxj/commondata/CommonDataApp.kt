package com.qxj.commondata

import android.app.Application
import android.util.Log
import com.qxj.commonbase.ApplicationImpl

class CommonDataApp : ApplicationImpl {

    private val TAG = CommonDataApp::class.java.simpleName
    companion object {
        lateinit var INSTANCE: Application
            private set
    }

    override fun onCreate(application: Application) {
        Log.d(TAG, "CommonDataApp init")
        INSTANCE = application
    }

    override fun onDestroy() {
    }
}