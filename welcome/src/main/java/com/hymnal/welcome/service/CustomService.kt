package com.hymnal.welcome.service

import android.content.Context
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/home/service/hello", name = "testService")
class CustomService : IService {
    override fun sayHello(context: Context) {
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
    }

    override fun init(context: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}