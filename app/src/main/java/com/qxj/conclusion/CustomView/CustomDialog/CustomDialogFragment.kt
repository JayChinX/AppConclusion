package com.qxj.conclusion.CustomView.CustomDialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import com.qxj.conclusion.AppConfig
import com.qxj.conclusion.ConclusionUtils.LogTool
import com.qxj.conclusion.R

class CustomDialogFragment: DialogFragment(){

    private lateinit var mCancelListener: OnDialogCancelListener

    fun isCancelListener(): Boolean= ::mCancelListener.isInitialized

    private val INSERT_ANI = R.style.Base_Animation
    
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

        val dm = context.resources.displayMetrics
        val height = dm.heightPixels
        val width = dm.widthPixels

        val configuration: Configuration = context.resources.configuration
        val ori: Int = configuration.orientation //屏幕状态

        val window = dialog.window

        window.setWindowAnimations(INSERT_ANI)
        //消除白边
        window.setBackgroundDrawable(BitmapDrawable())
        window.setBackgroundDrawableResource(android.R.color.transparent)

        val windowParams: WindowManager.LayoutParams = window.attributes

        when(ori) {
            Configuration.ORIENTATION_LANDSCAPE -> windowParams.width = (height * 0.9).toInt()
            Configuration.ORIENTATION_PORTRAIT -> windowParams.width = (width * 0.9).toInt()
        }

        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        windowParams.dimAmount = 0.5f //遮罩透明度
        window.attributes = windowParams
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        if (isCancelListener()) mCancelListener!!.onCancel()
    }
}