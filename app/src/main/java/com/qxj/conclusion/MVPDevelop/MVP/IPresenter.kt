package com.qxj.conclusion.MVPDevelop.MVP

import android.content.Intent
import android.os.Bundle
import java.lang.ref.SoftReference

abstract class IPresenter<T : IView>(v: T) {
    open var mView: SoftReference<T> = SoftReference(v)

    open fun onCreate(intent: Intent) {
        mView.get()?.initView()
    }

    open fun onStart() {}
    open fun onResume() {}
    open fun onPause() {}
    open fun onStop() {}
    open fun onDestroy() {}
    open fun onCreateView(arguments: Bundle?) {}
}