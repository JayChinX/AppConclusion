package com.hymnal.welcome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class PannelDynamicView extends View {

    private Context mContext;

    // 画笔对象
    private Paint mPaintArc;

    // View宽
    private float mWidth;

    // View高
    private float mHeight;

    // 圆圈是否为填充模式
    private Paint mPaintText;
    private Paint mPaintText2;

    public PannelDynamicView(Context context) {
        this(context, null);
    }

    public PannelDynamicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PannelDynamicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取用户配置属性
        init();
    }

    private void init() {
        mContext = getContext();

        //圆弧画笔
        mPaintArc = new Paint();
        mPaintArc.setStrokeWidth(dip2px(mContext, 6));
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setStrokeCap(Paint.Cap.ROUND);
        mPaintArc.setAntiAlias(true);

        //文字画笔
        mPaintText = new Paint();
        mPaintText.setStrokeWidth(dip2px(mContext, 6));
        mPaintText.setTextSize(dip2px(mContext, 36));
        mPaintText.setTypeface(Typeface.DEFAULT_BOLD);
        //文字
        mPaintText2 = new Paint();
        mPaintText2.setStrokeWidth(dip2px(mContext, 6));
        mPaintText2.setTextSize(dip2px(mContext, 30));
        mPaintText2.setTypeface(Typeface.DEFAULT_BOLD);

        // 设置View的圆为半透明
        setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int myWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int myHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        // 获取宽度
        if (myWidthSpecMode == MeasureSpec.EXACTLY) {
            // match_parent
            mWidth = myWidthSpecSize;
        } else {
            // wrap_content
            mWidth = dip2px(mContext, 120);
        }

        // 获取高度
        if (myHeightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = myHeightSpecSize;
        } else {
            // wrap_content
            mHeight = dip2px(mContext, 120);
        }

        // 设置该view的宽高
        setMeasuredDimension((int) mWidth, (int) mHeight);
    }


    private float arcStart = -90;
    private float arcProcessing = 0;
    private float arcEnd = 240;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale((float) 0.8, (float) 0.8);
        canvas.translate(-mWidth / 2, 8);

        if (mCarSpeed >= 81) {
            mPaintText.setColor(Color.RED);
            mPaintText2.setColor(Color.RED);
            mPaintArc.setColor(Color.RED);
        } else {
            mPaintText.setColor(Color.BLUE);
            mPaintText2.setColor(Color.BLUE);
            mPaintArc.setColor(Color.BLUE);
            mPaintArc.setPathEffect(null);
        }


        if (arcProcessing != arcEnd) {
            arcProcessing += 5;
        } else {
            isLoading = false;
        }
//        int y = (int) (mWidth * Math.cos(-120));
        //圆弧
        canvas.drawArc(0, 0, mWidth, mHeight, -120, arcProcessing, false, mPaintArc);
        canvas.drawText(String.valueOf(mCarSpeed), mWidth / 2, mHeight / 4 * 2, mPaintText);
        canvas.drawText("km/h", mWidth / 2, mHeight / 4 * 3, mPaintText2);
        if (isLoading) {
            invalidate();
            canvas.restore();
        } else {

        }
    }

    private boolean isLoading = true;

    public void loading() {
        isLoading = true;
        arcProcessing = 0;
        invalidate();
    }

    private int mCarSpeed = 0;

    public void setSpeed(int kmH) {
        mCarSpeed = kmH;
        invalidate();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}