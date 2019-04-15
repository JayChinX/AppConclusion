package com.qxj.commonbase

import android.app.Application

interface ApplicationImpl {
    fun onCreate(application: Application)
    fun onDestroy()
}