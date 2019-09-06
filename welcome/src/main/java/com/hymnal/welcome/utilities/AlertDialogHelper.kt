package com.hymnal.welcome.utilities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import org.jetbrains.anko.*
import android.view.WindowManager
import com.hymnal.welcome.utilities.Type.*

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.hymnal.welcome.R
import org.jetbrains.anko.support.v4.UI


fun Fragment.showDialog(title: String, msg: String, resultListener: ((Boolean) -> Unit)? = null) {
    val builder = Builder()
    builder.title(title)
    builder.msg(msg)
    builder.left("取消")
    builder.right("确定")
    if (resultListener != null) builder.setOnListener(resultListener)
    val dialog = builder.builder()
    dialog.show(requireFragmentManager(), "NORMAL")
}

//fun Fragment.showCustomDialog(
//    data: Config,
//    msg: View,
//    resultListener: ((Boolean) -> Unit)? = null
//) {
//    val builder = Builder()
//    builder.type(CUSTOM)
//    builder.title(data.configId)
//    builder.msg(msg)
//    builder.left("取消")
//    builder.right("确定")
//    builder.cancelable(true)
//    if (resultListener != null) builder.setOnListener(resultListener)
//    val dialog = builder.builder()
//    dialog.show(fragmentManager, "CUSTOM")
//}

fun AppCompatActivity.showProgress() {
    val dialog = Builder()
        .type(Type.PROGRESS)
        .builder()
    dialog.show(supportFragmentManager, "PROGRESS")
}

fun AppCompatActivity.showCustomDialog(
    title: String,
    msg: View,
    resultListener: ((Boolean) -> Unit)? = null
) {
    val builder = Builder()
    builder.type(CUSTOM)
    builder.title(title)
    builder.msg(msg)
    builder.left("取消")
    builder.right("确定")
    builder.cancelable(true)
    if (resultListener != null) builder.setOnListener(resultListener)
    val dialog = builder.builder()
    dialog.show(supportFragmentManager, "CUSTOM")
}

fun AppCompatActivity.showDialog(
    title: String,
    msg: String,
    resultListener: ((Boolean) -> Unit)? = null
) {
    val builder = Builder()
    builder.title(title)
    builder.msg(msg)
    builder.left("取消")
    builder.right("确定")
    if (resultListener != null) builder.setOnListener(resultListener)
    val dialog = builder.builder()
    dialog.show(supportFragmentManager, "NORMAL")
}

fun AppCompatActivity.showPermissionDialog(
    title: String,
    msg: String,
    resultListener: ((Boolean) -> Unit)? = null
) {
    val builder = Builder()
    builder.title(title)
    builder.msg(msg)
    builder.left("取消")
    builder.right("去设置")
    builder.cancelable(false)
    if (resultListener != null) builder.setOnListener(resultListener)
    val dialog = builder.builder()
    dialog.show(supportFragmentManager, "NORMAL")
}


internal class Builder {
    private var title: String = "提示"
    private var msg: String = ""
    private var cancelable = true
    private var leftString = "取消"
    private var rightString = "确定"
    private var view: View? = null
    private var run: ((View) -> Unit)? = null
    private var type = Type.NORMAL

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

    fun msg(view: View): Builder {
        this.view = view
        return this
    }

    fun run(run: (View) -> Unit): Builder {
        this.run = run
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

    fun builder(): DialogFragment {
        return when (type) {
            SUCCESS -> TODO()
            ERROR -> TODO()
            WARNING -> TODO()
            IMAGE -> TODO()
            PROGRESS -> createProgressDialog()
            CUSTOM -> createCustomDialog(
                title, view, leftString, rightString, run, resultListener, cancelable
            )
            NORMAL -> createInsertDialog(
                title, msg, leftString, rightString, resultListener, cancelable
            )
        }
    }

    private fun createInsertDialog(
        title: String, msg: String,
        leftString: String?, rightString: String?,
        resultListener: ((Boolean) -> Unit)?, cancelable: Boolean
    ): DialogFragment {
        return NormalDialogFragment.newInstance(object : NormalDialogFragment.OnCallDialog {
            @SuppressLint("NewApi")
            override fun getDialog(context: Context): Dialog {

                /**自定义**/
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                val dialog = builder.create()
                dialog.show()

                val dm = context.resources.displayMetrics
//                val height = dm.heightPixels
                val width = (dm.widthPixels * 0.8).toInt()

                val view = context.verticalLayout {

                    textView {

                        gravity = Gravity.CENTER
                        background = getContext().getDrawable(R.drawable.dialog_line_dash_btm)

                        text = title
                        textSize = 16f
                        textColor = getContext().getColor(R.color.msg_text_block)
                    }.lparams(width = matchParent, height = width / 10) {
                        marginStart = 20
                        marginEnd = 20
                    }

                    textView {
                        gravity = Gravity.CENTER
                        background = getContext().getDrawable(R.drawable.dialog_line_dash_btm)

                        text = msg
                        textSize = 16f
                        textColor = getContext().getColor(R.color.msg_text_block)
                    }.lparams(width = matchParent, height = width / 4) {
                        marginStart = 20
                        marginEnd = 20
                    }

                    verticalLayout {
                        orientation = LinearLayout.HORIZONTAL

                        if (!TextUtils.isEmpty(leftString) && !TextUtils.isEmpty(rightString)) {
                            textView {
                                background =
                                    context.getDrawable(R.drawable.dialog_btn_left_bg_pressed)
                                gravity = Gravity.CENTER
                                setOnClickListener {
                                    dialog.dismiss()
                                    resultListener?.invoke(false)
                                }

                                text = leftString
                            }.lparams(width = matchParent, height = width / 10, weight = 1f)

                            view {
                                background = getContext().getDrawable(R.drawable.dialog_line_dash_ver)
                            }.lparams(width = 1, height = width / 10 - 40) {
                                gravity = Gravity.CENTER
                            }
                        }

                        textView {
                            gravity = Gravity.CENTER
                            background =
                                if (!TextUtils.isEmpty(leftString) && !TextUtils.isEmpty(rightString)) {
                                    context.getDrawable(R.drawable.dialog_btn_right_bg_pressed)
                                } else {
                                    context.getDrawable(R.drawable.dialog_btn_bottom_bg_pressed)

                                }
                            setOnClickListener {
                                dialog.dismiss()
                                resultListener?.invoke(true)
                            }

                            text = rightString
                            textColor = getContext().getColor(R.color.baseAccent)
                        }.lparams(width = matchParent, height = width / 10, weight = 1f)
                    }
                }.apply {
                    layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
                    background = getContext().getDrawable(R.drawable.dialog_bg)
                }

                dialog.setContentView(view)

                return dialog
            }
        }, cancelable)
    }

    private fun createCustomDialog(
        title: String, view: View?,
        leftString: String?, rightString: String?,
        run: ((View) -> Unit)?,
        resultListener: ((Boolean) -> Unit)?, cancelable: Boolean
    ): DialogFragment {
        return CustomDialogFragment.newInstance(
            title,
            view,
            leftString,
            rightString,
            run,
            resultListener,
            cancelable
        )
    }

    private fun createProgressDialog() =
        NormalDialogFragment.newInstance(object : NormalDialogFragment.OnCallDialog {
            @SuppressLint("NewApi")
            override fun getDialog(context: Context): Dialog {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                val dialog = builder.create()
                dialog.show()

                val view = context.verticalLayout {

                    progressBar()

                }.apply {
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
//                    background = getActivity().getDrawable(R.drawable.dialog_bg)
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
    CUSTOM,
    NORMAL

}

internal class NormalDialogFragment : DialogFragment() {

    interface OnCancelListener {
        fun onCancel()
    }

    interface OnCallDialog {
        fun getDialog(context: Context): Dialog
    }

    private lateinit var mCancelListener: OnCancelListener
    private lateinit var mOnCallDialog: OnCallDialog

    companion object {
        private var INSERT_ANI = R.style.AnimPage

        private var dimAmount = 0.5f

        internal fun newInstance(call: OnCallDialog, dim: Float): NormalDialogFragment {
            dimAmount = dim
            return newInstance(call, true, null)
        }

        internal fun newInstance(call: OnCallDialog, cancelable: Boolean): NormalDialogFragment {
            return newInstance(call, cancelable, null)
        }

        private fun newInstance(
            call: OnCallDialog,
            cancelable: Boolean,
            cancelListener: OnCancelListener?
        ): NormalDialogFragment {
            val instance = NormalDialogFragment()
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
                Configuration.ORIENTATION_LANDSCAPE -> windowParams.width =
                    (dm.heightPixels * 0.8).toInt()
                Configuration.ORIENTATION_PORTRAIT -> windowParams.width =
                    (dm.widthPixels * 0.8).toInt()
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

internal class CustomDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(
            title: String, view: View?,
            leftString: String?, rightString: String?,
            run: ((View) -> Unit)?,
            resultListener: ((Boolean) -> Unit)?, cancelable: Boolean
        ): CustomDialogFragment {
            val dialogFragment = CustomDialogFragment()
            dialogFragment.run = run
            dialogFragment.title = title
            dialogFragment.viewCustom = view!!
            dialogFragment.leftString = leftString ?: "取消"
            dialogFragment.rightString = rightString ?: "确定"
            dialogFragment.resultListener = resultListener
            dialogFragment.isCancelable = cancelable
            return dialogFragment
        }
    }

    private var run: ((View) -> Unit)? = null
    private lateinit var title: String
    private lateinit var viewCustom: View
    private lateinit var leftString: String
    private lateinit var rightString: String
    private var resultListener: ((Boolean) -> Unit)? = null

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dm = context!!.resources.displayMetrics
        val width = (dm.widthPixels * 0.8).toInt()
        Log.d("qxj", "初始化完成${dm.widthPixels},${dm.heightPixels},${container?.width}")
        return UI {
            verticalLayout {
                textView {
                    gravity = Gravity.CENTER
                    background = context.getDrawable(R.drawable.dialog_line_dash_btm)
                    text = title
                    textSize = 16f
                    textColor = context.getColor(R.color.msg_text_block)
                }.lparams(width = matchParent, height = width / 10) {
                    marginStart = 20
                    marginEnd = 20
                }
                verticalLayout {
                    gravity = Gravity.CENTER
                    addView(viewCustom)
                    background = context.getDrawable(R.drawable.dialog_line_dash_btm)
                }.lparams(width = matchParent, height = width / 4) {
                    marginStart = 20
                    marginEnd = 20
                }
                verticalLayout {
                    orientation = LinearLayout.HORIZONTAL
                    if (!TextUtils.isEmpty(leftString) && !TextUtils.isEmpty(rightString)) {
                        textView {
                            background = context.getDrawable(R.drawable.dialog_btn_left_bg_pressed)
                            gravity = Gravity.CENTER
                            setOnClickListener {
                                dismiss()
                                resultListener?.invoke(false)
                            }

                            text = leftString
                        }.lparams(width = matchParent, height = width / 10, weight = 1f)

                        view {
                            background = context.getDrawable(R.drawable.dialog_line_dash_ver)
                        }.lparams(width = 1, height = width / 10 - 40) {
                            gravity = Gravity.CENTER
                        }
                    }
                    textView {
                        gravity = Gravity.CENTER
                        background =
                            if (!TextUtils.isEmpty(leftString) && !TextUtils.isEmpty(rightString)) {
                                context.getDrawable(R.drawable.dialog_btn_right_bg_pressed)
                            } else {
                                context.getDrawable(R.drawable.dialog_btn_bottom_bg_pressed)

                            }
                        setOnClickListener {
                            dismiss()
                            resultListener?.invoke(true)
                        }
                        text = rightString
                        textColor = context.getColor(R.color.baseAccent)
                    }.lparams(width = matchParent, height = width / 10, weight = 1f)
                }
            }.apply {
                layoutParams = ViewGroup.LayoutParams(width, wrapContent)
                background = context.getDrawable(R.drawable.dialog_bg)
            }

        }.view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            setWindowAnimations(R.style.AnimPage)
            //消除白边window.setBackgroundDrawable(BitmapDrawable())
            setBackgroundDrawableResource(android.R.color.transparent)
            val windowParams: WindowManager.LayoutParams = attributes
            val dm = context!!.resources.displayMetrics
            //屏幕状态
            when (context!!.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> windowParams.width =
                    (dm.heightPixels * 0.8).toInt()
                Configuration.ORIENTATION_PORTRAIT -> windowParams.width =
                    (dm.widthPixels * 0.8).toInt()
            }

            windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            windowParams.dimAmount = 0.5f //遮罩透明度
            attributes = windowParams
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        run?.invoke(view)
    }

}
