package com.qxj.conclusion

import android.os.Bundle
import android.view.View
import com.qxj.conclusion.ConclusionUtils.LogTool
import com.qxj.conclusion.CustomView.CustomDialog.CustomDialogFragment
import com.qxj.conclusion.CustomView.CustomDialog.DialogFragmentHelper
import com.qxj.conclusion.CustomView.CustomDialog.IDialogResultListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppConclusionActivity(), View.OnClickListener {

    private val TAG: String = LoginActivity::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dialog.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.dialog -> {
                var dialog = DialogFragmentHelper()
                dialog.showInsertDialog(fragmentManager, "警告","没有权限", "取消", "去开启", object : IDialogResultListener<String>{
                    override fun onDataResult(result: String) {
                        LogTool.d(TAG, "点击$result")
                    }
                }, true)
            }
        }
    }
}