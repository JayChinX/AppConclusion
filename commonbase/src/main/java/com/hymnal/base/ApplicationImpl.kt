package com.hymnal.base

import android.app.Application

interface ApplicationImpl {
    fun onCreate(application: Application)
    fun onDestroy()
}