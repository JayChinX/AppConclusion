package com.qxj.conclusion.mvvm.view

import android.view.View

interface Presenter: View.OnClickListener {
    override fun onClick(v: View?)
}