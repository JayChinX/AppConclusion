package com.hymnal.socket

import org.apache.mina.core.service.IoService
import org.apache.mina.core.service.IoServiceListener
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal open class IoListener : IoServiceListener {
    private val logger: Logger by lazy { LoggerFactory.getLogger(IoListener::class.java) }

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
        logger.info("服务闲置")

    }

    @Throws(Exception::class)
    override fun sessionClosed(arg0: IoSession) {
        //关闭
        logger.info("连接关闭")

    }

    @Throws(Exception::class)
    override fun sessionCreated(arg0: IoSession) {
        //IoService创建服务
        logger.info("连接创建")

    }

    @Throws(Exception::class)
    override fun sessionDestroyed(arg0: IoSession) {
        //IoService销毁服务
        logger.info("连接销毁")

    }
}
