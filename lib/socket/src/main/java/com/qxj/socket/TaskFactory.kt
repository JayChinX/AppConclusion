package com.qxj.socket

import android.net.ParseException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.*

class TaskFactory private constructor() {
    private val logger: Logger by lazy { LoggerFactory.getLogger(TaskFactory::class.java) }

    companion object {
        private var taskMap: ConcurrentMap<String, Task> = ConcurrentHashMap()

        @Volatile
        private var INSTANCE: TaskFactory? = null

        @JvmStatic
        fun getInstance() = INSTANCE ?: synchronized(this) {
            return@synchronized INSTANCE ?: TaskFactory()
        }
    }

    fun getTcpTask(ip: String, port: Int, name: String, period: Long, received: Received, run: (SocketClient) -> Unit) {
        getTcpTask(ip, port, name, 1000, period, received, run)
    }

    private fun getTcpTask(
        ip: String, port: Int, name: String, delay: Long, period: Long, received: Received, run: (SocketClient) -> Unit
    ): Task {

        val task = taskMap[name]
        return if (task != null) {
            logger.error("更新 CycleTask")
            task.update(run)
            task
        } else {
            logger.info("创建 CycleTask")
            val socketClient = SocketClient.Builder()
                .setType(SocketClient.Type.TCP)
                .setTag(name)
                .setIp(ip = ip, port = port)
                .setTime(15 * 1000)
                .setReceived(received)
                .builder()

            val newInstance = CycleTask(socketClient, delay, period)
                .create(run)
                .start()
            val oldInstance = taskMap.putIfAbsent(name, newInstance)
            oldInstance ?: newInstance
        }
    }

    fun getTcpTask(ip: String, port: Int, msg: String, received: Received): Task {
        val name = "SocketSingle"

        val task = taskMap[name]

        return if (task != null && (task as SingleTask).socketClient.getIsAliveThread()) {
            task.update {
                logger.info("更新 SocketSingle")
                it.sendShortData(msg)
            }.start()
            task
        } else {
            logger.info("创建 SocketSingle")
            val socketClient = SocketClient.Builder()
                .setType(SocketClient.Type.TCP, false)
                .setTag(name)
                .setIp(ip = ip, port = port)
                .setTime(15 * 1000)
                .setReceived(received)
                .builder()
                .apply {
                    startThread()
                }
            val newInstance = SingleTask(socketClient)
                .create {
                    logger.info("启动 SocketSingle")
                    it.sendShortData(msg)
                }
                .start()
            val oldInstance = taskMap.putIfAbsent(name, newInstance)
            oldInstance ?: newInstance
        }

    }

    fun getUdpTask(ip: String, port: Int, msg: String, received: Received): Task {
        val name = "UdpSocketSingle"
        val task = taskMap[name]

        return if (task != null && (task as SingleTask).socketClient.getIsAliveThread()) {
            task
                .update {
                    logger.info("更新 UdpSocketSingle")
                    it.sendShortData(msg)
                }
                .start()
            task
        } else {
            logger.info("创建 UdpSocketSingle")
            val socketClient = SocketClient.Builder()
                .setType(SocketClient.Type.UDP, false)
                .setTag(name)
                .setIp(ip = ip, port = port)
                .setTime(15 * 1000)
                .setReceived(received)
                .builder()
                .apply {
                    startThread()
                }
            val newInstance = SingleTask(socketClient)
                .create {
                    logger.info("启动 UdpSocketSingle")
                    it.sendShortData(msg)
                }
                .start()
            val oldInstance = taskMap.putIfAbsent(name, newInstance)
            oldInstance ?: newInstance
        }

    }


    interface Task {

        fun create(run: (SocketClient) -> Unit): Task

        fun update(run: (SocketClient) -> Unit): Task

        fun start(): Task

        fun stop()
    }

    internal class SingleTask(val socketClient: SocketClient) : Task {

        private var run: ((SocketClient) -> Unit)? = null

        private val runnable = Runnable {
            try {
                run?.invoke(socketClient)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }

        override fun create(run: (SocketClient) -> Unit): Task {

            this.run = run
            return this
        }

        override fun update(run: (SocketClient) -> Unit): Task {
            this.run = run
            return this
        }

        override fun start(): Task {
            runnable.run()
            return this
        }

        override fun stop() {

        }
    }


    internal class CycleTask internal constructor(
        private val socketClient: SocketClient,
        private val delay: Long,
        private val period: Long
    ) : Task {

        private var run: ((SocketClient) -> Unit)? = null
        private val semaphore = Semaphore(1)
        private val runnable = Runnable {
            try {
                semaphore.acquire()
                run?.invoke(socketClient)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                semaphore.release()
            }
        }

        override fun create(run: (SocketClient) -> Unit): Task {
            this.run = run
            return this
        }

        override fun update(run: (SocketClient) -> Unit): Task {
            this.run = run
            return this
        }

        private lateinit var scheduler: ScheduledExecutorService
        override fun start(): Task {
            try {
                socketClient.startThread()
                if (::scheduler.isInitialized) scheduler.shutdown()

                val scheduler = Executors.newScheduledThreadPool(1)

                synchronized(scheduler) {
                    scheduler.scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS)//周期
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return this
        }

        override fun stop() {

        }

    }
}