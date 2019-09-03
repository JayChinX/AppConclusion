package com.hymnal.qin

import com.hymnal.base.BaseApplication
import com.hymnal.base.CommonBaseApp
import com.hymnal.welcome.base.App
import com.hymnal.welcome.utilities.initARouter

class BaseApp : BaseApplication() {
    //
    companion object {
        lateinit var instance: BaseApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initARouter()
    }

    override fun registerModule(): List<String> {
        return listOf(
            App::class.java.name,
            CommonBaseApp::class.java.name
        )
    }
}