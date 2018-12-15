package com.shanchain.shandata.base;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.soloader.SoLoader;
import com.shanchain.data.common.BaseApplication;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.shandata.db.ContactDao;
//import com.shanchain.shandata.manager.CharacterManager;
import com.shanchain.shandata.manager.LoginManager;
import com.shanchain.shandata.push.PushManager;
import com.shanchain.shandata.ui.view.activity.MainActivity;
//import com.shanchain.shandata.ui.view.activity.chat.ChatRoomActivity;
import com.shanchain.shandata.utils.Utils;
//import com.tencent.bugly.crashreport.CrashReport;
//import com.umeng.message.IUmengRegisterCallback;
//import com.umeng.message.PushAgent;
//import com.umeng.message.UmengMessageHandler;
//import com.umeng.message.UmengNotificationClickHandler;
//import com.umeng.message.entity.UMessage;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
//import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
//import me.shaohui.shareutil.ShareConfig;
//import me.shaohui.shareutil.ShareManager;
import okhttp3.OkHttpClient;

import static com.shanchain.data.common.base.Constants.CACHE_CUR_USER;
import static com.shanchain.data.common.base.Constants.CACHE_USER_MSG_IS_RECEIVE;
import static com.shanchain.data.common.base.Constants.SP_KEY_DEVICE_TOKEN;


public class MyApplication extends BaseApplication {

    private static Context mContext;
    private LocationClient locationClient;
    public static final int IMAGE_MESSAGE = 1;
    public static final int TAKE_PHOTO_MESSAGE = 2;
    public static final int TAKE_LOCATION = 3;
    public static final int FILE_MESSAGE = 4;
    public static final int TACK_VIDEO = 5;
    public static final int TACK_VOICE = 6;
    public static final int BUSINESS_CARD = 7;
    public static final int REQUEST_CODE_SEND_FILE = 26;

    private static final String QQ_ID = "1106258060";
    private static final String WX_ID = "wx0c49828919e7fd03";
    private static final String QQ_KEY = "cc7M3ByR9jPcsIDg";
    private static final String WEIBO_ID = "2916880440";
    private static final String WEIBO_SECRET = "8a25275c367126c9c6708f90ab5d5edd";
    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    private static final String WX_SECRET = "3a8e3a6794d962d1dbbbea2041e57308";
    public static String PICTURE_DIR = "sdcard/JChatDemo/pictures/";
    public static String FILE_DIR = "sdcard/JChatDemo/recvFiles/";
/*
    UmengNotificationClickHandler mNotificationClickHandler = new UmengNotificationClickHandler() {
        @Override
        public void dealWithCustomAction(Context context, UMessage msg) {
            String userId = CommonCacheHelper.getInstance().getCache("0", CACHE_CUR_USER);

            String isReceiveMsg = CommonCacheHelper.getInstance().getCache(userId, CACHE_USER_MSG_IS_RECEIVE);
            if (TextUtils.isEmpty(isReceiveMsg) || isReceiveMsg.equalsIgnoreCase("true")) {
                PushManager.dealWithCustomClickAction(context, msg);
            }
        }
    };

    UmengMessageHandler messageHandler = new UmengMessageHandler() {
        @Override
        public void dealWithCustomMessage(final Context context, final UMessage msg) {
            new Handler(getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    String userId = CommonCacheHelper.getInstance().getCache("0", CACHE_CUR_USER);
                    String isReceiveMsg = CommonCacheHelper.getInstance().getCache(userId, CACHE_USER_MSG_IS_RECEIVE);
                    if (TextUtils.isEmpty(isReceiveMsg) || isReceiveMsg.equalsIgnoreCase("true")) {
                        PushManager.dealWithCustomMessage(context, msg);
                    }
                }
            });
        }
    };*/


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        SoLoader.init(this, /* native exopackage */ false);
        SDKInitializer.initialize(this);//初始化百度地图sdk
        initBaiduMap();//初始化百度地图
        Utils.init(this);
        initOkhttpUtils();
        initJMessage();
//        initUPush();
//        initHuanXin();
        initDB();
        initSCCache();
        initShareAndLogin();
//        initInstance();
//        initUmengSocial();
        initBugly();//bugly崩溃日志上报
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initBaiduMap() {
        locationClient = new LocationClient(getApplicationContext());//声明LocationClient类
//        locationClient.registerLocationListener(this);//注册监听函数
        LocationClientOption option = new LocationClientOption();//创建定位配置参数
        //显示位置描述信息
        option.setIsNeedLocationDescribe(true);

        locationClient.setLocOption(option);//设置定位参数
        locationClient.start();
    }

    /** bugly崩溃日志上报*/
    private void initBugly() {
//        CrashReport.initCrashReport(getApplicationContext(),"adaaf0fcb3",true);
    }

//    private void initUmengSocial() {
//        //全局异常捕捉
//          /*Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//              try {
//                    String product = Build.PRODUCT;
//                    String device = Build.DEVICE;
//                    String board = Build.BOARD;
//                    String bootloader = Build.BOOTLOADER;
//                    String brand = Build.BRAND;
//                    String display = Build.DISPLAY;
//                    String fingerprint = Build.FINGERPRINT;
//                    String hardware = Build.HARDWARE;
//                    String host = Build.HOST;
//                    String id = Build.ID;
//                    String manufacturer = Build.MANUFACTURER;
//                    String model = Build.MODEL;
//                    String serial = Build.SERIAL;
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("produt = " + product);
//                    sb.append("device = " + device);
//                    sb.append("board = " + board);
//                    sb.append("bootloader = " + bootloader);
//                    sb.append("brand = " + brand);
//                    sb.append("display = " + display);
//                    sb.append("fingerprint = " + fingerprint);
//                    sb.append("hardware = " + hardware);
//                    sb.append("host = " + host);
//                    sb.append("id = " + id);
//                    sb.append("manufacturer = " + manufacturer);
//                    sb.append("model = " + model);
//                    sb.append("serial = " + serial);
//
//                    Writer writer = new StringWriter();
//                    PrintWriter printWriter = new PrintWriter(writer);
//                    e.printStackTrace(printWriter);
//                    printWriter.flush();
//                    writer.flush();
//                    String s1 = writer.toString();
//                    writer.close();
//                    printWriter.close();
//                    sb.append(s1);
//                    String errorLog = sb.toString();
//                    LogUtils.i(errorLog);
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });*/
//    }

//    private void initInstance() {
//        CharacterManager.getInstance();
//        LoginManager.getInstance(this);
//    }

    private void initSCCache() {
        SCCacheUtils.initCache(this);
    }


    public static Context getAppContext() {
        return mContext;
    }

    /**
     * 描述：初始化本地数据库
     */
    private void initDB() {
        //初始化联系人数据库
        ContactDao.initContactDao(this);
    }

    /**
     * 初始化第三方登录和分享
     */
    public void initShareAndLogin() {
//        ShareConfig config = ShareConfig.instance()
//                .qqId(QQ_ID)
//                .wxId(WX_ID)
//                .weiboId(WEIBO_ID)
//                // 下面两个，如果不需要登录功能，可不填写
//                .weiboRedirectUrl(REDIRECT_URL)
//                .wxSecret(WX_SECRET);
//        ShareManager.init(config);
    }

//    private void initUPush() {
//        final PushAgent mPushAgent = PushAgent.getInstance(this);
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //注册推送服务，每次调用register方法都会回调该接口
//                mPushAgent.register(new IUmengRegisterCallback() {
//                    @Override
//                    public void onSuccess(String deviceToken) {
//                        if (TextUtils.isEmpty(deviceToken)) {
//                            return;
//                        }
//                        PrefUtils.putString(AppManager.getInstance().getContext(), SP_KEY_DEVICE_TOKEN, deviceToken);
//                    }
//
//                    @Override
//                    public void onFailure(String s, String s1) {
//
//                    }
//                });
//            }
//        });
//        thread.start();
//        mPushAgent.setNotificationClickHandler(mNotificationClickHandler);
//        mPushAgent.setMessageHandler(messageHandler);
////        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
//    }


    /**
     * 描述：初始化环信
     */
//    private void initHuanXin() {
//        EMOptions options = new EMOptions();
//        //设置添加好友时需要验证
//        options.setAcceptInvitationAlways(false);
//        //禁止自动登录
//        options.setAutoLogin(false);
//        int pid = android.os.Process.myPid();
//        String processAppName = getAppName(pid);
//        // 如果APP启用了远程的service，此application:onCreate会被调用2次
//        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
//        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
//
//        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
//            Log.e("myApplication", "enter the service process!");
//
//            // 则此application::onCreate 是被service 调用的，直接返回
//            return;
//        }
//
//        //初始化
//        EMClient.getInstance().init(mContext, options);
//        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);
//        //初始化easeui
//        //EaseUI.getInstance().init(mContext, options);
//
//        //添加全局消息监听
//        initMsgListener();
//
//        //添加全局通讯录监听
//        initContactListener();
//    }

    /*
    * 初始化极光IM
    * */
    private void initJMessage() {
        PlatformConfig platformConfig = new PlatformConfig()
                .setWechat(WX_ID, WX_SECRET)
                .setQQ(QQ_ID, QQ_KEY)
                .setSinaWeibo(WEIBO_ID, WEIBO_SECRET, REDIRECT_URL);
//                .setFacebook("1847959632183996", "JShareDemo")
//                .setTwitter("fCm4SUcgYI1wUACGxB2erX5pL", "NAhzwYCgm15FBILWqXYDKxpryiuDlEQWZ5YERnO1D89VBtZO6q")
//                .setJchatPro("1847959632183996");

        JMessageClient.init(this,true);
        JPushInterface.init(this);//初始化极光推送
        JShareInterface.init(this,platformConfig);//极光分享
        JMessageClient.setDebugMode(true);
        JShareInterface.setDebugMode(true);
    }
//    private void initContactListener() {
//        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
//            @Override
//            public void onContactAdded(String s) {
//                //好友请求被同意
//
//            }
//
//            @Override
//            public void onContactDeleted(String s) {
//                //被删除时回调此方法
//
//            }
//
//            @Override
//            public void onContactInvited(String s, String s1) {
//                //收到好友邀请
//
//            }
//
//            @Override
//            public void onFriendRequestAccepted(String s) {
//                //添加好友成功时的回调
//
//            }
//
//            @Override
//            public void onFriendRequestDeclined(String s) {
//                //添加好友呗拒绝的回调
//
//            }
//        });
//    }

//    private void initMsgListener() {
//        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
//            @Override
//            public void onMessageReceived(List<EMMessage> list) {
//                //当接收到消息的回调
//                if (list != null && list.size() > 0) {
//
//                    if (isRunningBackground()) {
//                        sendNotification(list.get(0));
//                    }
//
//                    EventBus.getDefault().post(list.get(0));
//                    LogUtils.d("接收到消息" + list.get(0).getBody().toString());
//                }
//            }
//
//            @Override
//            public void onCmdMessageReceived(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageRead(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageDelivered(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageRecalled(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageChanged(EMMessage emMessage, Object o) {
//
//            }
//        });
//    }

    /**
     * 描述：初始化网络请求框架OkhttpUtils
     */
    private void initOkhttpUtils() {
        try {
            if (Constants.SC_ENV_PRD) {
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
            } else {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new LoggerInterceptor("TAG"))
                        .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                        .readTimeout(60000L, TimeUnit.MILLISECONDS)
                        //其他配置
                        .build();
                OkHttpUtils.initClient(okHttpClient);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

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


    private boolean isRunningBackground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        if (runningTaskInfo.topActivity.getPackageName().equals(getPackageName())) {
            return false;
        } else {
            return true;
        }
    }

//    private void sendNotification(EMMessage message) {
//        EMTextMessageBody messageBody = (EMTextMessageBody) message.getBody();
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        //延时意图
//        /**
//         * 参数2：请求码 大于1
//         */
//        Intent mainIntent = new Intent(this, MainActivity.class);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Intent chatIntent = new Intent(this, ChatRoomActivity.class);
//        chatIntent.putExtra("toChatName", message.getFrom());
//
//        chatIntent.putExtra("isGroup", message.getChatType() == EMMessage.ChatType.GroupChat ? true : false);
//
//        Intent[] intents = {mainIntent, chatIntent};
//        PendingIntent pendingIntent = PendingIntent.getActivities(this, 1, intents, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new Notification.Builder(this)
//                .setAutoCancel(true) //当点击后自动删除
//                .setSmallIcon(R.mipmap.abs_home_btn_comment_default) //必须设置
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.abs_addanewrole_def_photo_default))
//                .setContentTitle("您有一条新消息")
//                .setContentText(messageBody.getMessage())
//                .setContentInfo(message.getFrom())
//                .setContentIntent(pendingIntent)
//                .setPriority(Notification.PRIORITY_MAX)
//                .build();
//        notificationManager.notify(1, notification);
//    }

//    @Override
//    public void onReceiveLocation(BDLocation bdLocation) {
//
//    }
}
