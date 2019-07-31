package com.qxj.socket

import org.apache.mina.core.filterchain.IoFilter
import org.apache.mina.core.service.IoHandler

data class SocketConfiguration(
    val ip: String,
    val port: Int,
    val loggerIoFilterAdapter: IoFilter,
    val heartBeat: IoFilter?,
    val protocolCodecIoFilterAdapter: IoFilter,
    val ioHandler: IoHandler,
    val received: Received?,
    val long: Boolean = true
) {

    internal var HEART_TIME = 3000
    internal var TIMEOUT = 15 * 1000
    internal var BOTH_IDLE = 10
}