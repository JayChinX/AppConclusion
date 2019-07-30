package com.qxj.socket

import android.net.ParseException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.*

class TaskFactory private constructor() {

    companion object {
        private val logger: Logger by lazy { LoggerFactory.getLogger(TaskFactory::class.java) }
        private const val ROOT_LOGGER_NAME = "root"
        private var taskMap: ConcurrentMap<String, Task> = ConcurrentHashMap()

        @Volatile
        private var INSTANCE: TaskFactory? = null

        @JvmStatic
        fun getInstance() = INSTANCE ?: synchronized(this) {
            return@synchronized INSTANCE ?: TaskFactory()
        }
    }

    fun getTask(
        ip: String,
        port: Int,
        name: String,
        delay: Long,
        period: Long,
        received: Received,
        run: (SocketClient) -> Unit
    ): Task {
        val name = if (name.equals(ROOT_LOGGER_NAME, ignoreCase = true)) {
            ""
        } else {
            name
        }

        val task = taskMap[name]
        return if (task != null) {
            logger.error("更新 CycleTask")
            task.update(run)
            task
        } else {
            logger.info("创建 CycleTask")
            val newInstance = CycleTask(
                name,
                SocketClient.Builder()
                    .setTag(name)
                    .setIp(ip = ip, port = port)
                    .setTime(15 * 1000)
                    .setReceived(received)
                    .builder(),
                delay,
                period
            )
                .create(run)
                .start()
            val oldInstance = taskMap.putIfAbsent(name, newInstance)
            oldInstance ?: newInstance
        }
    }

    fun getTask(ip: String, port: Int, msg: String, received: Received): Task {
        val name = "SocketSingle"

        val task = taskMap[name]

        return if (task != null && (task as SingleTask).socketClient.isAlive) {
            task.update {
                logger.info("更新 SocketSingle")
                it.sendShortData(msg)
            }.start()
            task
        } else {
            logger.info("创建 SocketSingle")
            val newInstance = SingleTask(
                SocketClient.Builder()
                    .setTag(name)
                    .setIp(ip = ip, port = port)
                    .setTime(15 * 1000)
                    .setType(false)
                    .setReceived(received)
                    .builder().apply {
                        start()
                    }
            )
                .create {
                    logger.info("启动 SocketSingle")
                    it.sendShortData(msg)
                }.start()
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

    class SingleTask(val socketClient: SocketClient) : Task {

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


    class CycleTask internal constructor(
        private val tag: String,
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
                socketClient.start()
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