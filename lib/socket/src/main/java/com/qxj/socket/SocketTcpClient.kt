package com.qxj.socket

import org.apache.mina.core.future.ConnectFuture
import org.apache.mina.core.service.IoConnector
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.apache.mina.transport.socket.nio.NioSocketConnector
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.InetSocketAddress


internal class SocketTcpClient(tag: String, private val config: SocketConfiguration) : Thread(tag), SocketClient {
    private val logger: Logger by lazy { LoggerFactory.getLogger(tag) }

    private var connector: IoConnector? = null

    private var future: ConnectFuture? = null

    private var session: IoSession? = null

    private var failCount = 0

    private var initing = false

    override fun run() {
        super.run()
        conntection()
    }

    @Throws
    override fun conntection() {

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
                if (!config.long && shortMsg != null) {
                    sendShortData(shortMsg!!)
                }
            } catch (e: Exception) {
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

    override fun init() {

    }

    override fun createConnector(): IoConnector {
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
                sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, config.BOTH_IDLE)

                //设置链接超时时间
                connectTimeoutMillis = (config.TIMEOUT).toLong()

                //设置消息拦截器
                val ioHandler = config.ioHandler
                handler = ioHandler

                val socketAddress = InetSocketAddress(config.ip, config.port)

                setDefaultRemoteAddress(socketAddress)

                if (config.long) addListener(this)
                connector = this
            }

    }

    override fun stopSession() {

        if (session != null && session!!.isConnected) {
            session!!.closeFuture.awaitUninterruptibly()// 等待连接断开
            connector?.dispose()//彻底释放Session,退出程序时调用不需要重连的可以调用这句话，也就是短连接不需要重连。长连接不要调用这句话，注释掉就OK。
        }
    }

    override fun send(msg: String) {
        session?.let {
            val pack = Pack(content = msg)
            if (it.isConnected) it.write(pack)
        }
    }

    private var shortMsg: String? = null

    @Throws
    override fun sendShortData(msg: String) {
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
    override fun close() {
        logger.info("关闭重连: {}", "close()")
        session?.closeOnFlush()

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


    override fun getIsAliveThread(): Boolean = isAlive

    override fun startThread() {
        start()
    }

}
