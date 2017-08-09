package com.shanchain.shandata.base;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

import com.alipay.euler.andfix.patch.PatchManager;
import com.shanchain.shandata.utils.LocationService;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.utils.VersionUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    private static final String QQ_ID = "1106258060";
    private static final String WX_ID = "wx0c49828919e7fd03";

    private static final String WEIBO_ID = "2916880440";
    private static final String REDIRECT_URL = "http://api.weibo.com/oauth2/default.html";
    private static final String WX_SECRET = "3a8e3a6794d962d1dbbbea2041e57308";

    private PatchManager patchManager;

    public static Context getContext() {
        return mContext;
    }

    public static Context mContext;

    public LocationService locationService;
    public Vibrator mVibrator;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        initOkhttpUtils();

        initAndFix();
        initShareAndLogin();
        initUmeng();
        initUPush();
    }

    //友盟自定义推送消息事件
    UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
        @Override
        public void dealWithCustomAction(Context context, UMessage msg) {
            Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();

        }
    };

    private void initUPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.d("U-Push : " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    private void initUmeng() {
        MobclickAgent.openActivityDurationTrack(false);
    }

    private void initAndFix() {
        patchManager = new PatchManager(mContext);
        //current version
        patchManager.init(VersionUtils.getVersionName(mContext));
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
     * 描述：初始化网络请求框架OkhttpUtils
     */
    private void initOkhttpUtils() {
        try {
            HttpsUtils.SSLParams sslParams =
                    HttpsUtils.getSslSocketFactory(new InputStream[]{getAssets().open("certificates.cer")}, null, null);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LoggerInterceptor("TAG"))
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                    .readTimeout(60000L, TimeUnit.MILLISECONDS)
                    //其他配置
                    .build();
            OkHttpUtils.initClient(okHttpClient);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 初始化第三方登录和分享
     */
    public void initShareAndLogin() {
        ShareConfig config = ShareConfig.instance()
                .qqId(QQ_ID)
                .wxId(WX_ID)
                .weiboId(WEIBO_ID)
                // 下面两个，如果不需要登录功能，可不填写
                .weiboRedirectUrl(REDIRECT_URL)
                .wxSecret(WX_SECRET);
        ShareManager.init(config);
    }

}
