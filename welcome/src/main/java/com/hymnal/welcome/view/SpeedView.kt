package com.hymnal.welcome.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import kotlin.math.cos

class SpeedView : View {

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null)

    //画笔
    private lateinit var mPaintArc: Paint
    //文字画笔
    private lateinit var mPaintText: Paint
    private lateinit var mPaintText2: Paint

    //view 宽
    private var mWidth: Float = 0f

    private var mHeight: Float = 0f


    private fun dip2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    fun init() {
        mPaintArc = Paint().apply {
            strokeWidth = dip2px(context, 10f)
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }

        mPaintText = Paint().apply {
            strokeWidth = dip2px(context, 6f)
            textSize = dip2px(context, 36f)
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        mPaintText2 = Paint().apply {
            strokeWidth = dip2px(context, 6f)
            textSize = dip2px(context, 20f)
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        mWidth = when (widthSpecMode) {
            MeasureSpec.EXACTLY -> widthSpecSize.toFloat()
            else -> dip2px(context, 120f)
        }

        mHeight = when (heightSpecMode) {
            MeasureSpec.EXACTLY -> heightSpecSize.toFloat()
            else -> dip2px(context, 120f)
        }

        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
    }

    private var speed = 0
    private var process = 0f
    private var isLimit = false

    private var isLoading = true

    @SuppressLint("NewApi")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.scale(0.8f, 0.8f)
        canvas.translate((-mWidth / 2 + mWidth * cos(30.0)).toFloat(), 0f)
        if (isLimit) {
            mPaintArc.color = Color.parseColor("#cce34545")
        } else {
            mPaintArc.color = Color.parseColor("#cc8fbaff")
            mPaintArc.pathEffect = null
        }
        mPaintText.color = Color.parseColor("#ccffffff")
        mPaintText2.color = Color.parseColor("#ccffffff")
        if (process != 240f) {
            process += 5
        } else {
            isLoading = false
        }

        val paintW = mPaintArc.strokeWidth
        val x = mWidth / 2
        canvas.drawArc(
            0f + paintW,
            0f + paintW,
            mWidth + paintW,
            mHeight + paintW,
            -120f,
            process,
            false,
            mPaintArc
        )
        canvas.drawText(
            speed.toString(),
            x + mWidth * cos(30.0).toFloat() / 2 - (speed.toString().length - 1) * mPaintText.textSize / 4,
            mHeight / 2 + mPaintText.textSize / 2,
            mPaintText
        )
        canvas.drawText("km/h", x, mHeight / 2 + mPaintText.textSize + paintW / 2, mPaintText2)


        if (isLoading) {
            invalidate()
            canvas.restore()
        }
    }

    fun setSpeed(speed: Int) {

        this.speed = speed
        invalidate()
    }

    fun setLimitSpeed(limitSpeed: Int, isLimit: Boolean) {
        if (!isLoading && (isLimit != this.isLimit)) {
            isLoading = true
            process = 0f
            invalidate()
        }
        this.isLimit = isLimit
    }
}