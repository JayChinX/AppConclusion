package com.qxj.welcome.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qxj.commonbase.mvvm.Repository
import com.qxj.socket.Socket
import com.qxj.welcome.utilities.Navigation

class HomeRepository private constructor() : Repository {

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
                getGarden("Dashboard", "/home/fragment/BlankFragment") ,
                getGarden("Notification", "/home/fragment/BlankFragment"),
                getGarden("Find", "/home/fragment/BlankFragment"))

        val da = MutableLiveData(fragments)
        return da
    }

    suspend fun send() {
        val socket = Socket.Builder()
                .setIp("10.202.91.95", 7083)
                .setTime(15 * 1000)
                .builder()
        socket.start()
        var num = 0
        while (true) {
            kotlinx.coroutines.delay(5000)
            num++
            socket.sendData("$num.....")
        }
    }

    private fun getGarden(name: String, fragmentPath: String) =
            Garden(name, Navigation.getInstance()
                    .getFragment(fragmentPath))

    fun getPagerNum() = MutableLiveData(0)



}

