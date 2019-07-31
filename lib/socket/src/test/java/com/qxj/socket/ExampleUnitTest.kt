package com.qxj.socket

import org.junit.Test

import org.junit.Assert.*

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
    var count = 0


//    while (true) {
//        val msg = "{\"name\":\"${count++}\",\"name1\":\"${count++}\",\"name2\":\"${count++}\",\"name3\":\"${count++}\",\"name4\":\"${count++}\"" +
//                ",\"name5\":\"${count++}\",\"name6\":\"${count++}\",\"name7\":\"${count++}\",\"name8\":\"${count++}\",\"name9\":\"${count++}\",\"name10\":\"${count++}\"}"
//        TaskFactory.getInstance().getTcpTask("10.202.91.95", 7084, msg, object : Received {
//            override fun parseData(result: Result<String>) {
//                if (result.isSuccess) {
//                    println("接受的消息： $result")
//                } else {
//                    println("error： $result")
//                }
//
//            }
//        })
//        Thread.sleep(3000)
//    }

    while (true) {
        val msg =
            "{\"name\":\"${count++}\",\"name1\":\"${count++}\",\"name2\":\"${count++}\",\"name3\":\"${count++}\",\"name4\":\"${count++}\"" +
                    ",\"name5\":\"${count++}\",\"name6\":\"${count++}\",\"name7\":\"${count++}\",\"name8\":\"${count++}\",\"name9\":\"${count++}\",\"name10\":\"${count++}\"}"
        TaskFactory.getInstance().getUdpTask("10.202.91.95", 7084, msg, object : Received {
            override fun parseData(result: Result<String>) {
                if (result.isSuccess) {
                    println("接受的消息： $result")
                } else {
                    println("error： $result")
                }

            }
        })
        Thread.sleep(3000)
    }

}