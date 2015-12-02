package com.zhy.http.okhttp.request;

import android.text.TextUtils;
import android.util.Pair;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.zhy.http.okhttp.OkHttpClientManager;
import com.zhy.http.okhttp.callback.ResultCallback;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by LinLin on 2015/11/20
 */
public class OkHttpOrderlyRequest
{
    protected OkHttpClientManager mOkHttpClientManager = OkHttpClientManager.getInstance();
    protected OkHttpClient mOkHttpClient;

    protected RequestBody requestBody;
    protected Request request;

    protected String url;
    protected Object tag;
    protected ArrayList<Pair<String, String>> pairs;
    protected Map<String, String> headers;

    protected OkHttpOrderlyRequest(String url, Object tag, ArrayList<Pair<String, String>> pairs, Map<String, String> headers)
    {
        mOkHttpClient = mOkHttpClientManager.getOkHttpClient();
        this.url = url;
        this.tag = tag;
        this.headers = headers;
        this.pairs = pairs;
    }

    protected Request buildRequest()
    {
        if (TextUtils.isEmpty(url))
        {
            throw new IllegalArgumentException("url can not be empty!");
        }
        Request.Builder builder = new Request.Builder();
        appendHeaders(builder, headers);
        builder.url(url).tag(tag).post(requestBody);
        return builder.build();
    }

    protected RequestBody buildRequestBody()
    {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (pairs != null && !pairs.isEmpty())
        {
            for (Pair<String, String> pair : pairs)
            {
                builder.add(pair.first, pair.second);
            }
        }
        return builder.build();
    }

    protected void prepareInvoked(ResultCallback callback)
    {
        requestBody = buildRequestBody();
        requestBody = wrapRequestBody(requestBody, callback);
        request = buildRequest();
    }


    public void invokeAsyn(ResultCallback callback)
    {
        prepareInvoked(callback);
        mOkHttpClientManager.execute(request, callback);
    }



    protected void appendHeaders(Request.Builder builder, Map<String, String> headers)
    {
        if (builder == null)
        {
            throw new IllegalArgumentException("builder can not be empty!");
        }

        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty())
            return;

        for (String key : headers.keySet())
        {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public void cancel()
    {
        if (tag != null)
            mOkHttpClientManager.cancelTag(tag);
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallback callback)
    {
        return new CountingRequestBody(requestBody, new CountingRequestBody.Listener()
        {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength)
            {

                mOkHttpClientManager.getDelivery().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callback.inProgress(bytesWritten * 1.0f / contentLength);
                    }
                });

            }
        });
    }



    public static class Builder
    {
        private String url;
        private Object tag;
        private Map<String, String> headers;
        private ArrayList<Pair<String, String>> pairs;

        public Builder url(String url)
        {
            this.url = url;
            return this;
        }

        public Builder tag(Object tag)
        {
            this.tag = tag;
            return this;
        }

        public Builder addHeader(String key, String val)
        {
            if (this.headers == null)
            {
                headers = new IdentityHashMap<>();
            }
            headers.put(key, val);
            return this;
        }

        public Builder pairs(ArrayList<Pair<String, String>> pairs)
        {
            this.pairs = pairs;
            return this;
        }

        public OkHttpOrderlyRequest post(ResultCallback callback)
        {
            OkHttpOrderlyRequest request = new OkHttpOrderlyRequest(url, tag, pairs, headers);
            request.invokeAsyn(callback);
            return request;
        }
    }
}
