package com.sxmbit.fire;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;
import com.zhy.http.okhttp.OkHttpClientManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by LinLin on 2015/11/19
 */
public class BaseApplication extends Application
{

    private static BaseApplication application;


    public static BaseApplication getInstance()
    {
        return application;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        application = this;
        OkHttpClient httpClient = OkHttpClientManager.getInstance().getOkHttpClient();
        httpClient.setConnectTimeout(100, TimeUnit.SECONDS);
        httpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        httpClient.setReadTimeout(10, TimeUnit.SECONDS);

    }


}
