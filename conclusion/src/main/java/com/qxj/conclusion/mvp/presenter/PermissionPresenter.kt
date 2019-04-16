package com.qxj.conclusion.mvp.presenter

import androidx.lifecycle.LifecycleOwner
import android.content.Context
import com.qxj.conclusion.AppConclusionActivity
import com.qxj.conclusion.util.PermissionUtil
import com.qxj.commonbase.mvpbase.IPresenter
import com.qxj.conclusion.mvp.model.UserModel

class PermissionPresenter(view: PermissionContract.IPermissionView) :
        PermissionContract.IPermissionPresenter,
        IPresenter<PermissionContract.IPermissionView>(view) {

    private val TAG: String = PermissionPresenter::class.java.name

    var permissionUtil: PermissionUtil? = null

    override fun initLifecycle(owner: LifecycleOwner) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toCheckPermission(context: Context) {
        permissionUtil = PermissionUtil(context as AppConclusionActivity)
        UserModel.toCheckPermission(permissionUtil!!) {
            mView.get()?.showCheckPermissionResult(it)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionUtil!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}