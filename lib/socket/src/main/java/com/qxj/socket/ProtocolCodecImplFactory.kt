package com.qxj.socket

import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFactory
import org.apache.mina.filter.codec.ProtocolDecoder
import org.apache.mina.filter.codec.ProtocolEncoder
import java.nio.charset.Charset

class ProtocolCodecImplFactory() : ProtocolCodecFactory {

    private var encoder: ProtocolEncoder? = null
    private var decoder: ProtocolDecoder? = null

    constructor(charset: Charset) : this() {
        this.encoder = ProtocolEncoderImpl(charset)
        this.decoder = ProtocolDecoderImpl(charset)
    }

    constructor(encoder: ProtocolEncoder, decoder: ProtocolDecoder) : this() {
        this.encoder = encoder
        this.decoder = decoder
    }

    override fun getDecoder(p0: IoSession?): ProtocolDecoder? {
        return decoder
    }

    override fun getEncoder(p0: IoSession?): ProtocolEncoder? {
        return encoder
    }
}