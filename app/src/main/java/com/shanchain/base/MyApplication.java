package com.shanchain.base;

import android.app.Application;
import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 周建 on 2017/5/13.
 * 程序入口,全局单利
 */

public class MyApplication extends Application {

    public static Context getContext() {
        return mContext;
    }

    public static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        initOkhttpUtils();
    }

    /**
     *  2017/5/16
     *  描述：初始化网络请求框架OkhttpUtils
     *
     */
    private void initOkhttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
