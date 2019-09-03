package com.hymnal.welcome.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.hymnal.welcome.R
import com.hymnal.welcome.base.BaseActivity

@Route(path = "/home/activity/CustomGroupActivity", group = "customGroup")
class CustomGroupActivity : BaseActivity() {
    override fun initView() {
    }

    override fun getLayoutId(): Int = R.layout.activity_custom_group

    override fun subscribeUi() {


    }
}
