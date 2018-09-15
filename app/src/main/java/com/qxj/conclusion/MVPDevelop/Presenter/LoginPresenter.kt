package com.qxj.conclusion.MVPDevelop.Presenter

import android.text.TextUtils
import com.qxj.conclusion.MVPDevelop.MVP.IPresenter
import com.qxj.conclusion.MVPDevelop.Model.UserModel

class LoginPresenter(view: LoginContract.ILoginView) : LoginContract.ILoginPresenter, IPresenter<LoginContract.ILoginView>(view) {

    private val TAG: String = LoginPresenter::class.java.name
    override fun toLoginUser(name: String, password: String, url: String) {
        //用户名和密码检查合法性

        if (TextUtils.isEmpty(name)) {
            mView.get()?.showLoginUserResult(false, "账户名不能为空")
            return
        }

        if (TextUtils.isEmpty(password)) {
            mView.get()?.showLoginUserResult(false, "密码不能为空")
            return
        }

        UserModel.toLoginUser(name, password, url){ b: Boolean, s: String ->
            mView.get()?.showLoginUserResult(b, s)
        }
    }
}