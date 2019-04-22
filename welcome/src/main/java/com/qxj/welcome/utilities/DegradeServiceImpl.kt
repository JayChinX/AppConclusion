package com.qxj.welcome.utilities

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService

/**
 * 跳转失败，降级容错服务
 */
@Route(path = "/home/service/DegradeServiceImpl")//自定义全局降级策略
class DegradeServiceImpl : DegradeService {

    private val TAG = DegradeServiceImpl::class.java.simpleName

    private lateinit var mContext: Context

    override fun onLost(context: Context, postcard: Postcard) {
        Log.d(TAG, "跳转路径出错 ${postcard.path}")
    }

    override fun init(context: Context) {
        this.mContext = context
        Log.d(TAG, "降级拦截器初始化")
    }
}