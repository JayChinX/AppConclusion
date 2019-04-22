package com.qxj.welcome.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route

import com.qxj.welcome.R
import com.qxj.welcome.base.BaseFragment
import com.qxj.welcome.utilities.InjectorUtils
import com.qxj.welcome.viewmodels.BlankViewModel

@Route(path = "/home/fragment/BlankFragment")
class BlankFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.blank_fragment

    override fun initView() {

    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        val factory = InjectorUtils.provideBlankViewModelFactory()
        ViewModelProviders.of(this, factory)
                .get(BlankViewModel::class.java)
    }

    override fun subscribeUi() {


    }

}
