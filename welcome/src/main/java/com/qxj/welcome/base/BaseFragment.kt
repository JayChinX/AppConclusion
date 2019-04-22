package com.qxj.welcome.base

import android.os.Bundle
import com.qxj.commonbase.mvvm.BaseFragment
import com.qxj.welcome.utilities.Navigation

abstract class BaseFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Navigation.getInstance().inject(this)
    }
}