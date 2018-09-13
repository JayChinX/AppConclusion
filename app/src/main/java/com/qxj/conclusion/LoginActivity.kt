package com.qxj.conclusion

import android.app.AlertDialog
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.qxj.conclusion.ConclusionUtils.LogTool
import com.qxj.conclusion.CustomView.CustomDialog.CustomDialogFragment
import com.qxj.conclusion.CustomView.CustomDialog.DialogFragmentHelper
import com.qxj.conclusion.CustomView.CustomDialog.IDialogResultListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.alert_dialog.view.*

class LoginActivity : AppConclusionActivity(), View.OnClickListener {

    private val TAG: String = LoginActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dialog.setOnClickListener(this)
        dialog1.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.dialog -> {
                this.showAlertDialog("警告", "网络连接失败！", "取消", "重新连接", null, object : OnClickListener{
                    override fun onClick() {
                        LogTool.d(TAG, "重新连接")
                    }
                }, true)
            }
            R.id.dialog1 -> {
                //trimMargin()//删除前置空格  trimMargin("..")删除字符
                this.showAlertDialog("更新提醒", """
                    |更新内容：
                    |   1.修复BUG。
                    |   2.更新新特性
                    """.trimMargin(), null, "现在更新", null, object : OnClickListener{
                    override fun onClick() {
                        LogTool.d(TAG, "现在更新")
                    }
                }, false)
            }
        }
    }
}