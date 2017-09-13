package com.lucio.mvpapp;

import android.app.Application;
import android.content.Context;


import com.lucio.mvpapp.util.ToastUtil;
import com.lucio.mvpapp.util.net.RetrofitUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MvpApp extends Application {

    private static Context mContext;
    private static OkHttpClient mClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        ToastUtil.initAppContext(mContext);

        mClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
//                .readTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(9, TimeUnit.SECONDS)
                .build();

        RetrofitUtil.setBaseUrl(AppConstant.getHost());
    }

    public static Context getContext() {
        return mContext;
    }

    public static OkHttpClient getClient() {
        if (mClient == null) {
            mClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
//                    .readTimeout(0, TimeUnit.SECONDS)
                    .connectTimeout(9, TimeUnit.SECONDS)
                    .build();
        }
        return mClient;
    }
}
