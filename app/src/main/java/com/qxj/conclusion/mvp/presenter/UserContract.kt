package com.qxj.conclusion.mvp.presenter

import com.qxj.conclusion.mvp.base.IView

interface UserContract {
    //成对出现   View层 Presenter层
    //返回结果进行UI操作接口 添加用户
    interface IUserView : IView {
        fun showAddUserResult(boolean: Boolean)
    }
    //P层  具体的添加用户操作接口
    interface IUserPresenter{
        fun addUser(name: String)
    }
}