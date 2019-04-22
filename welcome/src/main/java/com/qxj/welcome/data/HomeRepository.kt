package com.qxj.welcome.data

import androidx.lifecycle.MutableLiveData
import com.qxj.commonbase.data.Listing
import com.qxj.commonbase.mvvm.Repository
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

    @Suppress("UNCHECKED_CAST")
    override fun <T> getDataList(pageSize: Int): Listing<T> {
         val fragments = arrayListOf(
                getGarden("Home", "/home/fragment/OneFragment") as T,
                 getGarden("Dashboard", "/home/fragment/BlankFragment") as T,
                 getGarden("Notification", "/home/fragment/BlankFragment") as T,
                 getGarden("Find", "/home/fragment/BlankFragment") as T)

        val da = MutableLiveData<ArrayList<T>>(fragments)
        return Listing(dataList = da,
                networkState = null,
                refreshState = null,
                refresh = {},
                retry = {})
    }

    private fun getGarden(name: String, fragmentPath: String) =
            Garden(name, Navigation.getInstance()
                    .getFragment(fragmentPath))

}

