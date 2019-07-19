package com.qxj.socket

import android.util.Log


import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IoSession

internal class ClientHandler : IoHandlerAdapter() {
    private val TAG = ClientHandler::class.java.simpleName

    private var receivedData: Received? = null

    fun setReceive(received: Received) {
        this.receivedData = received
    }

    @Throws(Exception::class)
    override fun exceptionCaught(session: IoSession?, cause: Throwable) {
        Log.i(TAG, "mina error , to connect createSession mina")
        super.exceptionCaught(session, cause)
    }

    @Throws(Exception::class)
    override fun messageReceived(session: IoSession?, message: Any?) {
        //数据交互 接收到的信息
        val msg = message.toString()
        Log.i(TAG, "mina received :$msg")
        receivedData?.parseData(msg)
        super.messageReceived(session, message)
    }

    @Throws(Exception::class)
    override fun messageSent(session: IoSession?, message: Any?) {
        Log.i(TAG, "mina sent :${message.toString()}")
        //        session.close(false);//true为短连接
        super.messageSent(session, message)

    }

}
