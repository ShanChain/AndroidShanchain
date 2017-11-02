package com.shanchain.arkspot.base;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.facebook.soloader.SoLoader;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.db.ContactDao;
import com.shanchain.arkspot.ui.view.activity.MainActivity;
import com.shanchain.arkspot.ui.view.activity.chat.ChatRoomActivity;
import com.shanchain.arkspot.utils.Utils;
import com.shanchain.data.common.BaseApplication;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.shaohui.shareutil.ShareConfig;
import me.shaohui.shareutil.ShareManager;
import okhttp3.OkHttpClient;


public class MyApplication extends BaseApplication {

    private static Context mContext;

    private static final String QQ_ID = "1106258060";
    private static final String WX_ID = "wx0c49828919e7fd03";

    private static final String WEIBO_ID = "2916880440";
    private static final String REDIRECT_URL = "http://api.weibo.com/oauth2/default.html";
    private static final String WX_SECRET = "3a8e3a6794d962d1dbbbea2041e57308";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        SoLoader.init(this, /* native exopackage */ false);
        Utils.init(this);
        initOkhttpUtils();
        initHuanXin();
        initDB();
        initSCCache();
        initShareAndLogin();
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


    /**
     * 描述：初始化环信
     */
    private void initHuanXin() {
        EMOptions options = new EMOptions();
        //设置添加好友时需要验证
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            Log.e("myApplication", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(mContext, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //初始化easeui
        //EaseUI.getInstance().init(mContext, options);

        //添加全局消息监听
        initMsgListener();

        //添加全局通讯录监听
        initContactListener();
    }

    private void initContactListener() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                //好友请求被同意

            }

            @Override
            public void onContactDeleted(String s) {
                //被删除时回调此方法

            }

            @Override
            public void onContactInvited(String s, String s1) {
                //收到好友邀请

            }

            @Override
            public void onFriendRequestAccepted(String s) {
                //添加好友成功时的回调

            }

            @Override
            public void onFriendRequestDeclined(String s) {
                //添加好友呗拒绝的回调

            }
        });
    }

    private void initMsgListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                //当接收到消息的回调
                if (list != null && list.size() > 0) {

                    if (isRunningBackground()){
                        sendNotification(list.get(0));
                    }

                    EventBus.getDefault().post(list.get(0));
                    LogUtils.d("接收到消息" + list.get(0).getBody().toString());
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
    }

    /**
     * 描述：初始化网络请求框架OkhttpUtils
     */
    private void initOkhttpUtils() {
        try {
//            HttpsUtils.SSLParams sslParams =
//                    HttpsUtils.getSslSocketFactory(new InputStream[]{getAssets().open("certificates.cer")}, null, null);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LoggerInterceptor("TAG"))
                    //.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                    .readTimeout(30000L, TimeUnit.MILLISECONDS)
                    //其他配置
                    .build();
            OkHttpUtils.initClient(okHttpClient);
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

    private void sendNotification(EMMessage message) {
        EMTextMessageBody messageBody = (EMTextMessageBody) message.getBody();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //延时意图
        /**
         * 参数2：请求码 大于1
         */
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chatIntent = new Intent(this, ChatRoomActivity.class);
        chatIntent.putExtra("toChatName",message.getFrom());

        chatIntent.putExtra("isGroup",message.getChatType() == EMMessage.ChatType.GroupChat?true:false);

        Intent[] intents = {mainIntent,chatIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(this,1,intents,PendingIntent.FLAG_UPDATE_CURRENT) ;
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true) //当点击后自动删除
                .setSmallIcon(R.mipmap.abs_home_btn_comment_default) //必须设置
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.abs_addanewrole_def_photo_default))
                .setContentTitle("您有一条新消息")
                .setContentText(messageBody.getMessage())
                .setContentInfo(message.getFrom())
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        notificationManager.notify(1,notification);
    }

}
