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

class LimitSpeedView : View {

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

        if (speed == 0) visibility = INVISIBLE
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
    private var isLoading = true

    @SuppressLint("NewApi")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.scale(0.8f, 0.8f)
        canvas.translate((mWidth / 2 - mWidth * cos(30.0) + mPaintArc.strokeWidth).toFloat(), 0f)

        mPaintArc.color = Color.parseColor("#cce34545")
        mPaintArc.pathEffect = null

        mPaintText.color = Color.parseColor("#ccffffff")
        mPaintText2.color = Color.parseColor("#ccffffff")
        if (process != -240f) {
            process -= 5
        } else {
            isLoading = false
        }

        val paintW = mPaintArc.strokeWidth
        val x = (mWidth / 2 - mWidth * cos(30.0) / 2 - paintW).toFloat()
        canvas.drawArc(
            0f + paintW,
            0f + paintW,
            mWidth + paintW,
            mHeight + paintW,
            -60f,
            process,
            false,
            mPaintArc
        )
        canvas.drawText(speed.toString(), x, mHeight / 2 + mPaintText.textSize / 2, mPaintText)
        canvas.drawText("限速", x, mHeight / 2 + mPaintText.textSize + paintW / 2, mPaintText2)


        if (isLoading) {
            invalidate()
            canvas.restore()
        }
    }

    fun setSpeed(speed: Int) {
        if (speed > 0 && visibility == INVISIBLE) {
            isLoading = true
            visibility = VISIBLE
            process = 0f
        } else if (speed == 0 && visibility == VISIBLE) {
            visibility = INVISIBLE
        }

        this.speed = speed
        invalidate()
    }
}