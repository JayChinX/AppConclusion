package com.qxj.socket

import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFactory
import org.apache.mina.filter.codec.ProtocolDecoder
import org.apache.mina.filter.codec.ProtocolEncoder
import java.nio.charset.Charset

class ProtocolCodecImplFactory(charset: Charset) : ProtocolCodecFactory {

    private val encoder = ProtocolEncoderImpl(charset)
    private val decoder = ProtocolDecoderImpl(charset)

    constructor() : this(Charset.forName("UTF-8"))

    override fun getDecoder(p0: IoSession?): ProtocolDecoder {
        return decoder
    }

    override fun getEncoder(p0: IoSession?): ProtocolEncoder {
        return encoder
    }
}