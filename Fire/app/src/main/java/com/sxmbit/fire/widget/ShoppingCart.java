package com.sxmbit.fire.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LinLin on 2015/11/27.
 */
public class ShoppingCart extends ViewGroup
{

    private static final int SQRETWO = (int) (Math.sqrt(2) / 2);

    public ShoppingCart(Context context)
    {
        super(context);
    }

    public ShoppingCart(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ShoppingCart(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    //    private void init()
    //    {
    //        inflate(getContext(), R.layout.shopping_cart, this);
    //    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
//        /**
//         * 记录如果是wrap_content是设置的宽和高
//         */
//        int width = 0;
//        int height = 0;
//
//
//        int cWidth = 0;
//        int cHeight = 0;
//        MarginLayoutParams cParams = null;
//
//        //        // 用于计算左边两个childView的高度
//        //        int lHeight = 0;
//        //        // 用于计算右边两个childView的高度，最终高度取二者之间大值
//        //        int rHeight = 0;
//        //
//        //        // 用于计算上边两个childView的宽度
//        //        int tWidth = 0;
//        //        // 用于计算下面两个childiew的宽度，最终宽度取二者之间大值
//        //        int bWidth = 0;
//
//        /**
//         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
//         */
//        int oneRadir = 0, twoRadir = 0, threeWidth = 0;
//        int threeHeight = 0;
//        MarginLayoutParams oneParams = null, twoParams = null, threeParams = null;
//        View childOne = getChildAt(0);
//        //        oneWidth = childOne.getMeasuredWidth();
//        //        oneHeight = childOne.getMeasuredHeight();
//        oneRadir = Math.max(childOne.getMeasuredWidth(), childOne.getMeasuredHeight()) / 2;
//        oneParams = (MarginLayoutParams) childOne.getLayoutParams();
//        View childTwo = getChildAt(1);
//        //        twoWidth = childTwo.getMeasuredWidth();
//        //        twoWHeight = childTwo.getMeasuredHeight();
//        twoRadir = Math.max(childTwo.getMeasuredWidth(), childTwo.getMeasuredHeight()) / 2;
//        twoParams = (MarginLayoutParams) childTwo.getLayoutParams();
//        View childThree = getChildAt(2);
//        threeHeight = childThree.getMeasuredHeight();
//        threeParams = (MarginLayoutParams) childThree.getLayoutParams();
//
//        if (twoRadir + twoParams.rightMargin > (1 - SQRETWO) * oneRadir + oneParams.rightMargin)
//        {
//            width = (1 + SQRETWO) * oneRadir + oneParams.leftMargin + twoRadir + twoParams.rightMargin;
//        } else
//        {
//            width = oneRadir * 2 + oneParams.leftMargin + oneParams.rightMargin;
//        }
//
//        if (twoRadir + twoParams.topMargin > (1 - SQRETWO) * oneRadir + oneParams.topMargin)
//        {
//            height = twoRadir + twoParams.topMargin + (1 + SQRETWO) * oneRadir + oneParams.bottomMargin + threeHeight + threeParams.topMargin + threeParams.bottomMargin;
//        } else
//        {
//            height = oneRadir * 2 + oneParams.topMargin + oneParams.bottomMargin + threeHeight + threeParams.topMargin + threeParams.bottomMargin;
//        }


        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
//        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
//                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
//                : height);
        setMeasuredDimension(sizeWidth,sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int oneRadir = 0, twoRadir = 0, threeWidth = 0;
        int threeHeight = 0;
        MarginLayoutParams oneParams = null, twoParams = null, threeParams = null;
        View childOne = getChildAt(0);
        //        oneWidth = childOne.getMeasuredWidth();
        //        oneHeight = childOne.getMeasuredHeight();
        oneRadir = Math.max(childOne.getMeasuredWidth(), childOne.getMeasuredHeight()) / 2;
        oneParams = (MarginLayoutParams) childOne.getLayoutParams();
        View childTwo = getChildAt(1);
        //        twoWidth = childTwo.getMeasuredWidth();
        //        twoWHeight = childTwo.getMeasuredHeight();
        twoRadir = Math.max(childTwo.getMeasuredWidth(), childTwo.getMeasuredHeight()) / 2;
        twoParams = (MarginLayoutParams) childTwo.getLayoutParams();
        View childThree = getChildAt(2);
        threeHeight = childThree.getMeasuredHeight();
        threeParams = (MarginLayoutParams) childThree.getLayoutParams();

        int oneL = 0, twoL = 0, threeL = 0;
        int oneT = 0, twoT = 0, threeT = 0;
        int oneR = 0, twoR = 0, threeR = 0;
        int oneB = 0, twoB = 0, threeB = 0;

        oneL = oneParams.leftMargin;
        twoL = (1 + SQRETWO) * oneRadir + oneParams.leftMargin - twoRadir;
        threeL = oneRadir + oneParams.leftMargin - threeWidth / 2;
        oneR = oneL + childOne.getWidth();
        twoR = twoL + childTwo.getWidth();
        threeR = threeL + childThree.getWidth();
        if (twoRadir + twoParams.topMargin > (1 - SQRETWO) * oneRadir + oneParams.topMargin)
        {
            oneT = twoRadir + twoParams.topMargin - (1 - SQRETWO) * oneRadir;
            twoT = twoParams.topMargin;
        } else
        {
            oneT = oneParams.topMargin;
            twoT = oneRadir - SQRETWO * oneRadir - twoRadir + oneParams.topMargin;
        }
        threeT = oneT + oneRadir * 2 + oneParams.bottomMargin + threeParams.topMargin;
        oneB = oneB + childOne.getHeight();
        twoB = twoB + childTwo.getHeight();
        threeB = threeB + childThree.getHeight();
        childOne.layout(oneL, oneT,oneR,oneB );
        childTwo.layout(twoL, twoR,twoR,twoB );
        childThree.layout(threeT, threeT,threeR,threeB );
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams()
    {
//        Log.e(TAG, "generateDefaultLayoutParams");
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p)
    {
//        Log.e(TAG, "generateLayoutParams p");
        return new MarginLayoutParams(p);
    }
}
