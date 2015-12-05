package com.sxmbit.fire.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.okhttp.Request;
import com.sxmbit.fire.R;
import com.sxmbit.library.KLog;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpOrderlyRequest;

import java.util.ArrayList;

import butterknife.Bind;

public class MainActivity extends BaseActivity
{


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.id_nv_menu)
    NavigationView mNavigationView;

    @Override
    protected int getContentViewLayoutID()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void initView()
    {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null == actionBar)
            return;
        actionBar.setTitle("火火火");
        actionBar.setDisplayHomeAsUpEnabled(true);
//        Intent intent = new Intent(mContext, SelectorActivity.class);
        Intent intent = new Intent(mContext, ViewAnimationActivity.class);
        //            Intent intent = new Intent(getContext(), RichScanActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_settings)
        {
            //            showSnackbar("action_settings");
            //            showToast("action_settings");
            ArrayList<Pair<String, String>> pairs = new ArrayList<>();
            pairs.add(new Pair<>("title", "hehe"));
            pairs.add(new Pair<>("content[][text]", "first"));
            pairs.add(new Pair<>("content[][img]", "thread/2015111710584844877_600x800_0.75.jpg"));
            pairs.add(new Pair<>("content[][text]", "first"));
            pairs.add(new Pair<>("content[][img]", "thread/2015111710584844877_600x800_0.75.jpg"));
            pairs.add(new Pair<>("content[][text]", "first"));
            pairs.add(new Pair<>("content[][img]", "thread/2015111710584844877_600x800_0.75.jpg"));
            pairs.add(new Pair<>("content[][img]", "thread/2015111710584844877_600x800_0.75.jpg"));
            pairs.add(new Pair<>("content[][text]", "first"));
            pairs.add(new Pair<>("content[][text]", "first"));

            new OkHttpOrderlyRequest.Builder()
                    .tag(this)
                    .url("http://2.0.hlstreet.com/mobile/member_thread/addThread")
                    .addHeader("TOKEN", "cf20007684f48f06e9d38904b40e2397")
                    .pairs(pairs)
                    .post(new ResultCallback<String>()
                    {
                        @Override
                        public void onError(Request request, Exception e)
                        {
                            KLog.e(e.getMessage());
                        }

                        @Override
                        public void onResponse(String response)
                        {
                            KLog.i(response);
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }

}
