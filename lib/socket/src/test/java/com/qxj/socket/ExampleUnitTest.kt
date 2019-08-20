package com.qxj.socket

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

    val logger = LoggerFactory.getLogger("Test")
    val request = "{\"cmd\":\"login\",\"param\":{\"name\":\"qxj\",\"password\":\"123456\"}}"
    logger.info("start")
    SocketClient.Builder()
            .setType(SocketClient.Type.TCP, false)
            .setTag("SocketSingle")
            .setIp(ip = "106.12.184.238", port = 9999)
            .send(request)
            .setResponse(Response {

            })
            .builder()

}