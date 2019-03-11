package com.zzdc.emulationcard

import com.qxj.commonbase.BaseApplication

class App : BaseApplication() {

    private val TAG = App::class.java.simpleName

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}