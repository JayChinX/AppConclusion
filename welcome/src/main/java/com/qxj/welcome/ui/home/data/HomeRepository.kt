package com.qxj.welcome.ui.home.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qxj.commonbase.mvvm.Repository
import com.qxj.socket.Received
import com.qxj.socket.TaskFactory
import com.qxj.welcome.ui.home.data.model.Garden
import com.qxj.welcome.utilities.getFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository private constructor() : Repository, Received {

    private val TAG = HomeRepository::class.java.simpleName

    companion object {
        @Volatile
        private var instance: HomeRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance
                            ?: HomeRepository().also {
                        instance = it
                    }
                }
    }

    fun getDataList(): LiveData<ArrayList<Garden>> {
        val fragments = arrayListOf(
                getGarden("Home", "/home/fragment/OneFragment"),
                getGarden("Dashboard", "/home/fragment/BlankFragment"),
                getGarden("Notification", "/home/fragment/BlankFragment"),
                getGarden("Find", "/home/fragment/BlankFragment"))

        val da = MutableLiveData(fragments)
        return da
    }


    private var count = 0
    suspend fun send() {
        count++
        withContext(Dispatchers.Default) {
            val task1 = TaskFactory.getInstance()
                    .getTcpTask("10.202.91.95",
                            7083,
                            "发送一次短消息$count",
                            this@HomeRepository)
        }

    }

    override fun parseData(result: Result<String>) {

    }

    private fun getGarden(name: String, fragmentPath: String) =
            Garden(name, getFragment(fragmentPath))

    fun getPagerNum() = MutableLiveData(0)


}

