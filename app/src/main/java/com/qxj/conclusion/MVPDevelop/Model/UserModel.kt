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

    fun toSortList(list: MutableList<String>, sortAsc: Boolean, listener: (MutableList<String>) -> Unit) {

        if (sortAsc) {
            list.sortBy { it.length } //正序排列
        } else {
            list.sortByDescending { it.length }  //反序排列
        }
        listener(list)
    }
}