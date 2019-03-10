package com.qxj.commonbase.mvvm

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

abstract class BaseActivity<VB: ViewDataBinding>: AppCompatActivity(), Presenter {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<VB>(this, getLayoutId())
        initView()
    }

    override fun onStart() {
        super.onStart()
        initData()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    abstract override fun onClick(v: View?)
}