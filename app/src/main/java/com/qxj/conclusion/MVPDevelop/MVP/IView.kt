package com.qxj.conclusion.MVPDevelop.MVP

import android.content.Context
import android.widget.Toast

interface IView {

    val mPresenter: IPresenter<out IView>

    fun initView()

    fun showProgressDialog() {}

    fun dismissProgressDialog() {}

    fun showToast(text: String, context: Context, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, text, time).show()
    }
}