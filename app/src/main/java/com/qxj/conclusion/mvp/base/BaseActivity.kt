package com.qxj.conclusion.mvp.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qxj.conclusion.AppConclusionActivity

abstract class BaseActivity : AppCompatActivity(), IView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        mPresenter.onCreate(intent)
        mPresenter.initLifecycle(this)
    }

    abstract fun getLayoutId() : Int

    override fun hideLoading() {
    }

    override fun onError() {
    }

    override fun showLoading() {
    }

}