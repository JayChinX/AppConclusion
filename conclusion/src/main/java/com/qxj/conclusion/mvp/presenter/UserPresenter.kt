package com.qxj.conclusion.mvp.presenter

import androidx.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.qxj.commonbase.util.LogTool
import com.qxj.commonbase.mvpbase.IPresenter
import com.qxj.conclusion.mvp.model.UserModel

class UserPresenter(view: UserContract.IUserView) :
        UserContract.IUserPresenter,
        IPresenter<UserContract.IUserView>(view){
    //P 层的实现

    private val TAG: String = UserPresenter::class.java.name

    override fun initLifecycle(owner: LifecycleOwner) {

    }

    override fun addUser(name: String) {
        //
        LogTool.d(TAG, "正在添加名字")
        mView.get()?.showLoading()

        //
        if (!TextUtils.isEmpty(name)) {
            LogTool.d(TAG, "正在验证名字")
            UserModel.addUser(name) {
                mView.get()?.hideLoading()
                mView.get()?.showAddUserResult(it)
            }
        }
    }
}