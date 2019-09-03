package com.hymnal.qin

import com.hymnal.base.mvvm.BaseActivity
import com.hymnal.welcome.utilities.navActivity

class MainActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_main

    override fun subscribeUi() {
        navActivity("/home/activity/HomeActivity")
    }


    override fun initView() {

    }

}
