package com.sxmbit.fire.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sxmbit.fire.R;

/**
 * Created by LinLin on 2015/11/12
 */
public class CustomToolbar extends Toolbar
{

    private int textColor = 0xFFB5873C;
    private int iconColor = 0xFFB5873C;
    private boolean changeIcon = false;

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomToolbar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    public CustomToolbar(Context context)
    {
        super(context);
    }


    private void init(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomToolbar);
        textColor = a.getColor(R.styleable.CustomToolbar_textColor, textColor);
        iconColor = a.getColor(R.styleable.CustomToolbar_iconColor, iconColor);
        changeIcon = a.getBoolean(R.styleable.CustomToolbar_changeIcon, false);
        a.recycle();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        colorizeToolbar(this, textColor, iconColor);
    }

    public void setItemColor(boolean isChangeIcon, int toolbarTextsColor, int toolbarIconsColor)
    {
        changeIcon = isChangeIcon;
        colorizeToolbar(this, toolbarTextsColor, toolbarIconsColor);
    }


    /**
     * Use this method to colorize toolbar icons to the desired target color
     *
     * @param toolbarView       toolbar view being colored
     * @param toolbarIconsColor the target color of toolbar icons
     */
    public void colorizeToolbar(Toolbar toolbarView, int toolbarTextsColor, int toolbarIconsColor)
    {
        final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.SRC_IN);

        for (int i = 0, count = toolbarView.getChildCount(); i < count; i++)
        {
//            final View v = toolbarView.getChildAt(i);
//                    Log.i("hhde  " + toolbarView.getChildAt(i), toolbarView.getChildAt(i).getClass().getSimpleName());

            doColorizing(toolbarView.getChildAt(i), colorFilter, toolbarTextsColor);

        }

        //Step 3: Changing the color of title and subtitle.
        //        toolbarView.setTitleTextColor(toolbarIconsColor);
        //        toolbarView.setSubtitleTextColor(toolbarIconsColor);
    }

    public void doColorizing(View v, final ColorFilter colorFilter, int toolbarTextsColor)
    {
//        Log.i("hhde  " + v, v.getClass().getSimpleName());

//        if (v instanceof TextView)
//        {
//            Log.i("hh_text",((TextView)v).getText().toString());//原来是标题
//        }

        if (v instanceof ActionMenuView)
        {
            //            v.setBackgroundColor(Color.BLUE);
            for (int j = 0; j < ((ActionMenuView) v).getChildCount(); j++)
            {
                Log.i("hhaa  " +j, ((ActionMenuView) v).getChildAt(j).getClass().getSimpleName());

                //Step 2: Changing the color of any ActionMenuViews - icons that
                //are not back button, nor text, nor overflow menu icon.
                final View innerView = ((ActionMenuView) v).getChildAt(j);


                if (innerView instanceof TextView)
                {
                    ((TextView) innerView).setTextColor(toolbarTextsColor);//如果只显示文字的就是这个了
//                    Log.i("hh_text", ((TextView) innerView).getText().toString());//原来是标题

                    //                    innerView.setBackgroundColor(Color.GREEN);
                }

                if (changeIcon && innerView instanceof ActionMenuItemView)
                {
                    Log.i("hh_text "+j, ((ActionMenuItemView) innerView).getText().toString());//原来是标题

                    //                    innerView.setBackgroundColor(Color.RED);
                    int drawablesCount = ((ActionMenuItemView) innerView).getCompoundDrawables().length;
                    for (int k = 0; k < drawablesCount; k++)
                    {
                        if (((ActionMenuItemView) innerView).getCompoundDrawables()[k] != null)
                        {
                            final int finalK = k;
                            /**Important to set the color filter in seperate thread,by adding it to the message queue。
                             * Won't work otherwise.
                             * 重要的是在分离线程里设置滤色器,通过添加它到消息队列,否则行不通。
                             * */
                            ((ActionMenuItemView) innerView).getCompoundDrawables()[finalK].setColorFilter(colorFilter);
                        }
                    }
                }

            }
        }

    }


}
