package com.hymnal.welcome.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IService : IProvider {
    fun sayHello(context: Context)
}