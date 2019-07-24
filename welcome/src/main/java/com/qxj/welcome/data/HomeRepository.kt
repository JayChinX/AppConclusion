package com.qxj.welcome.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qxj.commonbase.mvvm.Repository
import com.qxj.socket.Received
import com.qxj.socket.TaskFactory
import com.qxj.welcome.utilities.Navigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository private constructor() : Repository, Received {

    private val TAG = HomeRepository::class.java.simpleName

    companion object {
        @Volatile
        private var instance: HomeRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: HomeRepository().also {
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
                    .getTask("10.202.91.95",
                            7083,
                            this@HomeRepository,
                            "发送一次短消息$count")
        }

    }

    override fun parseData(msg: String?) {

    }

    private fun getGarden(name: String, fragmentPath: String) =
            Garden(name, Navigation.getInstance()
                    .getFragment(fragmentPath))

    fun getPagerNum() = MutableLiveData(0)


}

