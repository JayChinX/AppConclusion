package com.qxj.conclusion.MVPDevelop.Model

import com.qxj.conclusion.ConclusionUtils.PermissionUtil

object UserModel {

    fun addUser(name: String, listener: (Boolean) -> Unit) {
        //添加任务

        if (name == "秦小杰") {
            listener(true)
        } else {
            listener(false)
        }
    }

    fun toCheckPermission(permissionUtil: PermissionUtil, listener: (Boolean) -> Unit) {
        permissionUtil.checkStoragePermission(Runnable {
            listener(true)
        })
    }

    fun toLoginUser(name: String, password: String, url: String, listener: (Boolean, String) -> Unit) {
        listener(true, "登录成功")
    }
}