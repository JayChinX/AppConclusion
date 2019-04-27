package com.qxj.conclusion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.qxj.conclusion.view.dialog.DialogFragmentHelper
import com.qxj.conclusion.view.dialog.IDialogResultListener

open class AppConclusionActivity : Activity() {

    private val TAG = AppConclusionActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)
    }

    fun showPermissionDialog(message: String) {
        showAlertDialog("权限", message, "取消", "去开启", true, null
                ) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
        }

    }

    fun showAlertDialog(title: String, message: String, leftStr: String?, rightStr: String?, cancelable: Boolean,
                        leftListener: ((Boolean) -> Unit)?,
                        rightListener: ((Boolean) -> Unit)?) {
        DialogFragmentHelper().showInsertDialog(fragmentManager, DialogFragmentHelper.INSERT_ANIMATION_01,
                title, message, leftStr, rightStr, object : IDialogResultListener<String> {
            override fun onDataResult(result: String) {
                Log.d(TAG, "onClick $result")
                when (result) {
                    leftStr -> {
                        leftListener?.let { it(false) }
                    }
                    rightStr -> {
                        rightListener?.let { it(true) }
                    }
                }
            }
        }, cancelable)
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
}
