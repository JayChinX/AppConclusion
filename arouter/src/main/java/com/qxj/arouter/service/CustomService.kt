package com.qxj.arouter.service

import android.app.Service
import android.content.Context
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/service/hello", name = "testService")
class CustomService : IService {
    override fun sayHello(context: Context) {
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
    }

    override fun init(context: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}