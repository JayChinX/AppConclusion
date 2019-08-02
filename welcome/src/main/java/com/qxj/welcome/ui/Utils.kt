package com.qxj.welcome.ui

import android.app.Activity
import android.os.Build
import android.view.View

fun setStatusBarColor(activity: Activity) {


    //Android6.0（API 23）以上，系统方法
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val window = activity.window
//            window.statusBarColor = activity.resources.getColor(colorId)

        val window = activity.window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


    }
}