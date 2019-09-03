package com.hymnal.welcome.base

import android.app.Application
import android.util.Log
import com.hymnal.base.ApplicationImpl

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