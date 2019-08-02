package com.qxj.welcome.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.qxj.welcome.R
import com.qxj.welcome.base.BaseActivity

@Route(path = "/home/activity/CustomGroupActivity", group = "customGroup")
class CustomGroupActivity : BaseActivity() {
    override fun initView() {
    }

    override fun getLayoutId(): Int = R.layout.activity_custom_group

    override fun subscribeUi() {


    }
}
