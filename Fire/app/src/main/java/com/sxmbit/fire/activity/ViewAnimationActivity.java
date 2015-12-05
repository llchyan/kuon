package com.sxmbit.fire.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;

import com.sxmbit.fire.R;

import butterknife.Bind;

public class ViewAnimationActivity extends BaseActivity
{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.view_animation_text1)
    AppCompatTextView mViewAnimationText1;
    @Bind(R.id.view_animation_checkbox1)
    AppCompatCheckBox mViewAnimationCheckbox1;
    @Bind(R.id.view_animation_checkbox2)
    AppCompatCheckBox mViewAnimationCheckbox2;
    @Bind(R.id.view_animation_text2)
    AppCompatTextView mViewAnimationText2;
    @Bind(R.id.view_animation_checkbox3)
    AppCompatCheckBox mViewAnimationCheckbox3;
    @Bind(R.id.view_animation_text3)
    AppCompatTextView mViewAnimationText3;
    @Bind(R.id.view_animation_checkbox4)
    AppCompatCheckBox mViewAnimationCheckbox4;
    @Bind(R.id.view_animation_text4)
    AppCompatTextView mViewAnimationText4;
    @Bind(R.id.view_animation_text5)
    AppCompatTextView mViewAnimationText5;
    @Bind(R.id.view_animation_text6)
    AppCompatTextView mViewAnimationText6;
    @Bind(R.id.view_animation_text7)
    AppCompatTextView mViewAnimationText7;
    @Bind(R.id.view_animation_text8)
    AppCompatTextView mViewAnimationText8;
    int height6;


    @Override
    protected int getContentViewLayoutID()
    {
        return R.layout.activity_view_animation;
    }

    @Override
    protected void initView()
    {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null == actionBar)
        {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("ViewAnimation Demo");
        mViewAnimationCheckbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mViewAnimationText1.startAnimation(AnimationUtils.loadAnimation(mContext, isChecked ? R.anim.alpha_out : R.anim.alpha_in));
            }
        });
        mViewAnimationCheckbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mViewAnimationText2.startAnimation(AnimationUtils.loadAnimation(mContext, isChecked ? R.anim.scale_min : R.anim.scale_max));
            }
        });
        mViewAnimationCheckbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mViewAnimationText3.startAnimation(AnimationUtils.loadAnimation(mContext, isChecked ? R.anim.translate_left2right : R.anim.translate_top2bottom));
            }
        });
        mViewAnimationCheckbox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mViewAnimationText4.startAnimation(AnimationUtils.loadAnimation(mContext, isChecked ? R.anim.rotate_shun : R.anim.rotate_ni));
            }
        });
        mViewAnimationText5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mViewAnimationText5.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.set_demo));
            }
        });


        final int maxW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, getResources().getDisplayMetrics());
        valueAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.animator_1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                int currentValue = (int) animation.getAnimatedValue();
                mViewAnimationText6.getLayoutParams().width = maxW * currentValue / 100;
                mViewAnimationText6.requestLayout();
            }
        });
        mViewAnimationText6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                valueAnimator.start();
            }
        });

        objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.animator_2);
        objectAnimator.setTarget(new ViewWrapper(mViewAnimationText7, maxW));
        mViewAnimationText7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                objectAnimator.start();
            }
        });
        animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.animator_3);

        animatorSet.setTarget(new ViewWrapper(mViewAnimationText8, maxW));
        mViewAnimationText8.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                animatorSet.start();
            }
        });

    }

    ValueAnimator valueAnimator;
    ObjectAnimator objectAnimator;
    AnimatorSet animatorSet;


    private static class ViewWrapper
    {
        private View target;
        private int maxWidth;

        public ViewWrapper(View target, int maxWidth)
        {
            this.target = target;
            this.maxWidth = maxWidth;
        }

        public int getWidth()
        {
            return target.getLayoutParams().width;
        }

        public void setWidth(int widthValue)
        {
            target.getLayoutParams().width = maxWidth * widthValue / 100;
            target.requestLayout();
        }

        public void setMarginTop(int margin)
        {
            LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) target.getLayoutParams();
            layoutParams.setMargins(0, margin, 0, 0);
            target.setLayoutParams(layoutParams);
        }
    }
}
