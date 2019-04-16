package com.qxj.welcome

import android.app.Application
import android.util.Log
import com.qxj.commonbase.ApplicationImpl

class AppWelcome : ApplicationImpl {
    private val TAG = AppWelcome::class.java.simpleName

    companion object {
        lateinit var INSTANCE: Application
            private set
    }

    override fun onCreate(application: Application) {
        Log.d(TAG, "AppWelcome init")
        INSTANCE = application
    }

    override fun onDestroy() {

    }
}