package com.qxj.commonbase.mvvm

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        subscribeUi()
    }

    abstract fun initView()

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun subscribeUi()

}