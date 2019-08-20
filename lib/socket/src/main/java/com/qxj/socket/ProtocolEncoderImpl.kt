package com.qxj.socket

import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolEncoder
import org.apache.mina.filter.codec.ProtocolEncoderOutput
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

/**
 * 自定义编码器
 */
class ProtocolEncoderImpl(private val charset: Charset) : ProtocolEncoder {

    constructor() : this(Charset.defaultCharset())


    override fun encode(p0: IoSession?, p1: Any?, p2: ProtocolEncoderOutput?) {
        // 转为自定义协议包
        val pack = p1 as Pack
        // 初始化缓冲区
        val buffer = IoBuffer.allocate(pack.size)
            .setAutoExpand(true)//自动扩容
            .setAutoShrink(true)//自动收缩
        /**
         * 设置长度、报头、内容
         */
        //报头
        buffer.putString(pack.header, charset.newEncoder())
        //长度
        buffer.putString(pack.length, charset.newEncoder())
        //内容
        if (pack.content != null) buffer.putString(pack.content, charset.newEncoder())
        //发送的消息体为： header + length + content
        // 重置mask，发送buffer
        buffer.flip()
        p2?.write(buffer)
    }

    override fun dispose(p0: IoSession?) {

    }
}