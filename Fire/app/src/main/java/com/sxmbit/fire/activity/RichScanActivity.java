package com.sxmbit.fire.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sxmbit.fire.R;

import butterknife.Bind;

public class RichScanActivity extends BaseActivity
{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rich_scan_commont)
    EditText mRichScanCommont;
    @Bind(R.id.rich_scan_send)
    Button mRichScanSend;
    @Bind(R.id.rich_scan_parent)
    LinearLayout mRichScanParent;

    @Override
    protected int getContentViewLayoutID()
    {
        return R.layout.activity_rich_scan;
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
        actionBar.setTitle("扫一扫");
        mRichScanCommont.setOnTouchListener((v, event) -> {
            getETFocusable(mRichScanCommont, true);//点击本身，无法触发OnCliklistener事件
            return false;
        });
//        Uri parse = Uri.parse("content://contacts/people");
        //        Intent intent = new Intent(Intent.ACTION_PICK, parse);
        //        startActivityForResult(intent,2);
//        new OkHttpRequest.Builder();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case Activity.RESULT_OK:

                break;
            case Activity.RESULT_CANCELED:
                break;
            case Activity.RESULT_FIRST_USER:
                break;
        }

    }

    private void getETFocusable(View view, boolean isAlive)
    {
//        if (view != lastView)
//        {
//            lastView = view;
//            if (isAlive)
//            {
//                showSoftKeyboard();
//            } else
//            {
//                hideSoftKeyboard();
//            }
//        }

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

}
