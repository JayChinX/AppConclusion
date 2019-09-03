package com.hymnal.welcome.debug

import android.util.Log
import com.hymnal.base.BaseApplication
import com.hymnal.base.CommonBaseApp
import com.hymnal.welcome.base.App
import com.hymnal.welcome.utilities.initARouter

class App : BaseApplication() {

    private val TAG = App::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        initARouter()
        Log.d(TAG, "ARouter init")
    }

    override fun registerModule(): List<String> {
        return arrayListOf(
            App::class.java.name,
                CommonBaseApp::class.java.name)
    }
}