package com.qxj.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import com.qxj.dialog.DialogFragmentHelper.Type.*
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.jetbrains.anko.*


fun Fragment.showDialog(title: String, msg: String) {

    val dialog = DialogFragmentHelper.Builder()
            .title(title)
            .msg(msg)
            .left("取消")
            .right("确定")
            .builder()
    dialog.show(fragmentManager = fragmentManager!!)
}

fun Fragment.showDialog(title: String, msg: String, resultListener: (Boolean) -> Unit) {

    val dialog = DialogFragmentHelper.Builder()
            .title(title)
            .msg(msg)
            .left("取消")
            .right("确定")
            .setOnListener(resultListener)
            .builder()
    dialog.show(fragmentManager = fragmentManager!!)
}

fun AppCompatActivity.showProgress() {

    val dialog = DialogFragmentHelper.Builder()
            .type(PROGRESS)
            .builder()
    dialog.show(fragmentManager = supportFragmentManager)
}

fun AppCompatActivity.showDialog(title: String, msg: String) {

    val dialog = DialogFragmentHelper.Builder()
            .title(title)
            .msg(msg)
            .left("取消")
            .right("确定")
            .builder()
    dialog.show(fragmentManager = supportFragmentManager)
}

fun AppCompatActivity.showDialog(title: String, msg: String, resultListener: (Boolean) -> Unit) {

    val dialog = DialogFragmentHelper.Builder()
            .title(title)
            .msg(msg)
            .left("取消")
            .right("确定")
            .setOnListener(resultListener)
            .builder()
    dialog.show(fragmentManager = supportFragmentManager)
}

private class DialogFragmentHelper private constructor() {

    companion object {
        private const val INSERT_TAG = "AlertDialogHelper"
    }

    internal class Builder {
        private var title: String = "提示"
        private var msg: String = ""
        private var cancelable = true
        private var leftString = "取消"
        private var rightString = "确定"

        private var type = NORMAL

        private var resultListener: ((Boolean) -> Unit)? = null

        fun type(type: Type): Builder {
            this.type = type
            return this
        }

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun msg(msg: String): Builder {
            this.msg = msg
            return this
        }

        fun left(leftString: String): Builder {
            this.leftString = leftString
            return this
        }

        fun right(rightString: String): Builder {
            this.rightString = rightString
            return this
        }

        fun cancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setOnListener(resultListener: (Boolean) -> Unit): Builder {
            this.resultListener = resultListener
            return this
        }

        fun builder(): DialogFragmentHelper {
            val dialogFragmentHelper = DialogFragmentHelper()
            when (type) {
                SUCCESS -> TODO()
                ERROR -> TODO()
                WARNING -> TODO()
                IMAGE -> TODO()
                PROGRESS -> dialogFragmentHelper.createProgressDialog()
                NORMAL -> dialogFragmentHelper.createInsertDialog(
                        title, msg, leftString, rightString, resultListener, cancelable)
            }

            return dialogFragmentHelper
        }
    }

    private lateinit var dialogFragment: CustomDialogFragment

    private fun createInsertDialog(
            title: String,
            msg: String,
            leftString: String?,
            rightString: String?,
            resultListener: ((Boolean) -> Unit)?,
            cancelable: Boolean) {
        dialogFragment = CustomDialogFragment.newInstance(object : CustomDialogFragment.OnCallDialog {
            @SuppressLint("NewApi")
            override fun getDialog(context: Context): Dialog {

                /**自定义**/
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                val dialog = builder.create()
                dialog.show()

                val dm = context.resources.displayMetrics
                val height = dm.heightPixels
                val width = dm.widthPixels

                val view = context.verticalLayout {

                    textView {

                        gravity = Gravity.CENTER
                        background = getContext().getDrawable(R.drawable.line_bottom)

                        text = title
                        textSize = 16f
                        textColor = getContext().getColor(R.color.text_block)
                    }.lparams(width = matchParent, height = width / 8) {
                        marginStart = 20
                        marginEnd = 20
                    }

                    textView {
                        gravity = Gravity.CENTER
                        background = getContext().getDrawable(R.drawable.line_bottom)

                        text = msg
                        textSize = 16f
                        textColor = getContext().getColor(R.color.text_block)
                    }.lparams(width = matchParent, height = width / 4) {
                        marginStart = 20
                        marginEnd = 20
                    }

                    verticalLayout {
                        orientation = LinearLayout.HORIZONTAL

                        if (!TextUtils.isEmpty(leftString) && !TextUtils.isEmpty(rightString)) {
                            textView {
                                foreground = getContext().getDrawable(R.drawable.item_select)
                                gravity = Gravity.CENTER
                                setOnClickListener {
                                    dialog.dismiss()
                                    resultListener?.invoke(false)
                                }

                                text = leftString
                            }.lparams(width = matchParent, height = width / 8, weight = 1f)

                            view {
                                background = getContext().getDrawable(R.drawable.line_ver)
                            }.lparams(width = 1, height = width / 8 - 40) {
                                gravity = Gravity.CENTER
                            }
                        }

                        textView {
                            gravity = Gravity.CENTER
                            foreground = getContext().getDrawable(R.drawable.item_select)
                            setOnClickListener {
                                dialog.dismiss()
                                resultListener?.invoke(true)
                            }

                            text = rightString
                            textColor = getContext().getColor(R.color.baseAccent)
                        }.lparams(width = matchParent, height = width / 8, weight = 1f)
                    }
                }.apply {
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    background = getContext().getDrawable(R.drawable.bg_dialog)
                }

                dialog.setContentView(view)

                return dialog
            }
        }, cancelable)
    }

    fun show(fragmentManager: FragmentManager) {
        dialogFragment.show(fragmentManager, INSERT_TAG)
    }

    fun createProgressDialog() {
        dialogFragment = CustomDialogFragment.newInstance(object : CustomDialogFragment.OnCallDialog {
            @SuppressLint("NewApi")
            override fun getDialog(context: Context): Dialog {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                val dialog = builder.create()
                dialog.show()

                val view = context.verticalLayout {

                    progressBar()

                }.apply {
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
//                    background = getContext().getDrawable(R.drawable.bg_dialog)
                }

                dialog.setContentView(view)
                return dialog
            }
        }, 0f)
    }

    internal enum class Type {
        SUCCESS,
        ERROR,
        WARNING,
        IMAGE,
        PROGRESS,
        NORMAL

    }

    internal class CustomDialogFragment : DialogFragment() {

        interface OnCancelListener {
            fun onCancel()
        }

        interface OnCallDialog {
            fun getDialog(context: Context): Dialog
        }

        private lateinit var mCancelListener: OnCancelListener
        private lateinit var mOnCallDialog: OnCallDialog

        companion object {
            private var INSERT_ANI = R.style.AlertDialog_AppCompat

            private var dimAmount = 0.5f

            internal fun newInstance(call: OnCallDialog, dim: Float): CustomDialogFragment {
                dimAmount = dim
                return newInstance(call, true, null)
            }

            internal fun newInstance(call: OnCallDialog, cancelable: Boolean): CustomDialogFragment {
                return newInstance(call, cancelable, null)
            }

            internal fun newInstance(call: OnCallDialog, cancelable: Boolean, cancelListener: OnCancelListener?): CustomDialogFragment {
                val instance = CustomDialogFragment()
                instance.isCancelable = cancelable
                if (null != cancelListener) instance.mCancelListener = cancelListener
                instance.mOnCallDialog = call
                return instance
            }
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            super.onCreateDialog(savedInstanceState)
            return mOnCallDialog.getDialog(activity!!)
        }

        override fun onStart() {
            super.onStart()
            dialog?.window?.apply {
                setWindowAnimations(INSERT_ANI)
                //消除白边window.setBackgroundDrawable(BitmapDrawable())

                setBackgroundDrawableResource(android.R.color.transparent)

                val windowParams: WindowManager.LayoutParams = attributes
                val dm = context!!.resources.displayMetrics
                //屏幕状态
                when (context!!.resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> windowParams.width = (dm.heightPixels * 0.8).toInt()
                    Configuration.ORIENTATION_PORTRAIT -> windowParams.width = (dm.widthPixels * 0.8).toInt()
                }

                windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                windowParams.dimAmount = dimAmount //遮罩透明度
                attributes = windowParams
            }
        }

        override fun onCancel(dialog: DialogInterface) {
            super.onCancel(dialog)
            if (::mCancelListener.isInitialized) mCancelListener.onCancel()
        }
    }
}