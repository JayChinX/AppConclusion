package com.qxj.conclusion.CustomView.CustomDialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.FragmentManager
import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.qxj.conclusion.AppConfig.win_width
import com.qxj.conclusion.R
import kotlinx.android.synthetic.main.alert_dialog.view.*
import kotlinx.android.synthetic.main.alert_dialog_amount.view.*

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

        val dialogFragment: CustomDialogFragment = CustomDialogFragment.newInstance(object : CustomDialogFragment.OnCallDialog {
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

    private val INSERT_ANI = R.style.Base_Animation

    fun showInsertDialog(fragmentManager: FragmentManager, title: String, msg: String, leftString: String, rightString: String, resultListener: IDialogResultListener<String>, cancelable: Boolean) {
        val dialogFragment: CustomDialogFragment = CustomDialogFragment.newInstance(object : CustomDialogFragment.OnCallDialog {
            override fun getDialog(context: Context): Dialog {


                /**自定义**/
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)

                val view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)
                view.tv_alert_title.text = title
                view.tv_alert_negative.text = leftString
                view.tv_alert_positive.text = rightString
                view.tv_alert_message.text = msg
                //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
                val dialog = builder.create()
                dialog.show()
                val window: Window = dialog.window

                window.setWindowAnimations(INSERT_ANI)
//                //消除棱角
                window.setBackgroundDrawableResource(android.R.color.transparent)
                val vm: WindowManager.LayoutParams = window.attributes
//                vm.alpha = 0.5f
                vm.width = (win_width * 0.8).toInt()
                vm.dimAmount = 0.5f
                window.attributes = vm
//                dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置

                window.setContentView(view)

                view.tv_alert_negative.setOnClickListener {
                    dialog.dismiss()
                    resultListener.onDataResult(leftString)

                }
                view.tv_alert_positive.setOnClickListener {
                    dialog.dismiss()
                    resultListener.onDataResult(rightString)

                }

                return dialog
                /**有遮罩**/

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