package com.qxj.conclusion.mvp.presenter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import com.qxj.commonbase.mvpbase.IPresenter
import com.qxj.conclusion.UserBean
import com.qxj.conclusion.api.ApiService
import com.qxj.conclusion.api.async
import com.qxj.conclusion.api.bindLifecycle
import com.qxj.conclusion.api.subscribes
import com.qxj.conclusion.mvp.model.LoginModel
import java.util.*

class LoginPresenter(view: LoginContract.LoginView) :
        LoginContract.LoginPresenter,
        IPresenter<LoginContract.LoginView>(view) {

    private val TAG: String = LoginPresenter::class.java.name

    private lateinit var model: LoginModel

    private lateinit var owner: LifecycleOwner

    override fun initData(intent: Intent) {
        val type = intent.getStringExtra("type")
        model = LoginModel()
    }

    override fun initLifecycle(owner: LifecycleOwner) {
        this.owner = owner
    }

    @SuppressLint("CheckResult")
    override fun login(name: String, password: String) {
        //用户名和密码检查合法性
        if (!model.checkLogin(name, password) { msg: String ->
                    mView.get()?.loginFiled(msg)
                }) return

        val map = HashMap<String, String>()
        map["userId"] = "H2409761"
        ApiService.getUserLocation<UserBean>(Gson().toRequestBody(map))
                .async(100)//扩展函数
                .doOnSubscribe {
                    //开始网络请求
                    mView.get()?.showLoading()
                }
                .doAfterTerminate {
                    //网络请求完毕
                    mView.get()?.hideLoading()
                }
                .bindLifecycle(owner)//扩展函数绑定生命周期
                .subscribe({ data ->
                    data?.let {
                        it.data.let {
                            Log.d(TAG, "请求成功${it!![0].name}")
                        }
                    }
                }, { e: Throwable ->

                })
        ApiService.login<UserBean>(name, password)
                .async(100)//扩展函数
                .doOnSubscribe {
                    //开始网络请求
                    mView.get()?.showLoading()
                }
                .doAfterTerminate {
                    //网络请求完毕
                    mView.get()?.hideLoading()
                }
                .bindLifecycle(owner)//扩展函数绑定生命周期
                .subscribes({

                }, {

                })//自定义范围数据拦截器




    }

}

