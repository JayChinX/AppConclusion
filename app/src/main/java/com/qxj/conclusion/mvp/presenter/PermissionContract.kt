package com.qxj.conclusion.mvp.presenter

import android.content.Context
import com.qxj.commonbase.mvpbase.IView

interface PermissionContract {
    //成对出现   View层 Presenter层
    //V层 返回结果进行UI操作接口 权限检查
    interface IPermissionView : IView {
        fun  showCheckPermissionResult(boolean: Boolean)
    }
    //P层 具体的权限检查操作接口
    interface IPermissionPresenter{
        fun toCheckPermission(context: Context)
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    }
}