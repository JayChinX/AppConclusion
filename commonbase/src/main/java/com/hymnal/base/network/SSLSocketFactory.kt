package com.hymnal.base.network

import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

//region https加密适配
@Throws
fun OkHttpClient.Builder.addCertificates(vararg input: InputStream): OkHttpClient.Builder = apply {

    val certificateFactory = CertificateFactory.getInstance("X.509")
    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(null)
    var index = 0
    input.forEach {
        index++
        val certificateAlias = index.toString()
        it.use { inputStream ->
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(inputStream))
        }
    }
    val tr = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    tr.init(keyStore)
    val trustManagers = tr.trustManagers
    if (trustManagers.size != 1 || trustManagers[0] is X509TrustManager) {
        throw IllegalStateException("Unexpected default trust managers: ${Arrays.toString(trustManagers)}")
    }

    val trustManager = trustManagers[0]
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, tr.trustManagers, SecureRandom())
    val sslSocketFactory = sslContext.socketFactory
    this.sslSocketFactory(sslSocketFactory, trustManager as X509TrustManager)
}

//endregion