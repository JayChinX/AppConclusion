package com.qxj.welcome.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.welcome.BaseActivity
import com.qxj.welcome.R
import com.qxj.welcome.utils.Author

@Route(path = "/home/activity/SecondActivity")
class SecondActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        //获取自定义参数
        val service: SerializationService =
                ARouter.getInstance().navigation(SerializationService::class.java)
        val a = service.json2Object(intent.getStringExtra("author"), Author::class.java)
    }
}
