package com.shanchain.shandata.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.alipay.euler.andfix.patch.PatchManager;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;
import okhttp3.OkHttpClient;

import com.shanchain.shandata.BuildConfig;

public class MyApplication extends Application implements ReactApplication{

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

    public Vibrator mVibrator;
    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage()
            );
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);

        mContext = getApplicationContext();

        initOkhttpUtils();

        initAndFix();
        initShareAndLogin();
        initUmeng();
        initUPush();
        initHuanXin();
    }

    private void initHuanXin() {
        EMOptions options = new EMOptions();
        //设置添加好友时需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            Log.e("myApplication", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(mContext, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //初始化easeui
        EaseUI.getInstance().init(mContext,options);
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


    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

}
