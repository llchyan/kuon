package com.sxmbit.fire.activity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.sxmbit.fire.R;

import butterknife.Bind;

public class SelectorActivity extends BaseActivity
{
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.selector_state_selected)
    Button mStateSelected;
    @Bind(R.id.selector_clip)
    ImageView mSelectorClip;
    @Bind(R.id.selector_scale)
    ImageView mSelectorScale;
    @Bind(R.id.selector_level)
    ImageView mSelectorLevel;
    @Bind(R.id.selector_transition)
    ImageView mSelectorTransition;

    boolean isTransition = true;
    @Bind(R.id.selector_animation_list)
    ImageView mSelectorAnimationList;
    @Bind(R.id.selector_scalpe)
    ScalpelFrameLayout mSelectorScalpe;

    @Override
    protected int getContentViewLayoutID()
    {
        return R.layout.activity_selector;
    }

    @Override
    protected void initView()
    {
        mSelectorScalpe.setLayerInteractionEnabled(true);//开启3D效果
        mSelectorScalpe.setDrawViews(true);//显隐DrawViews
        mSelectorScalpe.setDrawIds(true);//显隐 view ID
//        mSelectorScalpe.setChromeColor();//修改边框的颜色
//        mSelectorScalpe.setChromeShadowColor();//修改边框的阴影
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null == actionBar)
        {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        mStateSelected.setSelected(true);
        mSelectorClip.getDrawable().setLevel(5000);//level范围值0~10000,值越小裁剪的越多
        mSelectorScale.getDrawable().setLevel(5000);
        mSelectorLevel.getDrawable().setLevel(30);//level范围值0~100

        mSelectorLevel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isTransition)
                {
                    ((TransitionDrawable) mSelectorTransition.getDrawable()).startTransition(500); //正向切换，即从第一个drawable切换到第二个
                } else
                {
                    ((TransitionDrawable) mSelectorTransition.getDrawable()).reverseTransition(500); //逆向切换，即从第二个drawable切换回第一个
                }
                isTransition = !isTransition;
            }
        });
        mSelectorTransition.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((AnimationDrawable) mSelectorAnimationList.getDrawable()).stop();
                ((AnimationDrawable) mSelectorAnimationList.getDrawable()).start();//尼玛，android:oneshot="true"播放完之后，要stop()才能start()
            }
        });

    }



}
