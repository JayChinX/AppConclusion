package com.qxj.socket

import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFactory
import org.apache.mina.filter.codec.ProtocolDecoder
import org.apache.mina.filter.codec.ProtocolEncoder
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

// 类继承handler
internal class ServerHandler : IoHandlerAdapter() {

    @Throws(Exception::class)
    override fun messageReceived(session: IoSession?, message: Any?) {
        val pack = message as Pack?
        logger.info("服务端接收" + session!!.id + "消息成功：" + pack)
        val content = "服务端发送测试数据OK"
        val pack1 = Pack(content = content)
        //向客户端写入数据
        val future = session.write(pack1)
        //在100毫秒内写完
        future.awaitUninterruptibly(100)
        if (future.isWritten) {

        } else {

        }
    }

    @Throws(Exception::class)
    override fun messageSent(session: IoSession?, message: Any?) {
        val pack = message as Pack?
        logger.info("服务端发送" + session!!.id + "消息成功：" + pack)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(session: IoSession?, cause: Throwable) {
        cause.printStackTrace()
        logger.error("服务端" + session!!.id + "处理消息异常：" + cause)
    }

    @Throws(Exception::class)
    override fun sessionIdle(session: IoSession?, status: IdleStatus?) {
        logger.error("服务端" + session!!.id + "进入空闲状态")
    }

    @Throws(Exception::class)
    override fun sessionCreated(session: IoSession?) {
        logger.info("服务端与" + session!!.id + "用户连接")
        super.sessionCreated(session)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ServerHandler::class.java)
    }
}


internal class CustomProtocolCodecFactory// 构造方法注入编解码器
@JvmOverloads constructor(charset: Charset = Charset.forName("UTF-8")) : ProtocolCodecFactory {

    private val encoder: ProtocolEncoder
    private val decoder: ProtocolDecoder

    init {
        this.encoder = ProtocolEncoderImpl(charset)
        this.decoder = ProtocolDecoderImpl(charset)
    }

    @Throws(Exception::class)
    override fun getEncoder(session: IoSession): ProtocolEncoder {
        return encoder
    }

    @Throws(Exception::class)
    override fun getDecoder(session: IoSession): ProtocolDecoder {
        return decoder
    }
}