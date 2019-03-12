package com.qxj.commonsdk.router

import android.content.Context
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/common/router/service", name = "CommonService")
class CommonService : IService {

    override fun init(context: Context?) {
    }

    override fun sayHello(context: Context) {
        Toast.makeText(context, "CommonService执行sayHello", Toast.LENGTH_SHORT).show()
    }
}