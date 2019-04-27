//package com.qxj.conclusion.mvvm.model
//
//import androidx.lifecycle.LifecycleOwner
//import com.google.gson.Gson
//import com.qxj.commonbase.network.*
//
//class VMLoginModel {
//
//    fun getUserLogin(owner: LifecycleOwner,
//                     doOn: () -> Unit,
//                     doAfter: () -> Unit,
//                     success: (String) ->Unit,
//                     error: (String) -> Unit) {
//        val map = HashMap<String, String>()
//        map["userId"] = "H2409761"
//        ApiService.getUserLocation<UserBean>(Gson().toRequestBody(map))
//                .async(100)//扩展函数
//                .doOnSubscribe {
//                    //开始网络请求
//                    doOn()
//                }
//                .doAfterTerminate {
//                    //网络请求完毕
//                    doAfter()
//                }
//                .bindLifecycle(owner)//扩展函数绑定生命周期
//                .subscribe({ data ->
//                    data?.let { it ->
//                        it.message.let {
//                            success(it!![0].name)
//                        }
//                    }
//                }, {
//                    e: Throwable ->
//                    when(e.message) {
//                        "" -> {}
//                        else -> error("失败")
//                    }
//                })
//    }
//}