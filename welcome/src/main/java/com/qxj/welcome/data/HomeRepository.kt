package com.qxj.welcome.data

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter

class HomeRepository private constructor() {

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

    private val default = 0

    private val fragments = arrayListOf(
            getFragment("/home/fragment/OneFragment"),
            getFragment("/home/fragment/BlankFragment"),
            getFragment("/home/fragment/BlankFragment"),
            getFragment("/home/fragment/BlankFragment"))

    private val nameList = arrayListOf(
            "Home",
            "Dashboard",
            "Notification",
            "Find"
    )

    fun getDefaultFragment(): Int = default

    fun getFragments(): List<Fragment> = fragments

    fun getFragmentNames(): List<String> = nameList

    private fun getFragment(path: String) = ARouter.getInstance()
            .build(path)
            .navigation() as Fragment
}

