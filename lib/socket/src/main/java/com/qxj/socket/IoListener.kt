package com.qxj.socket

import android.util.Log

import org.apache.mina.core.service.IoService
import org.apache.mina.core.service.IoServiceListener
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession

internal class IoListener : IoServiceListener {
    private val TAG = IoListener::class.java.simpleName


    @Throws(Exception::class)
    override fun serviceActivated(arg0: IoService) {
        //IoService激活一个服务

    }

    @Throws(Exception::class)
    override fun serviceDeactivated(arg0: IoService) {
        //IoService释放服务

    }

    @Throws(Exception::class)
    override fun serviceIdle(arg0: IoService, arg1: IdleStatus) {
        //一个服务闲置
        Log.d(TAG, "链接闲置")

    }

    @Throws(Exception::class)
    override fun sessionClosed(arg0: IoSession) {
        //关闭
        Log.d(TAG, "链接关闭")

    }

    @Throws(Exception::class)
    override fun sessionCreated(arg0: IoSession) {
        //IoService创建服务
        Log.d(TAG, "创建一个新链接")

    }

    @Throws(Exception::class)
    override fun sessionDestroyed(arg0: IoSession) {
        //IoService销毁服务
        Log.d(TAG, "链接销毁")

    }
}
