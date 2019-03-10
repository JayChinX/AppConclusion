package com.qxj.conclusion.mvp.presenter

import com.qxj.commonbase.mvpbase.IModel
import com.qxj.commonbase.mvpbase.IView

interface LoginContract {
    //登录

    interface LoginModel: IModel {
        fun checkLogin(userId: String, password: String, callback: (String) -> Unit): Boolean
    }
    //V层
    interface LoginView : IView {
        fun loginSuccess(msg: String)
        fun loginFiled(msg: String)
    }

    //P层
    interface LoginPresenter{
        fun login(name: String, password: String)
    }
}