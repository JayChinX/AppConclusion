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
        permissionUtil.checkStoragePermission(Runnable{
            LogTool.d(TAG, "有权限")
        })
    }

    fun showPermissionDialog(message: String) {
        showAlertDialog(message, "取消", "去开启", null, object : OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        })
    }
    interface OnClickListener {
        fun onClick(v: View)
    }

    private fun showAlertDialog(message: String, leftStr: String, rightStr: String, leftListener: OnClickListener?, rightListener: OnClickListener?) {
        val dialog = Dialog(this, R.style.mydialogstyle)
        val view = inflate(this, R.layout.alert_dialog, null)
        dialog.setContentView(view)
        view.tv_alert_negative.text = leftStr
        view.tv_alert_positive.text = rightStr
        view.tv_alert_message.text = message
        view.tv_alert_negative.setOnClickListener { v ->
            leftListener?.onClick(v)
            dialog.dismiss()
        }
        view.tv_alert_positive.setOnClickListener { v ->
            rightListener?.onClick(v)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
