package com.qxj.welcome.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.qxj.socket.Received
import com.qxj.socket.Response
import com.qxj.socket.SocketClient
import com.qxj.socket.TaskFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SocketService : Service(), Response {

    private val logger by lazy { LoggerFactory.getLogger(SocketService::class.java) }

    private val client by lazy {
        SocketClient.Builder()
                .setType(SocketClient.Type.TCP, true)
                .setTag("SocketService")
                .setIp(ip = IP, port = PORT)
                .setResponse(this)
                .builder()
    }
    companion object {
        private const val IP = "106.12.184.238"
        private const val PORT = 9999
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logger.info("onStartCommand() start init")
        startPushData()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startPushData() {
        client.send("")
    }

    override fun response(p0: Result<String>?) {
    }
}
