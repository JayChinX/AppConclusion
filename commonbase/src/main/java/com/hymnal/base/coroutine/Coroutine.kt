package com.hymnal.base.coroutine

import android.os.AsyncTask
import android.widget.TextView
import kotlinx.coroutines.*
import java.lang.Runnable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine


/**
 * kotlin 协程
 */


/**
 * kotlin 协程
 */
private fun getUser() = runBlocking {
    launch(Dispatchers.Main) {
        val user = async(Dispatchers.IO) {
            "main"
        }.await()


    }
}

//runBlocking
private fun getUser(textView: TextView) {
    getUser()
    //launch
    GlobalScope.launch(Dispatchers.Main) {
        //async
        textView.text = async(AndroidCommonPool) {
            return@async "main"
        }.await()


    }
}

private fun coroutines() {

    GlobalScope.launch {
        delay(1000L)
        println("good")
    }
    println("Hello,")
    Thread.sleep(2000L)

    //对于没有协程作用域，但需要启动协程的时候使用GlobalScope
    GlobalScope.launch {
        /**
         * 有协程作用域 如：GlobalScope 内部
         */
        //在作用域内部创建作用域
        coroutineScope {

        }
        //对于明确要求子协程之间相互独立不干扰时使用
        supervisorScope {

        }
        //可以返回数据
        suspendCoroutine<String> {

        }
    }

    val name = GlobalScope.async<String> {
        ""
    }
    //async 生产者
    //await（会抛出异常） join（不会） 消费者
    //join不管因为什么原因结束
}

fun main() = runBlocking {
    //    coroutines()
//    getUser()
}




object AndroidCommonPool: CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(block)
    }

}
//
//fun main() = runBlocking {
//    //    launch {
////        delay(1000L)
////        println("good")
////        doWorld()
////    }
////    println("hello")
//
//    repeat(10_000) {
//        launch {
//            delay(1000L)
//            print(".")
//        }
//    }
//}
//
//suspend fun doWorld() {
//    delay(1000L)
//    println("world")
//}