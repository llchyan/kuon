package com.sxmbit.fire.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.sxmbit.fire.R;

import butterknife.Bind;

public class ShapeActivity extends BaseActivity
{
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getContentViewLayoutID()
    {
        return R.layout.activity_shape;
    }

    @Override
    protected void initView()
    {
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (null==actionBar)
            {
                return;
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setTitle("扫一扫");
    }

}
