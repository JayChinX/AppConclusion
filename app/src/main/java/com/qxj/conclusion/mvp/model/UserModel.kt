package com.qxj.conclusion.mvp.model

import com.qxj.conclusion.util.PermissionUtil
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

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

    fun toLoginUser(name: String, password: String, listener: (Boolean, String) -> Unit) {
        listener(true, "登录成功")
//        val user = mApi.getUser("", "")
//        user.enqueue(object : Callback<ResponseBody> {
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//        })
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