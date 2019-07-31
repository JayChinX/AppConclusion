package com.qxj.socket

import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal class MinaClientHandler(private val received: Received?, private val long: Boolean = true) : IoHandlerAdapter() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(MinaClientHandler::class.java) }

    @Throws(Exception::class)
    override fun exceptionCaught(session: IoSession?, cause: Throwable) {
        logger.info("ERROR: {}", cause)
        super.exceptionCaught(session, cause)
    }

    @Throws(Exception::class)
    override fun messageReceived(session: IoSession?, message: Any?) {
        //数据交互 接收到的信息
        logger.info("RECEIVED: {}", message)
        val msg = (message as Pack).content
        received?.parseData(Result.success(msg.toString()))
        if (!long) session?.closeNow()//短连接
        super.messageReceived(session, message)
    }

    @Throws(Exception::class)
    override fun messageSent(session: IoSession?, message: Any?) {
        logger.info("SEND: {}", message)
        super.messageSent(session, message)
    }

    override fun sessionClosed(session: IoSession?) {
        logger.info("CLOSED: {}", "会话关闭")
        super.sessionClosed(session)
    }

    override fun sessionCreated(session: IoSession?) {
        logger.info("CREATED: {}", "会话创建")
        super.sessionCreated(session)
    }

    override fun sessionIdle(session: IoSession?, status: IdleStatus?) {
        logger.info("IDLE: {}", "会话休眠")
        super.sessionIdle(session, status)
    }

    override fun sessionOpened(session: IoSession?) {
        logger.info("OPENED: {}", "会话打开")
        super.sessionOpened(session)
    }

}
