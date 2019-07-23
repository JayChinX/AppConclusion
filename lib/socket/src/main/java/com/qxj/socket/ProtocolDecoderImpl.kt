package com.qxj.socket

import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.CumulativeProtocolDecoder
import org.apache.mina.filter.codec.ProtocolDecoderOutput
import java.nio.charset.Charset

/**
 * 自定义解码器
 */
class ProtocolDecoderImpl(private val charset: Charset) : CumulativeProtocolDecoder() {

    constructor() : this(Charset.defaultCharset())


    override fun doDecode(p0: IoSession, p1: IoBuffer, p2: ProtocolDecoderOutput): Boolean {
        // 包头的长度
        val PACK_HEAD_LEN = 5;
        // 拆包时，如果可读数据的长度小于包头的长度，就不进行读取
        if (p1.remaining() < PACK_HEAD_LEN) {
            return false
        }
        if (p1.remaining() > 1) {
            // 标记设为当前
            p1.mark()
            // 获取总长度
            val length = p1.getInt(p1.position())
            // 如果可读取数据的长度 小于 总长度 - 包头的长度 ，则结束拆包，等待下一次
            if (p1.remaining() < (length - PACK_HEAD_LEN)) {
                p1.reset()
                return false
            } else {
                // 重置，并读取一条完整记录
                p1.reset()
                val bytes = ByteArray(length)
                // 获取长度4个字节、版本1个字节、内容
                p1.get(bytes, 0, length)
                val flag = bytes[4]
                val content = String(bytes, PACK_HEAD_LEN, length - PACK_HEAD_LEN, charset)
                // 封装为自定义的java对象
                val pack = Pack(flag, content)
                p2.write(pack)
                // 如果读取一条记录后，还存在数据（粘包），则再次进行调用
                return p1.remaining() > 0
            }
        }
        return false
    }
}