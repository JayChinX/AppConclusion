package com.qxj.commonbase.mvpbase

import android.content.Context
import android.widget.Toast

interface IView {
    //所有的UI操作都在这里声明接口方法

    val mPresenter: IPresenter<out IView>

    fun initView()

    fun showLoading()

    fun hideLoading()

    fun onError()

    fun showToast(text: String, context: Context, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, time).show()
    }
}