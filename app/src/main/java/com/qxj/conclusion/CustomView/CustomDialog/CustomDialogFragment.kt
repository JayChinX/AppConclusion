package com.qxj.conclusion.CustomView.CustomDialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

class CustomDialogFragment: DialogFragment(){

    private lateinit var mCancelListener: OnDialogCancelListener

    fun isCancelListener(): Boolean= ::mCancelListener.isInitialized
    
    interface OnDialogCancelListener {
        fun onCancel()
    }

    interface OnCallDialog {
        fun getDialog(context: Context): Dialog
    }

    companion object {
//        var mCancelListener: OnDialogCancelListener? = null
        var mOnCallDialog: OnCallDialog? = null

        fun newInstance(call: OnCallDialog, cancelable: Boolean) : CustomDialogFragment {
            return newInstance(call, cancelable, null)
        }

        fun newInstance(call: OnCallDialog, cancelable: Boolean, cancelListener: OnDialogCancelListener?) : CustomDialogFragment {
            var instance = CustomDialogFragment()
            instance.isCancelable = cancelable
            if (cancelListener != null) {
                instance.mCancelListener = cancelListener
            }
            this.mOnCallDialog = call
            return instance
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return mOnCallDialog!!.getDialog(activity)
    }


    @SuppressLint("ObsoleteSdkInt")
    override fun onStart() {
        super.onStart()
        val dialog: Dialog = dialog
        //在5.0以下的版本会出现白色背景边框，若在5.0以上设置则会造成文字部分的背景也变成透明
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //目前只有这两个dialog会出现边框
            if(dialog is ProgressDialog || dialog is DatePickerDialog) {
                getDialog().window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
        val window = getDialog().window
        val windowParams: WindowManager.LayoutParams = window.attributes
        windowParams.dimAmount = 0.0f
        window.attributes = windowParams
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        if (isCancelListener()) mCancelListener!!.onCancel()
    }
}