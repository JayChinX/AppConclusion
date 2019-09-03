package com.hymnal.welcome.ui.home.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hymnal.base.mvvm.Repository
import com.hymnal.socket.Response
import com.hymnal.welcome.ui.home.data.model.Garden
import com.hymnal.welcome.utilities.getFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository private constructor() : Repository, Response {

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

        }

    }


    override fun response(result: Result<String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getGarden(name: String, fragmentPath: String) =
            Garden(name, getFragment(fragmentPath))

    fun getPagerNum() = MutableLiveData(0)


}

