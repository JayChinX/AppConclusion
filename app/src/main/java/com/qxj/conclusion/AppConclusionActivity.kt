package com.qxj.conclusion

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.View.inflate
import com.qxj.conclusion.ConclusionUtils.ConfigPreference
import com.qxj.conclusion.ConclusionUtils.LogTool
import com.qxj.conclusion.ConclusionUtils.PermissionUtil
import kotlinx.android.synthetic.main.alert_dialog.view.*
import com.qxj.conclusion.AppConfig.win_height
import com.qxj.conclusion.AppConfig.win_width
import com.qxj.conclusion.CustomView.CustomDialog.DialogFragmentHelper
import com.qxj.conclusion.CustomView.CustomDialog.IDialogResultListener


open class AppConclusionActivity : AppCompatActivity() {
    
    private val TAG = AppConclusionActivity::class.java.name

    private var variable by ConfigPreference("wakeupStatus", true)
    private var userId by ConfigPreference("userId", "ID-unKnown")
    private var userName by ConfigPreference("userName", "Name-unKnown")
    private var userAccount by ConfigPreference("account", "A-unKnown")
    private var userPassword by ConfigPreference("password", "P-unKnown")

    private val permissionUtil: PermissionUtil = PermissionUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)

        //计算屏幕宽高
        val dm = resources.displayMetrics
        win_height = dm.heightPixels
        win_width = dm.widthPixels


        permissionUtil.checkStoragePermission(Runnable{
            LogTool.d(TAG, "有权限")
        })

    }

    fun showPermissionDialog(message: String) {
        showAlertDialog("权限", message, "取消", "去开启", null, object : OnClickListener {
            override fun onClick() {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }, true)
    }
    interface OnClickListener {
        fun onClick()
    }

    fun showAlertDialog(title: String, message: String, leftStr: String?, rightStr: String?, leftListener: OnClickListener?, rightListener: OnClickListener?, cancelable: Boolean) {

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
