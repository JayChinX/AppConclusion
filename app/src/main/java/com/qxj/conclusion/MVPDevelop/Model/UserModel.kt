package com.qxj.conclusion.MVPDevelop.Model

object UserModel {
    fun addUser(name: String, listener: (Boolean) -> Unit) {
        //添加任务

        if (name == "秦小杰") {
            listener(true)
        } else {
            listener(false)
        }
    }
}