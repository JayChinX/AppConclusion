package com.qxj.socket

import org.apache.mina.core.filterchain.IoFilter
import org.apache.mina.core.future.ConnectFuture
import org.apache.mina.core.future.IoFuture
import org.apache.mina.core.service.IoConnector
import org.apache.mina.core.service.IoHandler
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.keepalive.KeepAliveFilter
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler
import org.apache.mina.filter.logging.LoggingFilter
import org.apache.mina.transport.socket.nio.NioSocketConnector
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.charset.Charset


class SocketClient private constructor(
    tag: String,
    private val config: SocketConfiguration
) : Thread(tag) {

    private var connector: IoConnector? = null

    private var future: ConnectFuture? = null

    private var session: IoSession? = null

    private var failCount = 0

    private val logger: Logger by lazy { LoggerFactory.getLogger(tag) }


    var running = false

    private var initing = false

    class Builder {

        companion object {
            internal var HEART_TIME = 3000
            internal var TIMEOUT = 15 * 1000
            internal var BOTH_IDLE = 10
        }

        private var name: String = "SocketClient"
        private var ip: String? = null
        private var port: Int? = null

        private var long: Boolean = true

        private var charsetName = "UTF-8"

        private lateinit var loggerIoFilterAdapter: IoFilter

        private lateinit var protocolCodecIoFilterAdapter: IoFilter

        private var received: Received? = null


        fun setTag(tag: String): Builder {
            this.name = tag
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

        fun setType(long: Boolean): Builder {
            this.long = long
            return this
        }

        fun setReceived(received: Received): Builder {
            this.received = received
            return this
        }

        fun builder(): SocketClient {
            if (ip == null || port == null) {
                throw Exception("ip 和 端口号 不能为空")
            }

            if (!::loggerIoFilterAdapter.isInitialized) {
                loggerIoFilterAdapter = createLogger()
            }

            if (!::protocolCodecIoFilterAdapter.isInitialized) {
                protocolCodecIoFilterAdapter = createCodec()
            }

            val configuration = if (!long) {
                SocketConfiguration(
                    ip!!, port!!,
                    loggerIoFilterAdapter,
                    null,
                    protocolCodecIoFilterAdapter,
                    createIoHandler(),
                    received,
                    false
                )
            } else {
                SocketConfiguration(
                    ip!!, port!!,
                    loggerIoFilterAdapter,
                    createHearBeat(),
                    protocolCodecIoFilterAdapter,
                    createIoHandler(),
                    received
                )
            }

            return SocketClient(name, configuration)
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

    internal data class SocketConfiguration(
        val ip: String,
        val port: Int,
        val loggerIoFilterAdapter: IoFilter,
        val heartBeat: IoFilter?,
        val protocolCodecIoFilterAdapter: IoFilter,
        val ioHandler: IoHandler,
        val received: Received?,
        val long: Boolean = true
    )


    override fun run() {
        super.run()
        conntection()
    }

    @Throws
    private fun conntection() {

        with(createConnector()) {
            initing = true
            try {
                future = initFuture(this)

                if (future == null) {
                    throw Exception("socket 连接创建失败, future获取null")
                }
                session = initSession(future)

                if (session == null) {
                    throw Exception("socket 连接创建失败, session获取null")
                }
                initing = false
                running = true
                if (!config.long && shortMsg != null) {
                    sendShortData(shortMsg!!)
                }
            } catch (e: Exception) {
                running = false

                logger.error("连接出错: {}", e.toString())
                //如果是短链接抛出异常 不进行重试操作
                if (!config.long) {
                    config.received?.parseData(Result.failure(IOException("socket error", e)))
                    return@with this
                }

                failCount++
                val time = (if (failCount > 10) 5000L else 3000L)
                sleep(time)
                logger.error("正在重试： {}", "第${failCount}次重试，时间间隔${time / 1000}s")
                conntection()
            }

            return@with this
        }
    }

    fun init() {

    }

    private fun createConnector(): IoConnector {
        return connector ?: NioSocketConnector()
            .apply {
                logger.info("初始化 connector : {}", "ip: ${config.ip}, port: ${config.port} 初始化 connector")
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
                sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, Builder.BOTH_IDLE)

                //设置链接超时时间
                connectTimeoutMillis = (Builder.TIMEOUT).toLong()

                //设置消息拦截器
                val ioHandler = config.ioHandler
                handler = ioHandler

                val socketAddress = InetSocketAddress(config.ip, config.port)

                setDefaultRemoteAddress(socketAddress)

                addListener(this)
                connector = this
            }

    }

    @Throws
    private fun initFuture(connector: IoConnector): ConnectFuture {
        return connector.let {
            val future = it.connect()
            // 等待连接创建完成
            future.awaitUninterruptibly()
        }

    }

    @Throws
    private fun initSession(future: IoFuture?): IoSession? {
        return future?.let {
            //开始连接
            val session = future.session// 获得session
            if (session != null && session.isConnected) {
                //连接成功
                logger.info("连接成功: {}", "ip: ${config.ip}, port: ${config.port}")
                return@let session
            } else {
                //连接失败
                logger.error("连接失败: {}", "ip: ${config.ip}, port: ${config.port}")
                return@let null
            }
        }
    }

    fun stopSession() {

        if (session != null && session!!.isConnected) {
            session!!.closeFuture.awaitUninterruptibly()// 等待连接断开
            connector?.dispose()//彻底释放Session,退出程序时调用不需要重连的可以调用这句话，也就是短连接不需要重连。长连接不要调用这句话，注释掉就OK。
        }
    }


    private fun addListener(connector: IoConnector) {
        // 监听客户端是否断线
        connector.addListener(object : IoListener() {
            override fun sessionDestroyed(arg0: IoSession) {
                super.sessionDestroyed(arg0)
                if (config.long) {
                    logger.info("尝试重连: {}", "ip: ${config.ip}, port: ${config.port}")
                    conntection()
                }

            }
        })
    }

    fun send(msg: String) {
        session?.let {
            val pack = Pack(content = msg)
            if (it.isConnected) it.write(pack)
        }
    }

    private var shortMsg: String? = null

    @Throws
    fun sendShortData(msg: String) {
        if (config.long) {
            throw Exception("不是短链接")
        }
        this.shortMsg = msg
        session?.let {

            val pack = Pack(content = msg)
            if (!it.isConnected && !initing) {
                conntection()
            } else {
                it.write(pack)
            }
            return@let
        }

    }

    /**
     * 关闭Mina长连接 **
     */
    fun close() {
        logger.info("关闭重连: {}", "close()")
        session?.close(false)

        future?.cancel()
        connector?.let {
            if (!it.isDisposed) {
                //清空里面注册的所以过滤器
                it.filterChain.clear()
                it.dispose()
            }
        }

        logger.info("连接已关闭: {}")
    }


}
