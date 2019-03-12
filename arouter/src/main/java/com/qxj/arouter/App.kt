package com.qxj.arouter

import com.qxj.commonbase.BaseApplication

class App : BaseApplication() {

    companion object {
        lateinit var INSTANCE: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}