package com.qxj.conclusion.MVPDevelop.Presenter

import android.text.TextUtils
import com.qxj.conclusion.ConclusionUtils.LogTool
import com.qxj.conclusion.MVPDevelop.MVP.IPresenter
import com.qxj.conclusion.MVPDevelop.Model.UserModel

class UserPresenter(view: UserContract.IUserView) : UserContract.IUserPresenter, IPresenter<UserContract.IUserView>(view) {

    private val TAG: String = UserPresenter::class.java.name

    override fun addUser(name: String) {
        //
        LogTool.d(TAG, "正在添加名字")
        mView.get()?.showProgressDialog()

        //
        if (!TextUtils.isEmpty(name)) {
            LogTool.d(TAG, "正在验证名字")
            UserModel.addUser(name) {
                mView.get()?.dismissProgressDialog()
                mView.get()?.showAddUserResult(it)
            }
        }
    }
}