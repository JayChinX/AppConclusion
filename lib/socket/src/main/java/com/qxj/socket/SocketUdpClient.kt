package com.qxj.socket

import org.apache.mina.core.future.ConnectFuture
import org.apache.mina.core.service.IoConnector
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.apache.mina.transport.socket.nio.NioDatagramConnector
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress


internal class SocketUdpClient(tag: String, private val config: SocketConfiguration) : Thread(tag), SocketClient {
    private val logger: Logger by lazy { LoggerFactory.getLogger(tag) }

    private var connector: IoConnector? = null

    private var future: ConnectFuture? = null

    private var session: IoSession? = null

    private var failCount = 0

    override fun run() {
        super.run()
        conntection()
    }

    @Throws
    override fun conntection() {

        with(createConnector()) {
            try {
                future = initFuture(this)

                if (future == null) {
                    throw ConnectorException("Connector error future is null")
                }
                session = initSession(future)

                if (session == null) {
                    throw ConnectorException("Connector error session is null")
                }
                send(message)
            } catch (e: RuntimeException) {
                logger.error("Connector error: {}", e.toString())

                failCount++

                //如果是短链接或者重试次数过多抛出异常 不进行重试操作
                if (failCount > 10 || !config.long) {
                    config.response?.response(Result.failure(SocketException("Socket error, $e")))
                    return@with this
                }
                val time = 1500L
                sleep(time)
                logger.error("Connector retry： {}", failCount)
                conntection()
            }

            return@with this
        }
    }

    override fun init() {
        failCount = 0
    }

    override fun createConnector(): IoConnector {
        return connector ?: NioDatagramConnector()
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
                }

                // 设置缓冲区大小
                sessionConfig.readBufferSize = 1024
                // 设置空闲时间
                sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, config.BOTH_IDLE)
                //设置每一个非主监听连接的端口可以重用
                sessionConfig.isReuseAddress = true
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

    override fun stopConnector() {

        if (session != null && session!!.isConnected) {
            session!!.closeFuture.awaitUninterruptibly()// 等待连接断开
            connector?.dispose()//彻底释放Session,退出程序时调用不需要重连的可以调用这句话，也就是短连接不需要重连。长连接不要调用这句话，注释掉就OK。
        }
    }
    private var message: String? = null
    override fun send(msg: String?) {
        this.message = msg ?: return
        session?.let {
            val pack = Pack(content = msg)
            if (it.isConnected) {
                it.write(pack)
                message = null
            }
        }
    }

    override fun connectorState() = session?.isConnected ?: false

    /**
     * 关闭Mina长连接 **
     */
    override fun close() {
//        stopConnector()
        session?.closeNow()

        future?.cancel()
        connector?.let {
            if (!it.isDisposed) {
                //清空里面注册的所以过滤器
                it.filterChain.clear()
                it.dispose()
            }
        }

        logger.info("Connector closed")
    }

    override fun getIsAliveThread(): Boolean = isAlive

    override fun startThread() {
        start()
    }
}
