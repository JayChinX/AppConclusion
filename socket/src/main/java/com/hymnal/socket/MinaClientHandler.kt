package com.hymnal.socket

import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession

internal class MinaClientHandler(private val response: Response?, private val run: () -> Unit) : IoHandlerAdapter() {

    @Throws(Exception::class)
    override fun exceptionCaught(session: IoSession?, cause: Throwable) {
        super.exceptionCaught(session, cause)
    }

    @Throws(Exception::class)
    override fun messageReceived(session: IoSession?, message: Any?) {
        //数据交互 接收到的信息
        response?.response(Result.success(message.toString()))
        run()
        super.messageReceived(session, message)
    }

    @Throws(Exception::class)
    override fun messageSent(session: IoSession?, message: Any?) {
        super.messageSent(session, message)
    }

    override fun sessionClosed(session: IoSession?) {
        super.sessionClosed(session)
    }

    override fun sessionCreated(session: IoSession?) {
        super.sessionCreated(session)
    }

    override fun sessionIdle(session: IoSession?, status: IdleStatus?) {
        super.sessionIdle(session, status)
    }

    override fun sessionOpened(session: IoSession?) {
        super.sessionOpened(session)
    }

}
