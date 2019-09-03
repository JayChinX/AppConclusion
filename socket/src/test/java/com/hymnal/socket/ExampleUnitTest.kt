package com.hymnal.socket

import com.hymnal.socket.default.Pack
import com.hymnal.socket.default.ProtocolCodecFactoryImpl
import org.junit.Test
import org.junit.Assert.*
import org.slf4j.LoggerFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun test() {

    }

    @Test
    fun socketTest() {


    }


}


fun main(args: Array<String>) {
    test()
}

fun test () {

    val logger = LoggerFactory.getLogger("test")
    val request = "{\"cmd\":\"login\",\"param\":{\"name\":\"qxj\",\"password\":\"123456\"}}"
    logger.info("start")
    val client = SocketClient.Builder()
        .setType(SocketClient.Type.TCP, true)
        .setTag("SocketSingle")
        .setIp(ip = "172.17.0.1", port = 7085)
        .setCodecFactory(ProtocolCodecFactoryImpl(Pack(header = "5aa5", HEADER = 2, LENGTH = 4)))
        .setResponse(Response {

        })
        .builder()

    client.send(request)
}