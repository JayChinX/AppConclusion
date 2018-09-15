package com.qxj.conclusion.MVPDevelop.Presenter

import android.text.TextUtils
import com.qxj.conclusion.ConclusionUtils.PermissionUtil
import com.qxj.conclusion.MVPDevelop.MVP.IPresenter
import com.qxj.conclusion.MVPDevelop.Model.UserModel

class PermissionPresenter(view: PermissionContract.IPermissionView) : PermissionContract.IPermissionPresenter, IPresenter<PermissionContract.IPermissionView>(view) {


    private val TAG: String = PermissionPresenter::class.java.name

    override fun toCheckPermission(permissionUtil: PermissionUtil) {

        UserModel.toCheckPermission(permissionUtil) {
            mView.get()?.showCheckPermissionResult(it)
        }
    }
}