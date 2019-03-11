package com.qxj.conclusion.mvp.model

import android.text.TextUtils
import com.qxj.conclusion.mvp.presenter.LoginContract

class LoginModel: LoginContract.LoginModel {
    override fun checkLogin(userId: String, password: String, callback: (String) -> Unit): Boolean {
        if (TextUtils.isEmpty(userId)) {
            callback("账户名不能为空")
            return false
        }

        if (TextUtils.isEmpty(password)) {
            callback("密码不能为空")
            return false
        }
        return true
    }

}