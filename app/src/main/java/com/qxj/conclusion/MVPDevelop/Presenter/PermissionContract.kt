package com.qxj.conclusion.MVPDevelop.Presenter

import com.qxj.conclusion.ConclusionUtils.PermissionUtil
import com.qxj.conclusion.MVPDevelop.View.IView

interface PermissionContract {
    //成对出现   View层 Presenter层
    //V层 返回结果进行UI操作接口 权限检查
    interface IPermissionView : IView {
        fun  showCheckPermissionResult(boolean: Boolean)
    }
    //P层 具体的权限检查操作接口
    interface IPermissionPresenter{
        fun toCheckPermission(permissionUtil: PermissionUtil)
    }
}