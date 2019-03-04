package com.qxj.conclusion.view

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import com.qxj.conclusion.util.LogTool
import com.qxj.conclusion.R

class CustomEditText : EditText {

    private var TAG = CustomEditText::class.java.name

    var imgInable: Drawable? = null
    var imgAble: Drawable? = null
    var imgLeft: Drawable? = null
    var mType: Int = 0
    var select: Boolean = false


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.editTextStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setAfterChangedDrawable()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        Log.d(TAG, "获取焦点")
    }

    fun setType(type: Int) {
        this.mType = type
        setDrawable()
    }

    //type 2 密码 可见或不可见   1 删除图标
    private fun setDrawable() {
        when (mType) {
            1 -> {
                imgInable = resources.getDrawable(R.drawable.delete_gray)
                imgAble = resources.getDrawable(R.drawable.delete)
                imgLeft = resources.getDrawable(R.drawable.user_people)
                setCompoundDrawablesWithIntrinsicBounds(imgLeft, null, imgInable, null)//
            }
            2 -> {
                transformationMethod = PasswordTransformationMethod.getInstance()

                imgInable = resources.getDrawable(R.drawable.delete_gray)
                imgAble = resources.getDrawable(R.drawable.delete)
                imgLeft = resources.getDrawable(R.drawable.user_people)
                setCompoundDrawablesWithIntrinsicBounds(imgLeft, null, imgInable, null)//密码不可见
            }
//            3 -> setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null)//地址
        }
    }

    private fun setAfterChangedDrawable() {
        when (mType) {
            1 -> setCompoundDrawablesWithIntrinsicBounds(imgLeft, null, imgAble, null)//

        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mType != 0 && event.action == MotionEvent.ACTION_UP) {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            val rect: Rect = Rect()
            getGlobalVisibleRect(rect)
            //设置点击区域
            rect.left = rect.right - 100
            LogTool.d(TAG, rect.toString())
            if (rect.contains(x, y)) {
                Log.d(TAG, "点中图标")
                when (mType) {
                    1 -> {
                        setText("")
                        setCompoundDrawablesWithIntrinsicBounds(imgLeft, null, imgInable, null)
                    }
                    2 -> {
                        if (select){
                            select = false
                            transformationMethod = PasswordTransformationMethod.getInstance()
                            setCompoundDrawablesWithIntrinsicBounds(imgLeft, null, imgInable, null)
                        } else {
                            select = true
                            transformationMethod = HideReturnsTransformationMethod.getInstance()
                            setCompoundDrawablesWithIntrinsicBounds(imgLeft, null, imgAble, null)
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

}