package com.qxj.conclusion.MVPDevelop.Presenter

import com.qxj.conclusion.MVPDevelop.View.IView

interface LoginContract {
    //登录
    //V层
    interface ILoginView : IView{
        fun showLoginUserResult(boolean: Boolean, msg: String)
    }

    //P层
    interface ILoginPresenter{
        fun toLoginUser(name: String, password: String, url: String)
    }
}