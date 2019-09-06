package com.hymnal.welcome.base

import com.hymnal.base.mvvm.BaseFragment
import com.hymnal.welcome.utilities.injectARouter

abstract class BaseFragment : BaseFragment() {

    override fun init() {
        context?.injectARouter(this)
    }
}