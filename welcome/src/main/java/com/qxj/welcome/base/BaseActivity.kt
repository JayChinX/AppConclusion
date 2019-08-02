package com.qxj.welcome.base

import android.os.Bundle
import com.qxj.commonbase.mvvm.BaseActivity
import com.qxj.welcome.ui.setStatusBarColor
import com.qxj.welcome.utilities.injectARouter

abstract class BaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(this)
    }

    override fun initView() {
        injectARouter()
    }
}