package com.geely.gic.hmi.ui.main

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext


fun setStatusBarColor(activity: Activity) {
    //Android6.0（API 23）以上，系统方法
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val window = activity.window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

//        window.navigationBarColor = Color.parseColor("#ffffff")
    }
}
//
//private val mBagOfTags = HashMap<String, Any>()
//
//
//private const val JOB_KEY = "JOB_KEY"
//
//val ViewModel.viewModelScope: CoroutineScope
//    get() {
//        val scope: CoroutineScope? = this.getTag(JOB_KEY)
//        if (scope != null) {
//            return scope
//        }
//        return setTagIfAbsent(JOB_KEY,
//                CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main))
//    }
//
//
//internal fun <T> setTagIfAbsent(key: String, newValue: T): T {
//    val previous: T?
//    synchronized(mBagOfTags) {
//
//        previous = mBagOfTags[key] as T?
//        if (previous == null) {
//            mBagOfTags[key] = newValue
//        }
//    }
//    val result = previous ?: newValue
//    if (mCleared) {
//        // It is possible that we'll call close() multiple times on the same object, but
//        // Closeable interface requires close method to be idempotent:
//        // "if the stream is already closed then invoking this method has no effect." (c)
//        closeWithRuntimeException(result)
//    }
//    return result
//}
//
//private fun closeWithRuntimeException(obj: Any) {
//    if (obj is Closeable) {
//        try {
//            obj.close()
//        } catch (e: IOException) {
//            throw RuntimeException(e)
//        }
//
//    }
//}

val ViewModel.viewModelScope: CoroutineScope
    get() {
        return CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}