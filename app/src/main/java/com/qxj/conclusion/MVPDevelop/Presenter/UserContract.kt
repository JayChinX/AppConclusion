package com.qxj.conclusion.MVPDevelop.Presenter

import com.qxj.conclusion.MVPDevelop.MVP.IView

interface UserContract {
    interface IUserView : IView {
        fun showAddUserResult(boolean: Boolean)
    }

    interface IUserPresenter{
        fun addUser(name: String)
    }
}