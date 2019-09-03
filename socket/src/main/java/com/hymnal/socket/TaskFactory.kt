package com.hymnal.socket

import android.net.ParseException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class TaskFactory private constructor() {

    companion object {

        @Volatile
        private var INSTANCE: TaskFactory? = null

        @JvmStatic
        fun getInstance() = INSTANCE ?: synchronized(this) {
            return@synchronized INSTANCE ?: TaskFactory()
        }
    }

    private var run: (() -> Unit)? = null

    private lateinit var scheduler: ScheduledExecutorService

    private val semaphore = Semaphore(1)

    private val runnable = Runnable {
        try {
            semaphore.acquire()
            run?.invoke()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            semaphore.release()
        }
    }

    fun create(period: Long, run: () -> Unit) {
        try {
            this.run = run
            if (::scheduler.isInitialized) scheduler.shutdown()

            val scheduler = Executors.newScheduledThreadPool(1)

            synchronized(scheduler) {
                scheduler.scheduleAtFixedRate(runnable, 1000, period, TimeUnit.MILLISECONDS)//周期
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    fun update(run: () -> Unit) {
        this.run = run
    }

    fun stop() {
        if (::scheduler.isInitialized) scheduler.shutdown()
    }


}