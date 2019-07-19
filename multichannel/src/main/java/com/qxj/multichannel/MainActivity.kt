package com.qxj.multichannel

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.work.*
import com.qxj.multichannel.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //onSupportNavigateUp()方法的重写，意味着Activity将它的 back键点击事件的委托出去，
    // 如果当前并非栈中顶部的Fragment, 那么点击back键，返回上一个Fragment
    override fun onSupportNavigateUp(): Boolean {
//        NavigationUI.setupWithNavController()
        return findNavController(this, R.id.nav_host).navigateUp()
    }

    /**
     * 1.OneTimeWorkRequest.Builder
     * 创建一个单次执行的WorkQuest
     */
    @SuppressLint("NewApi")
    private fun doOther() {
        val workRequest = OneTimeWorkRequest.Builder(TestWorker::class.java).build()
        WorkManager.getInstance().enqueue(workRequest)

        WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.id)
                .observe(this, Observer {
                    Log.e("qxj", it?.state?.name)

                    if (it?.state!!.isFinished) {
                        Log.d("qxj", "testWork finish")
                    }
                })

        /**
         * 2.任务约束
         * 任务在连接网络，接通电源，并且设备空闲时执行
         */
        val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)//连接网络时
                .setRequiresCharging(true)//连接电源时
                .setRequiresDeviceIdle(true)//设备空闲时
                .build()
        //setRequiredNetworkType：网络连接设置
        //setRequiresBatteryNotLow：是否为低电量时运行 默认false
        //setRequiresCharging：是否要插入设备（接入电源），默认false
        //setRequiresDeviceIdle：设备是否为空闲，默认false
        //setRequiresStorageNotLow：设备可用存储是否不低于临界阈值

        val com = OneTimeWorkRequest.Builder(TestWorker::class.java)
                .setConstraints(constraint)
                .build()
        WorkManager.getInstance().enqueue(com)

        /**
         * 3.取消任务
         */
        WorkManager.getInstance().cancelWorkById(com.id)

        /**
         * 4.添加TAG
         * 添加分配标记字符串来对任务进行分组
         */
        OneTimeWorkRequest.Builder(TestWorker::class.java)
                .addTag("qq")
                .build()
        //返回所有具有该标记的任务列表
        val group = WorkManager.getInstance().getStatusesByTag("qq")
        //取消具有特定标记的所有任务
        WorkManager.getInstance().cancelAllWorkByTag("qq")

        /**
         * 5.重复任务
         */
        val timeRequest = PeriodicWorkRequest.Builder(TestWorker::class.java,
                10,
                TimeUnit.SECONDS).build()

        /**
         * 6.链式任务
         * 如果任何任务返回 Result.FAILURE 则整个序列结束
         */
        WorkManager.getInstance()
                .beginWith(workRequest, workRequest)//创建一个序列
                .then(com)//添加剩余的对象
                .then(com, workRequest)//同一方法传递的多个对象并行
                .enqueue()//整个序列排入队列

        /**
         * 7.多个链式连接成序列
         * 先执行A、C，然后B、D，最后执行任务E
         * 如果执行到B，返回Result.FAILURE，则执行结果也执行到B就结束
         */
        val configA_B = WorkManager.getInstance()
                .beginWith(workRequest)//A
                .then(workRequest)//B

        val configC_D = WorkManager.getInstance()
                .beginWith(workRequest)//C
                .then(workRequest)//D
        WorkContinuation.combine(configA_B, configC_D)
                .then(workRequest)//E
                .enqueue()

        /**
         * 8.其他特定的工作方式
         *      1）beginUniqueWork() 创建唯一工作序列
         *       * ExistingWorkPolicy.REPLACE：取消现有序列并将其替换为新序列
         *       * ExistingWorkPolicy.KEEP：保留现有序列并忽略您的新请求
         *       * ExistingWorkPolicy.APPEND：将新序列附加到现有序列，在现有序列的最后一个任务完成后运行新序列的第一个任务
         */

        WorkManager.getInstance().beginUniqueWork(
                "worker",//每个独特的工作序列都有一个名称; 同一时间只允许执行一个使用该名称工作序列
                ExistingWorkPolicy.APPEND,//当有相同名称序列时采取的策略方式
                workRequest)
                .enqueue()

        /**
         * 9.传递参数 并获取返回结果
         *
         * 对于链式调用 前一个任务结果会作为后一个任务的参数传入
         */

        val map = mapOf(MIN_NUMBER to 5, MAX_NUMBER to 15)
        val data = Data.Builder().putAll(map).build()
        //创建worker并传递参数
        val mathWork = OneTimeWorkRequest.Builder(TestWorkerA::class.java)
                .setInputData(data)
                .build()

        WorkManager.getInstance().getWorkInfoByIdLiveData(mathWork.id)
                .observe(this, Observer {
                    if (it?.state!!.isFinished) {
                        //获取返回的结果
                        val outData = it.outputData.getInt(RESULT_CODE, 0)
                        Log.e("qxj", "$outData")
                    }
                })


    }

}
