package com.qxj.welcome.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.qxj.socket.Received
import com.qxj.socket.TaskFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SocketService : Service(), Received {

    private val logger: Logger by lazy { LoggerFactory.getLogger(SocketService::class.java) }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logger.info("onStartCommand() start init")
        startPushData()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startPushData() {
        var count = 0
        val task = TaskFactory.getInstance()
                .getTask("10.202.91.95",
                        7083,
                        "CyclePush",
                        this,
                        1000,
                        5000) {
                    count++
                    it.send("消息$count")

                }
    }

    override fun parseData(msg: String?) {
        Log.d("SocketService", msg)
    }
}
