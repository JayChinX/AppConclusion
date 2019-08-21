package com.qxj.socket

import com.qxj.socket.default.Pack
import com.qxj.socket.default.ProtocolCodecFactoryImpl
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

fun test() {
    val logger = LoggerFactory.getLogger("test")
    val request = "{\"cmd\":\"login\",\"param\":{\"name\":\"qxj\",\"password\":\"123456\"}}"
    logger.info("start")
    SocketClient.Builder()
            .setType(SocketClient.Type.TCP, false)
            .setTag("SocketSingle")
            .setIp(ip = "106.12.184.238", port = 9999)
            .send(request)
            .setCodecFactory(ProtocolCodecFactoryImpl(Pack(header = "****", HEADER = 4, LENGTH = 4)))
            .setResponse(Response {

            })
            .builder()
}