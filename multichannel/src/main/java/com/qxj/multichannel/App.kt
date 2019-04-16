package com.qxj.multichannel

import com.qxj.commonbase.BaseApplication

class App : BaseApplication() {
    override fun registerModule(): List<String> {

        return emptyList()
    }

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}