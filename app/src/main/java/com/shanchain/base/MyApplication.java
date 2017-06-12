package com.shanchain.base;

import android.app.Application;
import android.content.Context;

import com.alipay.euler.andfix.patch.PatchManager;
import com.shanchain.utils.VersionUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 周建 on 2017/5/13.
 * 程序入口,全局单利
 */

public class MyApplication extends Application {

    private PatchManager patchManager;

    public static Context getContext() {
        return mContext;
    }

    public static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        initOkhttpUtils();

        initAndFix();

    }

    private void initAndFix() {
        patchManager = new PatchManager(mContext);
        patchManager.init(VersionUtils.getVersionName(mContext));//current version
        // 加载已经添加到PatchManager中的patch
        patchManager.loadPatch();
    /*
    2.如果有新的补丁需要修复，下载完成后，进行以下操作

        //添加patch，只需指定patch的路径即可，补丁会立即生效
        mPatchManager.addPatch(path);

    3.当apk版本升级，需要把之前patch文件的删除，需要以下操作

        //删除所有已加载的patch文件
        mPatchManager.removeAllPatch();
    */
    }

    /**
     * 2017/5/16
     * 描述：初始化网络请求框架OkhttpUtils
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
