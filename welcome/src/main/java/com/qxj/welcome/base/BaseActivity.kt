package com.qxj.welcome.base

import android.os.Bundle
import com.qxj.commonbase.mvvm.BaseActivity
import com.qxj.welcome.activity.setStatusBarColor
import com.qxj.welcome.init.startInit
import com.qxj.welcome.utilities.Navigation

abstract class BaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(this)
    }

    override fun initView() {
        startInit(this)
    }
}