package com.hymnal.socket

import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory

internal class KeepAliveMessageFactoryImpl : KeepAliveMessageFactory {
    override fun isRequest(ioSession: IoSession, o: Any): Boolean {
        return false
    }

    override fun isResponse(ioSession: IoSession, o: Any): Boolean {
        return false
    }

    override fun getRequest(ioSession: IoSession): Any? {
        return null
    }

    override fun getResponse(ioSession: IoSession, o: Any): Any? {
        return null
    }
}
