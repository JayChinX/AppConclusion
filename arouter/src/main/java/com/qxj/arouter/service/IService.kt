package com.qxj.arouter.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IService : IProvider {
    fun sayHello(context: Context)
}