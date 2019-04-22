package com.qxj.welcome.utilities

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter

/**
 * ARouter 的代理类
 */
class Navigation {

    companion object {
        @Volatile
        private var instance: Navigation? = null
        fun getInstance() =
                instance ?: synchronized(this) {
                    instance
                            ?: Navigation().also {
                        instance = it
                    }
                }
    }

    fun inject(thiz: Any) {
        ARouter.getInstance().inject(thiz)
    }

    fun getFragment(path: String) =
            ARouter.getInstance()
                    .build(path).navigation() as Fragment

    fun toOtherActivity(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

}