package com.hymnal.socket

import org.apache.mina.core.future.ConnectFuture
import org.apache.mina.core.future.IoFuture
import org.apache.mina.core.service.IoConnector
import org.apache.mina.core.session.IoSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseSocketClient(tag: String,protected val config: SocketConfiguration) :Thread(tag),
    SocketClient {

    protected val logger: Logger by lazy { LoggerFactory.getLogger(tag) }

    protected var connector: IoConnector? = null

    protected var future: ConnectFuture? = null

    protected var session: IoSession? = null

    protected var failCount = 0

    protected var running = true

    override fun init() {
        failCount = 0
    }

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
                init()
                send(message)
            } catch (e: RuntimeException) {
                logger.error("Connector error: {}", e.toString())

                failCount++

                //如果是短链接或者重试次数过多抛出异常 不进行重试操作
                if (!config.long) {
                    config.response?.response(Result.failure(SocketException("Socket error, $e")))
                    return@with this
                }
                config.response?.response(Result.failure(e))
                if (running) {
                    val time = 1500L
                    sleep(time)
                    logger.error("Connector retry： {}", failCount)
                    conntection()
                }

            }

            return@with this
        }
    }

    @Throws
    override fun initFuture(connector: IoConnector): ConnectFuture {
        return connector.let {
            val future = it.connect()
            // 等待连接创建完成
            future.awaitUninterruptibly()
        }
    }

    @Throws
    override fun initSession(future: IoFuture?): IoSession? {
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

    override fun addListener(connector: IoConnector) {
        // 监听客户端是否断线
        connector.addListener(object : IoListener() {
            override fun sessionDestroyed(arg0: IoSession) {
                super.sessionDestroyed(arg0)
                conntection()
            }
        })
    }

    override fun stopConnector() {

        if (session != null && session!!.isConnected) {
            session!!.closeFuture.awaitUninterruptibly()// 等待连接断开
            connector?.dispose()//彻底释放Session,退出程序时调用不需要重连的可以调用这句话，也就是短连接不需要重连。长连接不要调用这句话，注释掉就OK。
        }
    }

    protected var message: String? = null
    override fun send(msg: String?) {
        this.message = msg ?: return
        session?.let {
            if (it.isConnected) {
                it.write(msg)
                message = null
            }
        }
    }

    override fun close() {
        session?.closeOnFlush()

        future?.cancel()
        connector?.let {
            if (!it.isDisposed) {
                //清空里面注册的所以过滤器
                it.filterChain.clear()
                it.dispose()
            }
        }
        running = false
        logger.info("Connector closed")
    }

    override fun connectorState() = session?.isConnected ?: false

    override fun getIsAliveThread(): Boolean = isAlive

    override fun startThread() {
        start()
    }
}

