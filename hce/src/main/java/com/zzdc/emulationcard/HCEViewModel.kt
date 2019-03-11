package com.zzdc.emulationcard

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.nfc.Tag
import android.util.Log

class HCEViewModel(owner: LifecycleOwner) {
    private val TAG = HCEViewModel::class.java.simpleName

    var hce = HCE()
    var hceSupport: ObservableBoolean = ObservableBoolean(false)
    var model = HCEModel()

    fun checkHEC(context: Context) {
        val pm = context.packageManager
        val hasNfcHce = pm.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)
        hceSupport.set(hasNfcHce)
        Log.e(TAG, if (hasNfcHce) "支持HCE功能" else "不支持HCE功能")
    }

    fun read(tag: Tag) {
        val msg = model.load(tag)
        hce.msg.set(msg)
        Log.e(TAG, "enableReaderMode 读到的信息$msg")
    }

    class HCE constructor(var id: ObservableInt, var name: ObservableField<String>, var msg: ObservableField<String>) {
        constructor(id: Int = 0, name: String = "", msg: String = "")
                : this(ObservableInt(id), ObservableField(name), ObservableField(msg))
    }
}