package com.hymnal.socket.default

import com.hymnal.socket.default.Pack
import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.CumulativeProtocolDecoder
import org.apache.mina.filter.codec.ProtocolDecoderOutput
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.util.*

/**
 * 自定义解码器
 */
class ProtocolDecoderImpl(private val charset: Charset, private val pack: Pack) : CumulativeProtocolDecoder() {

    private val logger by lazy { LoggerFactory.getLogger("Decode") }

    override fun doDecode(p0: IoSession, p1: IoBuffer, p2: ProtocolDecoderOutput): Boolean {
        // 包头的长度
        val PACK_HEAD_LEN = pack.HEADER + pack.LENGTH

        // 拆包时，如果可读数据的长度小于包头的长度，就不进行读取
        if (p1.remaining() > 1 && p1.remaining() > PACK_HEAD_LEN) {
            // 标记设为当前
            p1.mark()
            // 获取总长度
            val bytes = ByteArray(PACK_HEAD_LEN)
            p1.get(bytes, 0, PACK_HEAD_LEN)

            val headerBytes = ByteArray(pack.HEADER)
            System.arraycopy(bytes, 0, headerBytes, 0, pack.HEADER)
            val header = Pack.bytesToHex(headerBytes)

            logger.info("解码包头：{}", header)
            val length =
                if (header == pack.header) {
                    //消息总长度
                    val lengthBytes = ByteArray(pack.LENGTH)
                    System.arraycopy(bytes, pack.HEADER, lengthBytes, 0, pack.LENGTH)
                    logger.info("解码长度 ${Arrays.toString(lengthBytes)}：{}", Pack.getBytesToInt(lengthBytes))
                    Pack.getBytesToInt(lengthBytes) + PACK_HEAD_LEN
                } else {
                    p1.limit()
                }

            // 如果可读取数据的长度 小于 总长度 - 包头的长度 ，则结束拆包，等待下一次
            if (p1.remaining() < (length - PACK_HEAD_LEN)) {
                p1.reset()
                return false
            } else {
                // 重置，并读取一条完整记录
                p1.reset()

                val dst = ByteArray(length)
                // 获取长度4个字节、版本1个字节、内容
                p1.get(dst, 0, length)


                val content = String(dst, PACK_HEAD_LEN, length - PACK_HEAD_LEN, charset)
                // 封装为自定义的java对象
                p2.write(content)
                // 如果读取一条记录后，还存在数据（粘包），则再次进行调用
                return p1.remaining() > 0
            }
        }
        return false
    }
}