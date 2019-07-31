package com.qxj.socket

import org.apache.mina.core.filterchain.IoFilter
import org.apache.mina.core.future.ConnectFuture
import org.apache.mina.core.future.IoFuture
import org.apache.mina.core.service.IoConnector
import org.apache.mina.core.service.IoHandler
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.keepalive.KeepAliveFilter
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler
import org.apache.mina.filter.logging.LoggingFilter
import java.nio.charset.Charset

interface SocketClient {

    fun conntection()

    fun init()

    fun createConnector(): IoConnector

    @Throws
    fun initFuture(connector: IoConnector): ConnectFuture {
        return connector.let {
            val future = it.connect()
            // 等待连接创建完成
            future.awaitUninterruptibly()
        }
    }

    @Throws
    fun initSession(future: IoFuture?): IoSession? {
        return future?.let {
            //开始连接
            val session = future.session// 获得session
            if (session != null && session.isConnected) {
                return@let session
            } else {
                return@let null
            }
        }
    }

    fun stopSession()

    fun addListener(connector: IoConnector) {
        // 监听客户端是否断线
        connector.addListener(object : IoListener() {
            override fun sessionDestroyed(arg0: IoSession) {
                super.sessionDestroyed(arg0)
                conntection()
            }
        })
    }

    fun send(msg: String)

    @Throws
    fun sendShortData(msg: String)

    fun getIsAliveThread(): Boolean
    fun startThread()

    /**
     * 关闭Mina长连接 **
     */
    fun close()

    enum class Type {
        TCP,
        UDP
    }

    class Builder {

        private var type: Type = Type.TCP

        private var HEART_TIME = 3000
        private var TIMEOUT = 15 * 1000
        private var BOTH_IDLE = 10

        private var tag: String = "SocketClient"
        private lateinit var ip: String
        private var port: Int = 0

        private var long: Boolean = true

        private var charsetName = "UTF-8"

        private lateinit var loggerIoFilterAdapter: IoFilter

        private var hearBeat: IoFilter? = null

        private lateinit var protocolCodecIoFilterAdapter: IoFilter

        private lateinit var ioHandler: IoHandler

        private var received: Received? = null


        fun setType(type: Type, long: Boolean = true): Builder {
            this.type = type
            this.long = long
            return this
        }

        fun setTag(tag: String): Builder {
            this.tag = tag
            return this
        }

        fun setIp(ip: String, port: Int): Builder {
            this.ip = ip
            this.port = port
            return this
        }

        fun setTime(timeout: Int, idle: Int = 10): Builder {
            TIMEOUT = timeout
            BOTH_IDLE = idle
            return this
        }

        fun setLogFilter(logFilter: IoFilter): Builder {
            this.loggerIoFilterAdapter = logFilter
            return this
        }

        fun setCodecFilter(codecFilter: IoFilter): Builder {
            this.protocolCodecIoFilterAdapter = codecFilter
            return this
        }

        fun setReceived(received: Received): Builder {
            this.received = received
            return this
        }

        fun builder(): SocketClient {
            if (!::ip.isInitialized) {
                throw Exception("IP和端口号为必填项")
            }

            if (!::loggerIoFilterAdapter.isInitialized) {
                this.loggerIoFilterAdapter = createLogger()
            }

            if (long) hearBeat = createHearBeat()

            if (!::protocolCodecIoFilterAdapter.isInitialized) {
                this.protocolCodecIoFilterAdapter = createCodec()
            }

            if (!::ioHandler.isInitialized) {
                this.ioHandler = createIoHandler()
            }

            val configuration = SocketConfiguration(
                ip, port,
                loggerIoFilterAdapter,
                hearBeat,
                protocolCodecIoFilterAdapter,
                ioHandler,
                received,
                long
            ).apply {
                TIMEOUT = 15 * 1000
                BOTH_IDLE = 10
            }

            return when(type) {
                Type.TCP -> SocketTcpClient(tag, configuration)
                Type.UDP -> SocketUdpClient(tag, configuration)
            }
        }


        private fun createLogger(): IoFilter {
            return LoggingFilter()
        }

        private fun createCodec(): IoFilter {
//            val factory = TextLineCodecFactory(
//                Charset.forName("UTF-8"),
//                LineDelimiter.WINDOWS.value,
//                LineDelimiter.WINDOWS.value
//            )
//            factory.decoderMaxLineLength = 1024 * 1024
//            factory.encoderMaxLineLength = 1024 * 1024

            val factory = ProtocolCodecImplFactory(Charset.forName(charsetName))
            return ProtocolCodecFilter(factory)
        }

        private fun createHearBeat(): IoFilter {
            //设置心跳工程
            val heartBeatFactory = KeepAliveMessageFactoryImpl()
            //当读操作空闲时发送心跳
            val heartBeat = KeepAliveFilter(heartBeatFactory)
            //设置心跳包请求后超时无反馈情况下的处理机制，默认为关闭连接,在此处设置为输出日志提醒
            heartBeat.requestTimeoutHandler = KeepAliveRequestTimeoutHandler.LOG
            //是否回发
            heartBeat.isForwardEvent = false
            //发送频率
            heartBeat.requestInterval = HEART_TIME
            //设置心跳包请求后 等待反馈超时时间。 超过该时间后则调用KeepAliveRequestTimeoutHandler.CLOSE */
            heartBeat.requestTimeout = TIMEOUT
            return heartBeat
        }

        private fun createIoHandler(): IoHandler {
            return MinaClientHandler(received, long)
        }
    }
}