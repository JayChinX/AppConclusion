package com.qxj.conclusion.mvvm.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableBoolean
import com.qxj.conclusion.mvvm.model.User
import com.qxj.conclusion.mvvm.model.VMLoginModel

class VMLoginViewModel(var owner: LifecycleOwner) {

    var user = User()

    var loading = ObservableBoolean(false)

    var model = VMLoginModel()


    fun getUser() {
        model.getUserLogin(owner, { startLoading() },
                { stopLoading() },
                { user.updateUser("qq", 10, 1, "天天向上") },
                {  })


    }

    private fun startLoading() = loading.set(true)
    private fun stopLoading() = loading.set(false)

}