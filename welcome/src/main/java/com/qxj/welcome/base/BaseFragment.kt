package com.qxj.welcome.base

import com.qxj.commonbase.mvvm.BaseFragment
import com.qxj.welcome.init.startInit

abstract class BaseFragment : BaseFragment() {

    override fun initView() {
        startInit(this)
    }
}