package com.hymnal.welcome.base

import android.os.Bundle
import com.hymnal.base.mvvm.BaseActivity
import com.hymnal.welcome.ui.setStatusBarColor
import com.hymnal.welcome.utilities.injectARouter

abstract class BaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(this)
    }

    override fun initView() {
        injectARouter()
    }
}