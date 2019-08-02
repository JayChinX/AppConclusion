package com.qxj.welcome.base

import android.app.Application
import android.util.Log
import com.qxj.commonbase.ApplicationImpl

class App : ApplicationImpl {
    private val TAG = App::class.java.simpleName

    companion object {
        lateinit var INSTANCE: Application
            private set
    }

    override fun onCreate(application: Application) {
        Log.d(TAG, "App init")
        INSTANCE = application
    }

    override fun onDestroy() {

    }
}