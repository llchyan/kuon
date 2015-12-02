package com.sxmbit.fire.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.sxmbit.fire.R;

/**
 * Created by linlin on 2015/11/29.CircleButton
 */
public class CircleButton extends Button
{
    private GradientDrawable gradientDrawable;//控件的样式,用于渐变色
    private int backColors = Color.RED;//背景色，String类型
    private int backColorSelecteds = Color.TRANSPARENT;//按下后的背景色，String类型
    private int backGroundImage = 0;//背景图，只提供了Id
    private int backGroundImageSeleted = 0;//按下后的背景图，只提供了Id
    private int textColors = Color.WHITE;//文字颜色，String类型
    private int textColorSeleteds = Color.TRANSPARENT;//按下后的文字颜色，String类型
    private float radius = 8;//圆角半径
    private int shape = 0;//圆角样式，矩形、圆形等，由于矩形的Id为0，默认为矩形
    private Boolean fillet = false;//是否设置圆角

    public CircleButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CircleButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CircleButton(Context context)
    {
        this(context, null);
    }

    private void init(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleButton);
        this.backColors = a.getColor(R.styleable.CircleButton_android_background, Color.RED);
        this.textColors = a.getColor(R.styleable.CircleButton_android_textColor, Color.WHITE);
        this.backColorSelecteds = a.getColor(R.styleable.CircleButton_circlebutton_background_selected, Color.parseColor("#ffff4444"));
        this.textColorSeleteds = a.getColor(R.styleable.CircleButton_circlebutton_textColor_selected, Color.parseColor("#7fffffff"));
        this.radius = a.getDimensionPixelOffset(R.styleable.CircleButton_circlebutton_radius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, getResources().getDisplayMetrics()));
        this.fillet = a.getBoolean(R.styleable.CircleButton_circlebutton_isfillet, false);
        this.shape = a.getInt(R.styleable.CircleButton_circlebutton_gradientDrawable, GradientDrawable.RECTANGLE);

        a.recycle();


        //将Button的默认背景色改为透明，本人不喜欢原来的颜色
        if (fillet)
        {
            if (gradientDrawable == null)
            {
                gradientDrawable = new GradientDrawable();
            }
            gradientDrawable.setColor(backColors);
        } else
        {
            setBackgroundColor(Color.TRANSPARENT);
        }
        //设置文字默认居中
        setGravity(Gravity.CENTER);
        setFillet(fillet);
        //设置Touch事件
        setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View arg0, MotionEvent event)
            {
                //按下改变样式
                setColor(event.getAction());
                //此处设置为false，防止Click事件被屏蔽
                return false;
            }
        });
    }

    //改变样式
    private void setColor(int state)
    {
        if (state == MotionEvent.ACTION_DOWN)
        {
            if (backColorSelecteds != 0)//按下
            {
                if (fillet)//先判断是否设置了按下后的背景色int型
                {
                    if (gradientDrawable == null)
                    {
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setColor(backColorSelecteds);
                } else
                {
                    setBackgroundColor(backColorSelecteds);
                }
            }
            //判断是否设置了按下后文字的颜色
            if (textColorSeleteds != 0)
            {
                setTextColor(textColorSeleteds);
            }
            //判断是否设置了按下后的背景图
            if (backGroundImageSeleted != 0)
            {
                setBackgroundResource(backGroundImageSeleted);
            }
        }
        if (state == MotionEvent.ACTION_UP)
        {
            //抬起
            if (backColors == 0)
            {
                //如果没有设置背景色，默认改为透明
                if (fillet)
                {
                    if (gradientDrawable == null)
                    {
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setColor(Color.TRANSPARENT);
                } else
                {
                    setBackgroundColor(Color.TRANSPARENT);
                }
            }else
            {
                if (fillet)
                {
                    if (gradientDrawable == null)
                    {
                        gradientDrawable = new GradientDrawable();
                    }
                    gradientDrawable.setColor(backColors);
                } else
                {
                    setBackgroundColor(backColors);
                }
            }
            //如果为设置字体颜色，默认为黑色
            if (textColors == 0)
            {
                setTextColor(Color.BLACK);
            } else
            {
                setTextColor(textColors);
            }
            if (backGroundImage != 0)
            {
                setBackgroundResource(backGroundImage);
            }
        }
    }

    /**
     * 设置按钮的背景色,如果未设置则默认为透明
     */
    public void setBackColor(int backColor)
    {
        this.backColors = backColor;
        if (backColor==0)
        {
            if (fillet)
            {
                if (gradientDrawable == null)
                {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(Color.TRANSPARENT);
            } else
            {
                setBackgroundColor(Color.TRANSPARENT);
            }
        } else
        {
            if (fillet)
            {
                if (gradientDrawable == null)
                {
                    gradientDrawable = new GradientDrawable();
                }
                gradientDrawable.setColor(backColor);
            } else
            {
                setBackgroundColor(backColor);
            }
        }
    }





    /**
     * 设置按钮按下后的颜色
     *
     * @param backColorSelected
     */
    public void setBackColorSelected(int backColorSelected)
    {
        this.backColorSelecteds = backColorSelected;
    }

    /**
     * 设置按钮的背景图
     *
     * @param backGroundImage
     */
    public void setBackGroundImage(int backGroundImage)
    {
        this.backGroundImage = backGroundImage;
        if (backGroundImage != 0)
        {
            setBackgroundResource(backGroundImage);
        }
    }

    /**
     * 设置按钮按下的背景图
     *
     * @param backGroundImageSeleted
     */
    public void setBackGroundImageSeleted(int backGroundImageSeleted)
    {
        this.backGroundImageSeleted = backGroundImageSeleted;
    }

    /**
     * 设置按钮圆角半径大小
     *
     * @param radius
     */
    public void setRadius(float radius)
    {
        if (gradientDrawable == null)
        {
            gradientDrawable = new GradientDrawable();
        }
        gradientDrawable.setCornerRadius(radius);
    }

    /**
     * 设置按钮文字颜色
     *
     * @param textColor
     */
    public void setTextColors(int textColor)
    {
        this.textColors = textColor;
        setTextColor(textColor);
    }


    /**
     * 设置按钮按下的文字颜色
     *
     * @param textColor
     */
    public void setTextColorSelected(int textColor)
    {
        this.textColorSeleteds = textColor;
    }



    /**
     * 按钮的形状
     *
     * @param shape
     */
    public void setShape(int shape)
    {
        this.shape = shape;
    }

    /**
     * 设置其是否为圆角
     *
     * @param fillet
     */
    @SuppressWarnings("deprecation")
    public void setFillet(Boolean fillet)
    {
        this.fillet = fillet;
        if (fillet)
        {
            if (gradientDrawable == null)
            {
                gradientDrawable = new GradientDrawable();
            }
            //GradientDrawable.RECTANGLE
            gradientDrawable.setShape(shape);
            gradientDrawable.setCornerRadius(radius);
            setBackgroundDrawable(gradientDrawable);
        }
    }
}
