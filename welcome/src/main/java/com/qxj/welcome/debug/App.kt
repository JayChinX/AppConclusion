package com.qxj.welcome.debug

import android.util.Log
import com.qxj.commonbase.BaseApplication
import com.qxj.commonbase.CommonBaseApp
import com.qxj.welcome.base.App
import com.qxj.welcome.utilities.initARouter

class App : BaseApplication() {

    private val TAG = App::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        initARouter()
        Log.d(TAG, "ARouter init")
    }

    override fun registerModule(): List<String> {
        return arrayListOf(App::class.java.name,
                CommonBaseApp::class.java.name)
    }
}