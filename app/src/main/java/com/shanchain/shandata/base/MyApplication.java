package com.shanchain.shandata.base;

import android.app.Application;
import android.content.Context;

import com.alipay.euler.andfix.patch.PatchManager;
import com.shanchain.shandata.utils.VersionUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 周建 on 2017/5/13.
 */

public class MyApplication extends Application {

    private static final String QQ_ID = "1106258060";
    private static final String WX_ID = "wx0c49828919e7fd03";
    private static final String WEIBO_ID = "2099719405";
    private static final String REDIRECT_URL = "";

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
     //   initShareAndLogin();

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


    /**
     *  初始化第三方登录和分享
     */
   /*  public void initShareAndLogin(){
         ShareConfig config = ShareConfig.instance()
                 .qqId(QQ_ID)
                 .wxId(WX_ID)
                 .weiboId(WEIBO_ID)
                 // 下面两个，如果不需要登录功能，可不填写
                 .weiboRedirectUrl(REDIRECT_URL)
                 .wxSecret(WX_ID);
         ShareManager.init(config);
     }*/

}
