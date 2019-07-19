package com.qxj.socket


import android.os.HandlerThread
import android.util.Log
import org.apache.mina.core.filterchain.IoFilter
import org.apache.mina.core.future.ConnectFuture
import org.apache.mina.core.service.IoConnector
import org.apache.mina.core.service.IoHandler
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.filter.codec.textline.LineDelimiter
import org.apache.mina.filter.codec.textline.TextLineCodecFactory
import org.apache.mina.filter.keepalive.KeepAliveFilter
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler
import org.apache.mina.filter.logging.LoggingFilter
import org.apache.mina.transport.socket.nio.NioSocketConnector
import java.net.InetSocketAddress
import java.nio.charset.Charset

internal class SocketThread(tag: String) : HandlerThread(tag) {
    private val TAG = SocketThread::class.java.simpleName

    companion object {
        private const val IP = ""
        private const val PORT = 0
    }

    private lateinit var connector: IoConnector
    private var future: ConnectFuture? = null

    private var session: IoSession? = null

    override fun run() {
        super.run()
        conntection()
    }

    private fun conntection() {

        createConnector()
        createSession()
        addListener()
    }

    private fun createConnector() {
        connector = NioSocketConnector()
                .also {
                    //添加过滤器
                    val loggingFilter = createLogger()
                    it.filterChain.addLast("logger", loggingFilter)
                    //编码格式
                    val codec = createCodec()
                    it.filterChain.addLast("codec", codec)
                    //心跳包
                    val heartBeat = createHearBeat()
                    it.filterChain.addLast("heartbeat", heartBeat)

                    //connector.getSessionConfig().setKeepAlive(true);

                    //设置链接超时时间
                    it.connectTimeoutMillis = (15 * 1000).toLong()

                    //读写超时时间
                    it.sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, 15 * 1000)

                    //设置消息拦截器
                    val ioHandler = createIoHandler()
                    it.handler = ioHandler

                    val socketAddress = InetSocketAddress(IP, PORT)

                    it.setDefaultRemoteAddress(socketAddress)
                }

    }

    private fun createLogger(): IoFilter {
        return LoggingFilter()
    }

    private fun createCodec(): IoFilter {
        val factory = TextLineCodecFactory(
                Charset.forName("UTF-8"),
                LineDelimiter.WINDOWS.value,
                LineDelimiter.WINDOWS.value
        )
        factory.decoderMaxLineLength = 1024 * 1024
        factory.encoderMaxLineLength = 1024 * 1024
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
        heartBeat.requestInterval = 3000
        //设置心跳包请求后 等待反馈超时时间。 超过该时间后则调用KeepAliveRequestTimeoutHandler.CLOSE */
        heartBeat.requestTimeout = 15 * 1000
        return heartBeat
    }

    private fun createIoHandler(): IoHandler {
        return ClientHandler()
    }

    private fun createSession() {

        with(connector) {
            //开始连接
            try {
                future = this.connect()
                future?.awaitUninterruptibly()// 等待连接创建完成
                session = future?.session// 获得session
                if (session != null && session!!.isConnected) {
                    //连接成功
                } else {
                    //连接失败
                }
            } catch (e: Exception) {
                //连接出错
            }
        }


    }

    fun stopSession() {

        if (session != null && session!!.isConnected) {
            session!!.closeFuture.awaitUninterruptibly()// 等待连接断开
            connector.dispose()//彻底释放Session,退出程序时调用不需要重连的可以调用这句话，也就是短连接不需要重连。长连接不要调用这句话，注释掉就OK。
        }
    }


    private fun addListener() {
        // 监听客户端是否断线
        connector.addListener(IoListener())

//        connector?.addListener(object : IoListener() {
//            override fun sessionDestroyed(arg0: IoSession) {
//                super.sessionDestroyed(arg0)
//
//                try {
//                    var failCount = 0
//                    while (true) {
//                        sleep(3000)
//                        future = connector!!.connect()
//                        future!!.awaitUninterruptibly()// 等待连接创建完成
//                        session = future!!.session// 获得session
//                        failCount++
//                        if (session != null && session!!.isConnected) {
//                            //重连成功
//                            break
//                        } else {
//                            //重连失败
//                        }
//                    }
//                } catch (e: Exception) {
//                    //重连出错
//                }
//
//            }
//        })
    }

    fun sendData(msg: String) {
        with(session) {
            if (this != null && this.isConnected) this.write(msg)
        }

    }

    /**
     * 关闭Mina长连接 **
     */
    fun close() {
        Log.d(TAG, "close()")
        if (session != null) {
            session!!.close(false)//true为短连接
            session = null
        }
        if (future != null && future!!.isConnected) {
            future!!.cancel()
            future = null
        }
        if (!connector.isDisposed) {
            //清空里面注册的所以过滤器
            connector.filterChain.clear()
            connector.dispose()
        }
        Log.i(TAG, "长连接已关闭...")
    }

    fun addReceiveListener(received: Received) {
        val handler = connector.handler as ClientHandler
        handler.setReceive(received)
    }


}
