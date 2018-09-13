package com.qxj.conclusion.CustomView.CustomDialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.FragmentManager
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.qxj.conclusion.AppConfig
import com.qxj.conclusion.CustomView.CustomDialog.CustomDialogFragment.Companion.newInstance
import com.qxj.conclusion.R
import kotlinx.android.synthetic.main.alert_dialog.view.*

class DialogFragmentHelper {
    private val TAG: String = DialogFragmentHelper::class.java.name

    private val PROGRESS_THEME = R.style.Base_AlertDialog

    private val PROGRESS_TAG = TAG + ":progress"

    fun showProgress(fragmentManager: FragmentManager, msg: String): CustomDialogFragment {
        return showProgress(fragmentManager, msg, true, null)
    }

    fun showProgress(fragmentManager: FragmentManager, msg: String, cancelable: Boolean): CustomDialogFragment {
        return showProgress(fragmentManager, msg, cancelable, null)
    }

    fun showProgress(fragmentManager: FragmentManager, msg: String, cancelable: Boolean, cancelListener: CustomDialogFragment.OnDialogCancelListener?): CustomDialogFragment {

        val dialogFragment: CustomDialogFragment = newInstance(object : CustomDialogFragment.OnCallDialog {
            override fun getDialog(context: Context): Dialog {
                var progressDialog = ProgressDialog(context, PROGRESS_THEME)
                progressDialog.setMessage(msg)
                return progressDialog
            }
        }, cancelable, cancelListener)

        dialogFragment.show(fragmentManager, PROGRESS_TAG)
        return dialogFragment
    }

    private val INSERT_THEME = R.style.Base_AlertDialog

    private val INSERT_TAG = TAG + ":insert"

    fun showInsertDialog(fragmentManager: FragmentManager, title: String, msg: String, leftString: String, rightString: String, resultListener: IDialogResultListener<String>, cancelable: Boolean) {
        val dialogFragment: CustomDialogFragment = newInstance(object : CustomDialogFragment.OnCallDialog {
            override fun getDialog(context: Context): Dialog {

                /**自定义**/
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                val view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)

                view.tv_alert_title.text = title
                view.tv_alert_negative.text = leftString
                view.tv_alert_positive.text = rightString
                view.tv_alert_message.text = msg

                view.tv_alert_negative.setBackgroundColor(context.resources.getColor(R.color.switch_thumb_material))
                view.tv_alert_negative.setTextColor(context.resources.getColor(R.color.switch_thumb_disabled))

//                view.tv_alert_positive.setBackgroundColor(context.resources.getColor(R.color.switch_thumb_material))
//                view.tv_alert_positive.setTextColor(context.resources.getColor(R.color.switch_thumb_disabled))

                view.tv_alert_message.textSize = 16F

                view.tv_alert_title.visibility = View.VISIBLE
                view.tv_alert_message.visibility = View.VISIBLE
                view.tv_alert_negative.visibility = View.VISIBLE
                view.tv_alert_positive.visibility = View.VISIBLE


                val dialog = builder.create()
                dialog.show()
                dialog.setContentView(view)

                view.tv_alert_negative.setOnClickListener {
                    dialog.dismiss()
                    resultListener.onDataResult(leftString)

                }
                view.tv_alert_positive.setOnClickListener {
                    dialog.dismiss()
                    resultListener.onDataResult(rightString)

                }

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

    fun showInsertDialog(fragmentManager: FragmentManager, title: String, msg: String, rightString: String, resultListener: IDialogResultListener<String>, cancelable: Boolean) {
        val dialogFragment: CustomDialogFragment = newInstance(object : CustomDialogFragment.OnCallDialog {
            override fun getDialog(context: Context): Dialog {

                /**自定义**/
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)

                val view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)
                view.tv_alert_title.text = title
                view.tv_alert_positive_only.text = rightString
                view.tv_alert_message.text = msg

//                view.tv_alert_positive_only.setTextColor(context.resources.getColor(R.color.switch_thumb_disabled))

                view.tv_alert_message.textSize = 16F

                view.tv_alert_title.visibility = View.VISIBLE
                view.tv_alert_message.visibility = View.VISIBLE
                view.tv_alert_positive_only.visibility = View.VISIBLE

                val dialog = builder.create()
                dialog.show()
                dialog.setContentView(view)

                view.tv_alert_positive_only.setOnClickListener {
                    dialog.dismiss()
                    resultListener.onDataResult(rightString)

                }

                return dialog
            }
        }, cancelable, null)
        dialogFragment.show(fragmentManager, INSERT_TAG)
    }
}