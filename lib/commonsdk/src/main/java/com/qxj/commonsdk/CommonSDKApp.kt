package com.qxj.commonsdk

import android.app.Application
import android.util.Log
import com.qxj.commonbase.ApplicationImpl

class CommonSDKApp : ApplicationImpl {

    private val TAG = CommonSDKApp::class.java.simpleName

    companion object {
        lateinit var INSTANCE: Application
            private set
    }

    override fun onCreate(application: Application) {
        Log.d(TAG, "CommonSDKApp init")
        INSTANCE = application
    }

    override fun onDestroy() {
    }

}