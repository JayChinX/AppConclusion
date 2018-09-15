package com.qxj.conclusion.MVPDevelop.View

import android.content.Context
import android.widget.Toast
import com.qxj.conclusion.MVPDevelop.MVP.IPresenter

interface IView {
    //所有的UI操作都在这里声明接口方法

    val mPresenter: IPresenter<out IView>

    fun initView()

    fun startCheckPermission()

    fun showAlertDialog() {}

    fun showProgressDialog() {}

    fun dismissProgressDialog() {}

    fun showToast(text: String, context: Context, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, time).show()
    }
}