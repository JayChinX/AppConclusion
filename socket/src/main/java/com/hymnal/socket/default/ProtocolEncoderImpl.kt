package com.hymnal.socket.default

import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolEncoder
import org.apache.mina.filter.codec.ProtocolEncoderOutput
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

/**
 * 自定义编码器
 */
class ProtocolEncoderImpl(private val charset: Charset, private val pack: Pack) : ProtocolEncoder {

    private val logger by lazy { LoggerFactory.getLogger("Encoder") }

    override fun encode(p0: IoSession?, p1: Any?, p2: ProtocolEncoderOutput?) {
        pack.content = p1.toString()
        // 初始化缓冲区
        val buffer = IoBuffer.allocate(pack.getSize())
            .setAutoExpand(true)//自动扩容
            .setAutoShrink(true)//自动收缩
        /**
         * 设置长度、报头、内容
         */
        //报头
        if (Pack.isHex(pack.header)) {
            buffer.put(pack.getHeader())
        } else {
            buffer.putString(pack.header, charset.newEncoder())
        }

        //长度
        buffer.put(pack.getLengthByte())
        //内容
        if (pack.content != null) buffer.putString(pack.content, charset.newEncoder())
        //发送的消息体为： header + length + content
        // 重置mask，发送buffer
        logger.info("编码消息, 包头: {}, 消息体长度： {}, 消息体： {}", pack.header, pack.getLength(), buffer.array())
        buffer.flip()
        p2?.write(buffer)
    }

    override fun dispose(p0: IoSession?) {

    }
}