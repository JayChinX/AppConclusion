package com.qxj.commonsdk.router

import android.app.IntentService
import android.content.Intent
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.qxj.commonsdk.CommonSDKApp

private const val ACTION_INIT = "com.qxj.commonsdk.router.action.INIT"
private const val ACTION_BAZ = "com.qxj.commonsdk.router.action.BAZ"

private const val EXTRA_PARAM1 = "com.qxj.commonsdk.router.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.qxj.commonsdk.router.extra.PARAM2"

class InitializeService : IntentService("InitializeService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_INIT -> {
//                val param1 = intent.getStringExtra(EXTRA_PARAM1)
//                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionInit()
            }
            ACTION_BAZ -> {
                val param1 = intent.getStringExtra(EXTRA_PARAM1)
                val param2 = intent.getStringExtra(EXTRA_PARAM2)
                handleActionBaz(param1, param2)
            }
        }
    }


    private fun handleActionInit() {
        performInitialize()
    }

    private fun handleActionBaz(param1: String, param2: String) {
        TODO("Handle action Baz")
    }

    companion object {
        @JvmStatic
        fun startActionInit(context: Context, param1: String?, param2: String?) {
            val intent = Intent(context, InitializeService::class.java).apply {
                action = ACTION_INIT
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }

        @JvmStatic
        fun startActionBaz(context: Context, param1: String, param2: String) {
            val intent = Intent(context, InitializeService::class.java).apply {
                action = ACTION_BAZ
                putExtra(EXTRA_PARAM1, param1)
                putExtra(EXTRA_PARAM2, param2)
            }
            context.startService(intent)
        }
    }

    private fun performInitialize() {
        initARouter()
    }

    private fun initARouter() {
        ARouter.openLog()//打印日志
        ARouter.openDebug()//开启调试模式
        ARouter.init(CommonSDKApp.instance)
    }
}
