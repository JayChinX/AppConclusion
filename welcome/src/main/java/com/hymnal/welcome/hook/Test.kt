package com.hymnal.welcome.hook

import java.util.concurrent.TimeUnit

fun main() {
    //1.
    Runtime.getRuntime().addShutdownHook(Thread {
        println("The hook thread 1 is running.")
        TimeUnit.SECONDS.sleep(2)
        println("The hook thread 1 will exit.")
    })

    //2.
    Runtime.getRuntime().addShutdownHook(Thread {
        println("The hook thread 2 is running.")
        TimeUnit.SECONDS.sleep(2)
        println("The hook thread 2 will exit.")
    })
    println("The main thread will exit.")

    /**
     * 钩子线程
     * 当主线程任务结束时，也就是JVM进程即将退出的时候，注入的两个Hook线程被开启
     */

    /**
     * 应用场景 释放资源，如：数据库连接、Socket连接
     *          防止程序重复执行
     */

    /**
     * 注意事项：
     *      1.Hook 线程只有在正确接收到退出信号时，才能被正确执行，如果是通过kill -9强制杀死的进程，不会执行
     *      2.不要在Hook线程中执行耗时操作
     */
}