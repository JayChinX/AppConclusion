package com.qxj.conclusion.mvp.base

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.os.Bundle
import java.lang.ref.SoftReference

abstract class IPresenter<T : IView>(v: T) {

    open var mView: SoftReference<T> = SoftReference(v)

    open fun onCreate(intent: Intent) {
        mView.get()?.initView()
        initData(intent)
    }
    open fun initData(intent: Intent) {}

    abstract fun initLifecycle(owner: LifecycleOwner)
}