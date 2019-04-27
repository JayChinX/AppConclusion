package com.qxj.conclusion.view.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.FragmentManager
import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import com.qxj.conclusion.view.dialog.CustomDialogFragment.Companion.newInstance
import com.qxj.conclusion.R
import kotlinx.android.synthetic.main.alert_dialog.view.*
import kotlinx.android.synthetic.main.alert_dialog_message_text.view.*
import kotlinx.android.synthetic.main.alert_dialog_select_only.view.*
import kotlinx.android.synthetic.main.alert_dialog_select_two.view.*
import kotlinx.android.synthetic.main.alert_dialog_title_text.view.*

class DialogFragmentHelper {
    private val TAG: String = DialogFragmentHelper::class.java.name

    private val PROGRESS_THEME = R.style.Base_AlertDialog

    private val PROGRESS_TAG = TAG + ":progress"

    companion object {
        var INSERT_ANIMATION_01 = R.style.Base_Animation
    }

    fun showProgress(fragmentManager: FragmentManager, msg: String): CustomDialogFragment {
        return showProgress(fragmentManager, msg, true, null)
    }

    fun showProgress(fragmentManager: FragmentManager, msg: String, cancelable: Boolean): CustomDialogFragment {
        return showProgress(fragmentManager, msg, cancelable, null)
    }

    fun showProgress(fragmentManager: FragmentManager, msg: String, cancelable: Boolean, cancelListener: CustomDialogFragment.OnDialogCancelListener?): CustomDialogFragment {

        val dialogFragment: CustomDialogFragment = newInstance(INSERT_ANIMATION_01, object : CustomDialogFragment.OnCallDialog {
            override fun getDialog(context: Context): Dialog {
                val progressDialog = ProgressDialog(context, PROGRESS_THEME)
                progressDialog.setMessage(msg)
                return progressDialog
            }
        }, cancelable, cancelListener)

        dialogFragment.show(fragmentManager, PROGRESS_TAG)
        return dialogFragment
    }

    private val INSERT_THEME = R.style.Base_AlertDialog

    private val INSERT_TAG = TAG + ":insert"

    fun showInsertDialog(fragmentManager: FragmentManager, animation: Int?, title: String, msg: String, leftString: String?, rightString: String?, resultListener: IDialogResultListener<String>, cancelable: Boolean) {
        val dialogFragment: CustomDialogFragment = newInstance(animation, object: CustomDialogFragment.OnCallDialog {
            @SuppressLint("InflateParams")
            override fun getDialog(context: Context): Dialog {

                /**自定义**/
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                val view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)

                val dialog = builder.create()
                dialog.show()
                //标题
                view.tv_alert_title.layoutResource = R.layout.alert_dialog_title_text
                view.tv_alert_title.inflate()
                view.tv_dialog_title.text = title
                //信息
                view.tv_alert_message.layoutResource = R.layout.alert_dialog_message_text
                view.tv_alert_message.inflate()
                view.tv_dialog_message.text = msg
                //选项
                if (!TextUtils.isEmpty(leftString) && !TextUtils.isEmpty(rightString)) {
                    view.tv_alert_select.layoutResource = R.layout.alert_dialog_select_two
                    view.tv_alert_select.inflate()
                    view.tv_alert_negative.text = leftString
                    view.tv_alert_positive.text = rightString

                    view.tv_alert_negative.setOnClickListener {
                        dialog.dismiss()
                        resultListener.onDataResult(leftString!!)
                    }
                    view.tv_alert_positive.setOnClickListener {
                        dialog.dismiss()
                        resultListener.onDataResult(rightString!!)
                    }

                } else if (!TextUtils.isEmpty(rightString)){
                    view.tv_alert_select.layoutResource = R.layout.alert_dialog_select_only
                    view.tv_alert_select.inflate()
                    view.tv_alert_positive_only.text = rightString

                    view.tv_alert_positive_only.setOnClickListener {
                        dialog.dismiss()
                        resultListener.onDataResult(rightString!!)
                    }
                }

                dialog.setContentView(view)

                return dialog

                /**原生**/
//                val builder: AlertDialog.Builder = AlertDialog.Builder(context, INSERT_THEME)
//                builder.setTitle(title)
//                builder.setMessage(msg)
//
//
//                builder.setPositiveButton(rightString) { _, _ ->
//                    resultListener.onDataResult(rightString)
//                }
//                builder.setNegativeButton(leftString) { _, _ ->
//                    resultListener.onDataResult(leftString)
//                }
//                return builder.create()
            }
        }, cancelable, null)
        dialogFragment.show(fragmentManager, INSERT_TAG)
    }
}