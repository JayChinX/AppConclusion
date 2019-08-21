package com.qxj.socket

import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.slf4j.LoggerFactory

// 类继承handler
internal class ServerHandler : IoHandlerAdapter() {

    @Throws(Exception::class)
    override fun messageReceived(session: IoSession?, message: Any?) {
        val msg = message.toString()
        logger.info("服务端接收" + session!!.id + "消息成功：" + msg)
        val content = mapOf(
            "success" to "ok"
        )
        //向客户端写入数据
        val future = session.write(content.toString())
        //在100毫秒内写完
        future.awaitUninterruptibly(100)
        if (future.isWritten) {

        } else {

        }
    }

    @Throws(Exception::class)
    override fun messageSent(session: IoSession?, message: Any?) {
        val msg = message.toString()
        logger.info("服务端发送" + session!!.id + "消息成功：" + msg)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(session: IoSession?, cause: Throwable) {
        cause.printStackTrace()
        logger.error("服务端" + session!!.id + "处理消息异常：" + cause)
    }

    @Throws(Exception::class)
    override fun sessionIdle(session: IoSession?, status: IdleStatus?) {
        logger.error("服务端" + session!!.id + "进入空闲状态")
    }

    @Throws(Exception::class)
    override fun sessionCreated(session: IoSession?) {
        logger.info("服务端与" + session!!.id + "用户连接")
        super.sessionCreated(session)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ServerHandler::class.java)
    }
}
