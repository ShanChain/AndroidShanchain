package com.shanchain.shandata.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.android.tony.defenselib.DefenseCrash;
import com.android.tony.defenselib.handler.IExceptionHandler;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.soloader.SoLoader;
import com.shanchain.data.common.BaseApplication;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.BuildConfig;
import com.shanchain.shandata.db.ContactDao;
import com.shanchain.shandata.db.DaoMaster;
import com.shanchain.shandata.db.DaoSession;
import com.shanchain.shandata.ui.model.FriendEntry;
import com.shanchain.shandata.ui.model.FriendRecommendEntry;
import com.shanchain.shandata.ui.model.UserEntry;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.utils.Utils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.service.PatchResult;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.tinkerpatch.sdk.server.callback.ConfigRequestCallback;
import com.tinkerpatch.sdk.server.callback.RollbackCallBack;
import com.tinkerpatch.sdk.server.callback.TinkerPatchRequestCallback;
import com.tinkerpatch.sdk.tinker.callback.ResultCallBack;
import com.tinkerpatch.sdk.tinker.service.TinkerServerResultService;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.api.BasicCallback;
import okhttp3.OkHttpClient;

//import com.shanchain.shandata.manager.CharacterManager;
//import com.shanchain.shandata.push.PushManager;
//import com.shanchain.shandata.ui.view.activity.chat.ChatRoomActivity;
//import com.tencent.bugly.crashreport.CrashReport;
//import com.umeng.message.IUmengRegisterCallback;
//import com.umeng.message.PushAgent;
//import com.umeng.message.UmengMessageHandler;
//import com.umeng.message.UmengNotificationClickHandler;
//import com.umeng.message.entity.UMessage;
//import cn.jpush.android.api.JPushInterface;
//import me.shaohui.shareutil.ShareConfig;
//import me.shaohui.shareutil.ShareManager;


public class MyApplication extends BaseApplication implements IExceptionHandler {

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
    public static final int START_YEAR = 1900;
    public static final int END_YEAR = 2050;
    public static final int RESULT_CODE_SELECT_FRIEND = 23;
    public static final long INIT_CHAT_ROOM_ID = 15198852; //测试聊天室的id

    public static String systemLanguge;
    private static final String TAG = "MyApplication";
    private static final String QQ_ID = "1106258060";
    private static final String WX_ID = "wx0c49828919e7fd03";
    private static final String QQ_KEY = "cc7M3ByR9jPcsIDg";
    //    private static final String WEIBO_ID = "2916880440";
    private static final String WEIBO_ID = "1619531897";
    //    private static final String WEIBO_SECRET = "8a25275c367126c9c6708f90ab5d5edd";
    private static final String WEIBO_SECRET = "a669a85dffd04fc23f2051c96c73c68f";
    private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    private static final String WX_SECRET = "3a8e3a6794d962d1dbbbea2041e57308";
    public static String PICTURE_DIR = "sdcard/JChatDemo/pictures/";
    public static String FILE_DIR = "sdcard/JChatDemo/recvFiles/";
    public static final String CONV_TITLE = "conv_title";
    public static final int RESULT_CODE_FRIEND_INFO = 17;
    public static final int RESULT_CODE_EDIT_NOTENAME = 29;
    public static final String NOTENAME = "notename";
    public static final String TARGET_ID = "targetId";
    public static final String ATUSER = "atuser";
    public static final String TARGET_APP_KEY = "targetAppKey";
    public static int maxImgCount;               //允许选择图片最大数
    public static final String GROUP_NAME = "groupName";
    private ApplicationLike tinkerApplicationLike;
    private static DaoSession daoSession;

    /**
     * 描述：本地手机设备号
     */
    protected static String deviceId;

    /**
     * 描述：与极光推送对应的设备号
     */
    protected static String registrationId;

    /**
     * 描述：是否开通免密
     */
    protected static boolean isBindPwd = false;

    /**
     * 描述：是否开通推送
     */
    protected static boolean allowNotify = false;

    /**
     * 描述：是否开通推送
     */
    protected static boolean isRealName = false;
    private static DaoMaster daoMaster;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        setSystemLanguge(mContext.getResources().getConfiguration().locale.getLanguage());
        SoLoader.init(this, /* native exopackage */ false);
        //加载ActiveAndroid配置文件
        Configuration.Builder builder = new Configuration.Builder(this);
        //手动的添加模型类
        builder.addModelClasses(UserEntry.class);
        builder.addModelClasses(FriendEntry.class);
        builder.addModelClasses(FriendRecommendEntry.class);
        ActiveAndroid.initialize(builder.create());
        //facebook初始化
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        SDKInitializer.initialize(this);//初始化百度地图sdk
        initBaiduMap();//初始化百度地图
        Utils.init(this);
        initOkhttpUtils();
        initJMessage();
        //初始化二维码
        ZXingLibrary.initDisplayOpinion(this);
        initDB();
        initSCCache();
        //初始化Tinker热修复
//        initTinkerPatch();
        useSample();
        initBugly();//bugly崩溃日志上报
    }

    public String getSystemLanguge() {
        return systemLanguge;
    }

    public static void setSystemLanguge(String sysLanguge) {
        systemLanguge = sysLanguge;
    }

    public static UserEntry getUserEntry() {
        return UserEntry.getUser(JMessageClient.getMyInfo().getUserName(), JMessageClient.getMyInfo().getAppKey());
    }

    private void initBaiduMap() {
        locationClient = new LocationClient(getApplicationContext());//声明LocationClient类
//        locationClient.registerLocationListener(this);//注册监听函数
        LocationClientOption option = new LocationClientOption();//创建定位配置参数
        //显示位置描述信息
        option.setIsNeedLocationDescribe(true);
        locationClient.setLocOption(option);//设置定位参数
        locationClient.start();
        Intent intent = new Intent();
//        intent.setAction(".receiver.MyLocationReceiver");
//        sendBroadcast(intent);
    }

    /**
     * bugly崩溃日志上报
     */
    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "adaaf0fcb3", true);
    }

    /**
     * 我们需要确保至少对主进程跟patch进程初始化 TinkerPatch
     */
    private void initTinkerPatch() {
        // 我们可以从这里获得Tinker加载过程的信息
        if (BuildConfig.TINKER_ENABLE) {
            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
            // 初始化TinkerPatch SDK
            TinkerPatch.init(
                    tinkerApplicationLike
//                new TinkerPatch.Builder(tinkerApplicationLike)
//                    .requestLoader(new OkHttp3Loader())
//                    .build()
            )
                    .reflectPatchLibrary()
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true)
                    .setFetchPatchIntervalByHours(3);
            // 获取当前的补丁版本
            Log.d(TAG, "Current patch version is " + TinkerPatch.with().getPatchVersion());

            // fetchPatchUpdateAndPollWithInterval 与 fetchPatchUpdate(false)
            // 不同的是，会通过handler的方式去轮询
            TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
        }
    }

    /**
     * 在这里给出TinkerPatch的所有接口解释
     * 更详细的解释请参考:http://tinkerpatch.com/Docs/api
     */
    private void useSample() {
        if (BuildConfig.TINKER_ENABLE) {
            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
            TinkerPatch.init(tinkerApplicationLike)
                    //是否自动反射Library路径,无须手动加载补丁中的So文件
                    //注意,调用在反射接口之后才能生效,你也可以使用Tinker的方式加载Library
                    .reflectPatchLibrary()
                    //向后台获取是否有补丁包更新,默认的访问间隔为3个小时
                    //若参数为true,即每次调用都会真正的访问后台配置
                    .fetchPatchUpdate(false)
                    //设置访问后台补丁包更新配置的时间间隔,默认为3个小时
                    .setFetchPatchIntervalByHours(3)
                    //向后台获得动态配置,默认的访问间隔为3个小时
                    //若参数为true,即每次调用都会真正的访问后台配置
                    .fetchDynamicConfig(new ConfigRequestCallback() {
                        @Override
                        public void onSuccess(HashMap<String, String> hashMap) {

                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    }, false)
                    //设置访问后台动态配置的时间间隔,默认为3个小时
                    .setFetchDynamicConfigIntervalByHours(3)
                    //设置当前渠道号,对于某些渠道我们可能会想屏蔽补丁功能
                    //设置渠道后,我们就可以使用后台的条件控制渠道更新
                    .setAppChannel(getAppMetaData(mContext, "UMENG_CHANNEL"))
                    //屏蔽部分渠道的补丁功能
                    .addIgnoreAppChannel("googleplay")
                    //设置tinkerpatch平台的条件下发参数
                    .setPatchCondition("userId", "" + SCCacheUtils.getCacheUserId())
                    //设置补丁合成成功后,锁屏重启程序
                    //默认是等应用自然重启
                    .setPatchRestartOnSrceenOff(true)
                    //我们可以通过ResultCallBack设置对合成后的回调
                    //例如弹框什么
                    //注意，setPatchResultCallback 的回调是运行在 intentService 的线程中
                    .setPatchResultCallback(new ResultCallBack() {
                        @Override
                        public void onPatchResult(PatchResult patchResult) {
                            Log.i(TAG, "onPatchResult callback here");
                        }
                    })
                    //设置收到后台回退要求时,锁屏清除补丁
                    //默认是等主进程重启时自动清除
                    .setPatchRollbackOnScreenOff(true)
                    //我们可以通过RollbackCallBack设置对回退时的回调
                    .setPatchRollBackCallback(new RollbackCallBack() {
                        @Override
                        public void onPatchRollback() {
                            Log.i(TAG, "onPatchRollback callback here");
                        }
                    });
        }
    }

    /**
     * 自定义Tinker类的高级用法, 使用更灵活，但是需要对tinker有更进一步的了解
     * 更详细的解释请参考:http://tinkerpatch.com/Docs/api
     */
    private void complexSample() {
        //修改tinker的构造函数,自定义类
        TinkerPatch.Builder builder = new TinkerPatch.Builder(tinkerApplicationLike)
                .listener(new DefaultPatchListener(this))
                .loadReporter(new DefaultLoadReporter(this))
                .patchReporter(new DefaultPatchReporter(this))
                .resultServiceClass(TinkerServerResultService.class)
                .upgradePatch(new UpgradePatch())
                .patchRequestCallback(new TinkerPatchRequestCallback());
        //.requestLoader(new OkHttpLoader());

        TinkerPatch.init(builder.build());
    }

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
        //初始化GreenDao数据库
        //升级时调用onUpgrade（）方法，删除所有表！。
        try {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, "app_chat_message.db");
            SQLiteDatabase database = devOpenHelper.getWritableDatabase();
            daoMaster = new DaoMaster(database);
            daoSession = daoMaster.newSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DaoSession getDaoSession() {
        if (null == daoSession) {
            //升级时调用onUpgrade（）方法，删除所有表！。
            try {
                DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, "app_chat_message.db");
                SQLiteDatabase database = devOpenHelper.getWritableDatabase();
                daoMaster = new DaoMaster(database);
                daoSession = daoMaster.newSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return daoSession;
    }

    public static DaoMaster getDaoMaster() {
        if (null == daoMaster) {
            //升级时调用onUpgrade（）方法，删除所有表！。
            try {
                DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, "app_chat_message.db");
                SQLiteDatabase database = devOpenHelper.getWritableDatabase();
                daoMaster = new DaoMaster(database);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return daoMaster;
    }

    /*
     * 初始化极光IM
     * */
    private void initJMessage() {
        PlatformConfig platformConfig = new PlatformConfig()
                .setWechat(WX_ID, WX_SECRET)
                .setQQ(QQ_ID, QQ_KEY)
                .setSinaWeibo(WEIBO_ID, WEIBO_SECRET, REDIRECT_URL)
                .setFacebook("351737942088473", "MarJar");
//                .setTwitter("fCm4SUcgYI1wUACGxB2erX5pL", "NAhzwYCgm15FBILWqXYDKxpryiuDlEQWZ5YERnO1D89VBtZO6q")
//                .setJchatPro("1847959632183996");
        JMessageClient.setDebugMode(true);
        JPushInterface.setDebugMode(true);
        JShareInterface.setDebugMode(true);
        JPushInterface.init(this);//初始化极光推送
        JMessageClient.init(this, true);
        JMessageClient.registerEventReceiver(new MessageListActivity());
        JShareInterface.init(this, platformConfig);//极光分享
        String RegistrationID = JPushInterface.getRegistrationID(this);
        LogUtils.d("JPushInterface", RegistrationID);
        boolean guided = PrefUtils.getBoolean(mContext, Constants.SP_KEY_GUIDE, false);
        if (guided == false) {
            enterChatRoom(INIT_CHAT_ROOM_ID);
        }
    }

    //加入聊天室
    private void enterChatRoom(final long roomID) {
        ChatRoomManager.enterChatRoom(Long.valueOf(roomID), new RequestCallback<Conversation>() {
            @Override
            public void gotResult(int i, String s, Conversation conversation) {
                LogUtils.d("enterChatRoom", "roomID:" + roomID);
                if (i == 0) {
                    LogUtils.d("enterChatRoom", "加入聊天室成功 code:" + i);
                } else if (i == 851003) {//成员已在聊天室
                    LogUtils.d("enterChatRoom", "成员已在聊天室 code:" + i);
                    ChatRoomManager.leaveChatRoom(roomID, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                            }
                            enterChatRoom(roomID);
                        }
                    });
                } else if (i == 871300) {//未登录
                    LogUtils.d("enterChatRoom", "成员未登录 code:" + i);
                    JMessageClient.login(SCCacheUtils.getCacheHxUserName(), SCCacheUtils.getCacheHxUserName(), new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            int ii = i;
                            JMessageClient.login(SCCacheUtils.getCacheHxUserName(), SCCacheUtils.getCacheHxPwd(), new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        enterChatRoom(roomID);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }


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

    /**
     * 获取app当前的渠道号或application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
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

    public String getDeviceId() {
        return deviceId;
    }

    public static void setDeviceId(String devId) {
        deviceId = devId;
    }

    public static String getRegistrationId() {
        return registrationId;
    }

    public static void setRegistrationId(String regId) {
        registrationId = regId;
    }

    public static boolean isBindPwd() {
        return isBindPwd;
    }

    public static void setBindPwd(boolean binPwd) {
        isBindPwd = binPwd;
    }

    public static boolean isAllowNotify() {
        return allowNotify;
    }

    public static void setAllowNotify(boolean allNotify) {
        allowNotify = allNotify;
    }

    public static boolean isRealName() {
        return isRealName;
    }

    public static void setRealName(boolean realName) {
        isRealName = realName;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        // step1: Initialize the lib.
        DefenseCrash.initialize();
        // setp2: Install the fire wall defense.
        DefenseCrash.install(this);
    }

    @Override
    public void onCaughtException(Thread thread, Throwable throwable, boolean b) {
        // step3: 打印错误堆栈
        throwable.printStackTrace();
        // step4: 上报该错误到错误收集的平台,例如国内的Bugly,友盟等
        CrashReport.postCatchedException(throwable, thread);
    }

    /* 框架使程序运行进入了安全模式,这种情况是在程序运行过程中发生了崩溃,
    但是不要慌,已经被框架拦截并且进入了安全模式,这里你可以什么都不用做.
    */
    @Override
    public void onEnterSafeMode() {

    }

    /* 这个回调说明在onLayout(),onMeasure() 和 onDraw()中出现了崩溃
     这种崩溃会导致绘图异常和编舞者类崩溃我们建议你在这时候,重启当前的activity或者应用
     */
    @Override
    public void onMayBeBlackScreen(Throwable throwable) {
        throwable.printStackTrace();
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
