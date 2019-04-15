package com.qxj.arouter.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.arouter.R
import com.qxj.arouter.utils.Author

@Route(path = "/com/SecondActivity")
class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        //获取自定义参数
        val service: SerializationService =
                ARouter.getInstance().navigation(SerializationService::class.java)
        val a = service.json2Object(intent.getStringExtra("author"), Author::class.java)
    }
}
