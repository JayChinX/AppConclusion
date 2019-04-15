package com.qxj.commonbase

import android.app.Application
import android.util.Log

open class BaseApplication : Application() {
    private val TAG = BaseApplication::class.java.simpleName
    override fun onCreate() {
        super.onCreate()
        modulesApplicationInit()
    }

    private fun modulesApplicationInit() {
        for (appImpl in CommonConfig.COMMONSLIST) {
            try {
                val appClass = Class.forName(appImpl)
                val obj = appClass.newInstance()
                if (obj is ApplicationImpl) {
                    obj.onCreate(this)
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } finally {
                Log.d(TAG, "modulesApplicationInit")
            }

        }
    }

    override fun onTerminate() {
        super.onTerminate()
        for (appImpl in CommonConfig.COMMONSLIST) {
            try {
                val appClass = Class.forName(appImpl)
                val obj = appClass.newInstance()
                if (obj is ApplicationImpl) {
                    obj.onDestroy()
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } finally {
                Log.d(TAG, "modulesApplicationInit")
            }

        }
    }


}