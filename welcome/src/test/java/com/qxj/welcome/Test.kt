package com.qxj.welcome

import com.qxj.socket.Socket
import org.junit.Test

class Test {
    @Test
    fun socket() {
        val socket = Socket.Builder()
                .setIp("10.202.91.95", 7083)
                .setTime(15 * 1000)
                .builder()
        socket.start()
        var num = 0
        while (true) {
            Thread.sleep(5000)
            num++
            socket.sendData("$num.....")
        }
    }
}