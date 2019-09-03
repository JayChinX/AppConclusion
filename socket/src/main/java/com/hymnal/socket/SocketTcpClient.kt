package com.hymnal.socket

import org.apache.mina.core.service.IoConnector
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.transport.socket.nio.NioSocketConnector
import java.net.InetSocketAddress


internal class SocketTcpClient(tag: String, config: SocketConfiguration) : BaseSocketClient(tag, config) {


    override fun createConnector(): IoConnector {
        return connector ?: NioSocketConnector()
            .apply {
                logger.info("Connector init {}", "ip: ${config.ip}, port: ${config.port}")
                //添加过滤器
                val loggingFilter = config.loggerIoFilterAdapter
                filterChain.addLast("logger", loggingFilter)
                //编码解码格式
                val codec = config.protocolCodecIoFilterAdapter
                filterChain.addLast("codec", codec)
                //心跳包
                if (config.heartBeat != null) {
                    val heartBeat = config.heartBeat
                    filterChain.addLast("heartbeat", heartBeat)

                    sessionConfig.isKeepAlive = true
                }

                // 设置缓冲区大小
                sessionConfig.readBufferSize = 1024
                // 设置空闲时间
                sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, config.BOTH_IDLE)

                //设置链接超时时间
                connectTimeoutMillis = (config.TIMEOUT).toLong()

                //设置消息拦截器
                val ioHandler = MinaClientHandler(config.response) {
                    if (!config.long) close()
                }
                handler = ioHandler

                val socketAddress = InetSocketAddress(config.ip, config.port)

                setDefaultRemoteAddress(socketAddress)

                if (config.long) addListener(this)
                connector = this
            }

    }
}
