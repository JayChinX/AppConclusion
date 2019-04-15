package com.qxj.multichannel.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class TestWorker(context: Context, worker: WorkerParameters) : Worker(context, worker) {

    override fun doWork(): Result {

        Log.d("QXJ", "执行了 doWork 操作")
        return Result.SUCCESS
    }
}