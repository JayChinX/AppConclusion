package com.qxj.welcome.base

import com.qxj.commonbase.mvvm.BaseFragment
import com.qxj.welcome.utilities.injectARouter

abstract class BaseFragment : BaseFragment() {

    override fun initView() {
        injectARouter()
    }
}