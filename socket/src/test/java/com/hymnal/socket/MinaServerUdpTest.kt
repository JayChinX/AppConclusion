package com.hymnal.socket

import com.hymnal.socket.default.Pack
import com.hymnal.socket.default.ProtocolCodecFactoryImpl
import org.apache.mina.core.service.IoAcceptor
import org.apache.mina.core.session.ExpiringSessionRecycler
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.executor.ExecutorFilter
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.InetSocketAddress
import java.util.concurrent.Executors


object MinaServerUdpTest {
    private val logger = LoggerFactory.getLogger(MinaServerUdpTest::class.java)

    // 端口
    private val MINA_PORT = 7085

    @JvmStatic
    fun main(args: Array<String>) {
        val acceptor: IoAcceptor
        try {
            // 创建一个非阻塞的服务端server
            acceptor = NioDatagramAcceptor()
            // 此行代码能让你的程序整体性能提升10倍
            acceptor.getFilterChain().addLast(
                "threadPool",
                ExecutorFilter(Executors.newCachedThreadPool())
            )
            // 设置编码过滤器（自定义）
            acceptor.getFilterChain().addLast(
                "mycoder",
                ProtocolCodecFilter(
                    ProtocolCodecFactoryImpl(pack = Pack(header = "****", HEADER = 4, LENGTH = 4))
                )
            )

            // 设置会话超时时间（单位：毫秒），不设置则默认是10秒，请按需设置
            acceptor.sessionRecycler = ExpiringSessionRecycler(15 * 1000)

            // 设置缓冲区大小
            acceptor.sessionConfig.readBufferSize = 1024
            // 输入缓冲区
            acceptor.sessionConfig.receiveBufferSize = 1024
            // 输出缓冲区
            acceptor.sessionConfig.sendBufferSize = 1024
            //设置每一个非主监听连接的端口可以重用
            acceptor.sessionConfig.isReuseAddress = true
            // 设置读写空闲时间
            acceptor.sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, 10)
            // 绑定handler
            acceptor.setHandler(ServerHandler())
            // 绑定端口 可同时绑定多个
            acceptor.bind(InetSocketAddress(MINA_PORT))
            logger.info("创建Mina服务端成功，端口：$MINA_PORT")
        } catch (e: IOException) {
            logger.error("创建Mina服务端出错：" + e.message)
        }

    }
}



