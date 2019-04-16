package com.qxj.commonbase

import android.app.Application
import android.util.Log

abstract class BaseApplication : Application() {
    private val TAG = BaseApplication::class.java.simpleName
    override fun onCreate() {
        super.onCreate()
        val modulesApplicationImplPackage = registerModule()
        modulesApplicationInit(modulesApplicationImplPackage)
    }

    abstract fun registerModule() : List<String>

    private fun modulesApplicationInit(packageName: List<String>) {
        Log.d(TAG, "modules Application Init start")
        try {
            for (appImpl in packageName) {
                val appClass = Class.forName(appImpl)
                val obj = appClass.newInstance()
                if (obj is ApplicationImpl) {
                    obj.onCreate(this)
                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Log.e(TAG, " ${e.message}")
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } finally {
            Log.d(TAG, "modules Application Init end")
        }


    }

    override fun onTerminate() {
        super.onTerminate()

    }
}