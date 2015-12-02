package com.sxmbit.fire.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.sxmbit.fire.R;

/**
 * Created by LinLin on 2015/11/27.
 */
public class CircleTextView extends TextView
{
    int max;
    PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    Paint mPaint;
    Rect mBound;
    int text_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8f, getResources().getDisplayMetrics());
    int text_color = Color.WHITE;
    int background_color = Color.RED;
    int count = 0;
    String text;

    public CircleTextView(Context context)
    {
        super(context);
        init(null);
    }

    public CircleTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }


    public CircleTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {

        if (null != attrs)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleTextView);
            background_color = a.getColor(R.styleable.CircleTextView_android_background, Color.RED);
            count = a.getInt(R.styleable.CircleTextView_count, 0);
            text_color = a.getColor(R.styleable.CircleTextView_android_textColor, Color.WHITE);
            text_size = a.getDimensionPixelSize(R.styleable.CircleTextView_android_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8f, getResources().getDisplayMetrics()));
            a.recycle();
        }


        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(text_size);
        mBound = new Rect();
        text = String.valueOf(count);
        mPaint.getTextBounds(text, 0, text.length(), mBound);
        setPadding(6, 6, 6, 6);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = 0;
        int height = 0;

        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:// 明确指定了
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                width = getPaddingLeft() + getPaddingRight() + mBound.width();
                break;
        }

        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:// 明确指定了
                height = getPaddingTop() + getPaddingBottom() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                height = getPaddingTop() + getPaddingBottom() + mBound.height();
                break;
        }
        max = Math.max(width, height);
        setMeasuredDimension(max, max);
    }

    private void resetMeasured()
    {
        int width = 0;
        int height = 0;

        width = getPaddingLeft() + getPaddingRight() + mBound.width();
        height = getPaddingTop() + getPaddingBottom() + mBound.height();

        max = Math.max(width, height);
        setMeasuredDimension(max, max);

    }


    @Override
    public void setBackgroundColor(int color)
    {
        background_color = color;
        postInvalidate();
    }

    /**
     * 设置通知个数显示
     *
     * @param count
     */
    public void setNotifiCount(int count)
    {
        this.text = String.valueOf(this.count = count);
        postInvalidate();
    }

    public void puls()
    {
        this.text = String.valueOf(++this.count);
        if (count == 10 || count == 100 || count == 1000)
        {
            mPaint.getTextBounds(text, 0, text.length(), mBound);
            resetMeasured();
        }
        postInvalidate();
    }

    public void minus()
    {
        if (count <= 0)
        {
            return;
        }
        this.text = String.valueOf(--this.count);
        if (count == 9 || count == 99 || count == 999)
        {
            mPaint.getTextBounds(text, 0, text.length(), mBound);
            resetMeasured();
        }
        postInvalidate();
    }

    public int getNotifiCount()
    {
        return this.count;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setAntiAlias(true);
        mPaint.setColor(background_color);
        if (this.count == 0)
        {
            mPaint.setColor(Color.TRANSPARENT);
        }
        canvas.setDrawFilter(pfd);
        canvas.drawCircle(max / 2, max / 2, max / 2, mPaint);
        mPaint.setAntiAlias(false);
        mPaint.setColor(text_color);
        if (this.count == 0)
        {
            mPaint.setColor(Color.TRANSPARENT);
        }
        canvas.drawText(text, (max - mBound.width()) / 2, max / 2 + mBound.height() / 2, mPaint);
    }

}