package com.hymnal.socket.default

import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolCodecFactory
import org.apache.mina.filter.codec.ProtocolDecoder
import org.apache.mina.filter.codec.ProtocolEncoder
import java.nio.charset.Charset

class ProtocolCodecFactoryImpl : ProtocolCodecFactory {
    private var encoder: ProtocolEncoder? = null
    private var decoder: ProtocolDecoder? = null
    private val charset: Charset = Charset.forName("UTF-8")

    constructor(pack: Pack) {
        this.encoder = ProtocolEncoderImpl(charset, pack)
        this.decoder = ProtocolDecoderImpl(charset, pack)
    }

    constructor(encoder: ProtocolEncoder, decoder: ProtocolDecoder) {
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