package com.qxj.welcome.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.qxj.welcome.BaseActivity
import com.qxj.welcome.R

@Route(path = "/home/activity/CustomGroupActivity", group = "customGroup")
class CustomGroupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_group)
    }
}
