package com.qxj.commonsdk.router

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IService : IProvider {
    /**
     * 发现服务功能
     *
     * 这里的服务是接口开发的概念，就是将一部分功能和组件封装起来成为接口，以接口的形式对外提供能力，
     * 所以在这部分就可以将每个功能作为一个服务，而服务的实现就是具体的业务功能。
     * 发现服务这个功能的特点在于，我们只需要知道接口，不需要关心接口的实现类，很好了实现了解耦
     */
    fun sayHello(context: Context)
}