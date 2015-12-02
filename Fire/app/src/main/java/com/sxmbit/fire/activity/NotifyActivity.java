package com.sxmbit.fire.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sxmbit.fire.R;
import com.sxmbit.library.internal.LinearListView;
import com.sxmbit.library.niftynotification.Configuration;
import com.sxmbit.library.niftynotification.Effects;
import com.sxmbit.library.niftynotification.NiftyNotificationView;

import butterknife.Bind;

public class NotifyActivity extends BaseActivity
{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.mLyout)
    RelativeLayout mMLyout;
    @Bind(android.R.id.button1)
    Button mButton1;
    @Bind(android.R.id.button2)
    Button mButton2;
    @Bind(android.R.id.button3)
    Button mButton3;
    @Bind(R.id.button4)
    Button mButton4;
    @Bind(R.id.button5)
    Button mButton5;
    @Bind(R.id.button6)
    Button mButton6;
    @Bind(R.id.button7)
    Button mButton7;
    @Bind(R.id.vertical_list)
    LinearListView mLinearListView;

    @Override
    protected int getContentViewLayoutID()
    {
        return R.layout.activity_notify;
    }

    @Override
    protected void initView()
    {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("蛋碎");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mButton1.setOnClickListener(listener);
        mButton2.setOnClickListener(listener);
        mButton3.setOnClickListener(listener);
        mButton4.setOnClickListener(listener);
        mButton5.setOnClickListener(listener);
        mButton6.setOnClickListener(listener);
        mButton7.setOnClickListener(listener);


//        mLinearListView.setDividerDrawable(new ColorDrawable(Color.CYAN));
//        mLinearListView.setShowDividers(LinearLayout.SHOW_DIVIDER_END | LinearLayout.SHOW_DIVIDER_BEGINNING);
//        mLinearListView.setDividerThickness(getResources().getDimensionPixelSize(R.dimen.common_1dp));
        mLinearListView.setAdapter(mAdapter);

    }

    private Effects effect;

    private View.OnClickListener listener = v -> {
        String msg = "世间最遥远的距离就是没网，点我检查网络设置。〜(ー_ー )ノ";

        switch (v.getId())
        {
            case android.R.id.button1:
                effect = Effects.scale;
                break;
            case android.R.id.button2:
                effect = Effects.thumbSlider;
                break;
            case android.R.id.button3:
                effect = Effects.jelly;
                break;
            case R.id.button4:
                effect = Effects.slideIn;
                break;
            case R.id.button5:
                effect = Effects.flip;
                break;
            case R.id.button6:
                effect = Effects.slideOnTop;
                break;
            case R.id.button7:
                effect = Effects.standard;
                break;
        }


        //        You can configure like this
        //        The default

        Configuration cfg = new Configuration.Builder()
                .setAnimDuration(700)
                .setDispalyDuration(1500)
                .setBackgroundColor("#ffffff")
                .setTextColor("#FF444444")
                .setIconBackgroundColor("#FFFFFFFF")
                .setTextPadding(5)                      //dp
                .setViewHeight(48)                      //dp
                .setTextLines(2)                        //You had better use setViewHeight and setTextLines together
                .setTextGravity(Gravity.CENTER)         //only text def  Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
                .build();
        //
        NiftyNotificationView.build(this, msg, effect, R.id.mLyout, cfg)
                .setIcon(R.mipmap.ic_launcher)               //remove this line ,only text
                .setNiftyOnClickListener(niftyNotificationView -> {
                    //TODO: 2015/11/21  可以进入设置网络界面
                    niftyNotificationView.removeSticky();
                }).showSticky();
    };
    private int mCount = 50;
    private BaseAdapter mAdapter = new BaseAdapter()
    {

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            ((TextView) convertView).setText(String.format(" position== %d", position));
            return convertView;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public int getCount()
        {
            return mCount;
        }
    };
}
