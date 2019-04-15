package com.qxj.multichannel.work

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

const val MIN_NUMBER = "minNumber"
const val MAX_NUMBER = "maxNumber"
const val RESULT_CODE = "Result"

class TestWorkerA(context: Context, worker: WorkerParameters) : Worker(context, worker) {

    private var minNumber = 0
    private var maxNumber = 0

    override fun doWork(): Result {
        //传入的参数
        minNumber = inputData.getInt(MIN_NUMBER, 0)
        maxNumber = inputData.getInt(MAX_NUMBER, 0)

        /**
         * 执行操作
         */
        val result = maxNumber - minNumber

        //返回的数据
        val outData: Data = Data.Builder().putAll(
                mapOf(RESULT_CODE to result)
        ).build()

        //返回的数据
        outputData = outData
        return Result.SUCCESS
    }
}