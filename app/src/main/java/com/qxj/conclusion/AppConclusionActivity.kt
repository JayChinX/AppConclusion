package com.qxj.conclusion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.qxj.conclusion.ConclusionUtils.ConfigPreference
import com.qxj.conclusion.ConclusionUtils.PermissionUtil
import com.qxj.conclusion.CustomView.CustomDialog.DialogFragmentHelper
import com.qxj.conclusion.CustomView.CustomDialog.IDialogResultListener

open class AppConclusionActivity : Activity() {
    
    private val TAG = AppConclusionActivity::class.java.name

    protected var variable by ConfigPreference("wakeupStatus", true)
    protected var userId by ConfigPreference("userId", "ID-unKnown")
    protected var userName by ConfigPreference("userName", "Name-unKnown")
    protected var userAccount by ConfigPreference("account", "A-unKnown")
    protected var userPassword by ConfigPreference("password", "P-unKnown")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)

//        permissionUtil.checkStoragePermission(Runnable{
//            LogTool.d(TAG, "有权限")
//            toast("有权限")
//            longToast("有权限")
//        })

    }

    fun showPermissionDialog(message: String) {
        showAlertDialog("权限", message, "取消", "去开启", null, object : PermissionUtil.OnClickListener {
            override fun onClick() {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }, true)
    }

    fun showAlertDialog(title: String, message: String, leftStr: String?, rightStr: String?, leftListener: PermissionUtil.OnClickListener?, rightListener: PermissionUtil.OnClickListener?, cancelable: Boolean) {

        if (null != leftStr && null != rightStr) {
            DialogFragmentHelper().showInsertDialog(fragmentManager, title,message, leftStr, rightStr, object : IDialogResultListener<String> {
                override fun onDataResult(result: String) {
                    Log.d(TAG, "onClick $result")
                    when (result) {
                        leftStr -> {
                            leftListener?.onClick()
                        }
                        rightStr -> {
                            rightListener?.onClick()
                        }
                    }
                }
            }, cancelable)
        } else if (null != rightStr) {
            DialogFragmentHelper().showInsertDialog(fragmentManager, title,message, rightStr, object : IDialogResultListener<String> {
                override fun onDataResult(result: String) {
                    Log.d(TAG, "onClick $result")
                    when (result) {
                        leftStr -> {
                            leftListener?.onClick()
                        }
                        rightStr -> {
                            rightListener?.onClick()
                        }
                    }
                }
            }, cancelable)
        }

    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
}
