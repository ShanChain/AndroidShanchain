package com.shanchain.shandata.base;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.net.UpdateAppHttpUtil;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.Coordinates;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.utils.PermissionHelper;
import com.umeng.analytics.MobclickAgent;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.service.DownloadService;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.shanchain.data.common.base.Constants.CACHE_DEVICE_TOKEN_STATUS;
import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;
import static com.shanchain.data.common.base.Constants.SP_KEY_DEVICE_TOKEN;
import static com.shanchain.data.common.utils.SystemUtils.FlymeSetStatusBarLightModeWithWhiteColor;
import static com.shanchain.data.common.utils.SystemUtils.MIUISetStatusBarLightModeWithWhiteColor;
import static com.shanchain.data.common.utils.SystemUtils.setImmersiveStatusBar_API21;
import static com.shanchain.data.common.utils.SystemUtils.setStatusBarLightMode_API23;

//import com.umeng.message.PushAgent;


public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 描述：Log日志 Tag
     */
    protected static String TAG = null;

    /**
     * 描述：本地手机设备号
     */
    protected String deviceId;

    /**
     * 描述：与极光推送对应的设备号
     */
    protected String registrationId;

    /**
     * 描述：是否开通免密
     */
    protected boolean isBindPwd = false;

    /**
     * 描述：是否开通推送
     */
    protected boolean allowNotify = false;

    /**
     * 描述：是否开通推送
     */
    protected boolean isRealName = false;

    /**
     * 描述：上下文对象
     */
    protected Context mContext = null;

    /**
     * 描述：Activity对象
     */
    protected static Activity mActivity = null;

    // 屏幕信息 -------------------------

    /**
     * 描述：屏幕宽度
     */
    protected int mScreenWidth = 0;

    /**
     * 描述：屏幕高度
     */
    protected int mScreenHeight = 0;

    /**
     * 描述：屏幕密度
     */
    protected float mScreenDensity = 0.0f;
    /**
     * 描述：上传密码对话框
     */
    protected CustomDialog commonDialog = null;
    /**
     * 描述：身份验证码
     */
    protected String authCode;

    private CustomDialog mCustomDialog;
    private ProgressDialog mDialog;
    private PermissionHelper mPermissionHelper;
    private Context mBusContext;
    private File mPasswordFile;
    private CustomDialog mNewCustomDialog;
    private StandardDialog mStandardDialog;
    private CustomDialog showPasswordDialog;
    private Coordinates coordinates;
    private LatLng myLatLng;
    //百度地图
    private LocationClient locationClient;
    private BDLocationListener bdLocationListener;

    /**
     * 描述: onCreate 初始化
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 调用父类onCreate
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }        //获取极光推送绑定的设备号
        registrationId = JPushInterface.getRegistrationID(getApplicationContext());
        //获取本机唯一标识
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            }, 0);
//            deviceId = telephonyManager.getSimSerialNumber();
        } else {
//            deviceId = telephonyManager.getSimSerialNumber();
        }

//        RNManager.getInstance().init(getApplication());
        // 添加Activity入栈
        ActivityManager.getInstance().addActivity(this);
        ActivityStackManager.getInstance().addActivity(this);
        //判断Activity栈是否为空
        if (!ActivityStackManager.getInstance().getActivityStack().empty()) {
            JMessageClient.registerEventReceiver(mContext);
        }
        //竖屏锁定
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        try {
            // 获取Intent数据
            initIntent();
            // 初始化属性
            initAttribute();
            //初始化百度地图
//            initMap();
            // 初始化布局
            initLayout();
            // 初始化View和事件
            initViewsAndEvents();
            initStatusBar();
            initPushAgent();
            initDeviceToken();
            checkApkVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initMap() {
        if (myLatLng == null) {
            locationClient = new LocationClient(getApplicationContext());//创建LocationClient对象
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，设置定位模式,LocationMode.Hight_Accuracy：高精度；
            option.setCoorType("bd09ll");
            option.setScanSpan(3 * 60 * 1000);//可选，设置发起定位请求的间隔，int类型，单位ms,需设置1000ms以上才有效
            option.setOpenGps(true);//使用高精度和仅用设备两种定位模式的，参数必须设置为true
            option.setWifiCacheTimeOut(5 * 60 * 1000);//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
            locationClient.setLocOption(option);
            bdLocationListener = new MyLocationListener();
            locationClient.registerLocationListener(bdLocationListener);//注册监听函数
            locationClient.start();
        } else {
            getChatRoomInfo(myLatLng);
        }
    }

    ;

    protected void isRealName() {
        SCHttpUtils.getAndToken()
                .url(HttpApi.IS_REAL_NAME)
                .build()
                .execute(new SCHttpStringCallBack(mContext, new StandardDialog(mContext)) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(mContext, "网络异常");
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        final String code = SCJsonUtils.parseCode(response);
                        final String msg = SCJsonUtils.parseMsg(response);
                        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            isRealName = SCJsonUtils.parseBoolean(response, "data");
                            MyApplication.setRealName(isRealName);
                        } else {
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(mContext, code + ":" + msg);
                                }
                            });
                        }


                    }
                });

    }

    protected void initCurrentUserStatus() {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHARACTER_GET_CURRENT)
                .build()
                .execute(new SCHttpStringCallBack(mContext, new StandardDialog(mContext)) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("网络错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            if (TextUtils.isEmpty(data)) {
                                return;
                            }
                            String character = JSONObject.parseObject(data).getString("characterInfo");
                            CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                            if (!TextUtils.isEmpty(character)) {
                                isBindPwd = SCJsonUtils.parseBoolean(character, "isBindPwd");
                                allowNotify = SCJsonUtils.parseBoolean(character, "allowNotify");
                                MyApplication.setAllowNotify(allowNotify);
                                MyApplication.setBindPwd(isBindPwd);
                                setBindPwd(isBindPwd);
                            }
                        }
                    }
                });
    }

    private void initPushAgent() {
//        PushAgent.getInstance(this).onAppStart();
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setImmersiveStatusBar_API21(this, getResources().getColor(R.color.colorWhite));
            setStatusBarLightMode_API23(this);
        }
        MIUISetStatusBarLightModeWithWhiteColor(this, getWindow(), true);
        FlymeSetStatusBarLightModeWithWhiteColor(this, getWindow(), true);
    }

    /**
     * 描述: 初始化Intent传值
     */
    private void initIntent() {
        // 获取传值
        Bundle extras = getIntent().getExtras();
        // 校验是否为空，为空则不初始化
        if (null != extras) {
            getBundleExtras(extras);
        }
    }

    /**
     * 描述: 初始化属性
     */
    private void initAttribute() {
        // 获取当前类名至为TAG
        TAG = this.getClass().getSimpleName();
        // 初始化上下文对象
        mContext = this;
        // 初始化Activity对象
        mActivity = this;
        // 获取屏幕宽高及密度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 初始化屏幕属性
        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }

    /**
     * 描述: 初始化布局
     */
    private void initLayout() {
        // 根据返回LayoutID设置布局
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            //是否实名认证
//            isRealName();
            //获取当前角色的状态
//            initCurrentUserStatus();
            //上传图片对话框
            commonDialog = new CustomDialog(mContext, true, 1.0,
                    R.layout.dialog_bottom_wallet_password,
                    new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
        } else {
            throw new IllegalArgumentException("你必须返回一个正确的contentview布局的资源ID");
        }
    }

    /**
     * 描述: 重写setContentView 添加注解
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        // 调用父类方法
        super.setContentView(layoutResID);
        // 绑定注解
        ButterKnife.bind(this);
    }

    /**
     * 描述: 重写onOptionsItemSelected 监听手机HOME键（硬件及虚拟）
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //finish();
            LogUtils.d("click->home");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * 描述：接收EventBus通知
     * */
    @Subscribe
    public void onEventMainThread(EventBusObject event) {
        EventBusObject busObject = null;
        try {
            busObject = (EventBusObject) event;
            showPasswordDialog = (CustomDialog) busObject.getData();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        if (NetErrCode.WALLET_PHOTO == busObject.getCode()) {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
//                        创建上传密码图片弹窗
                    if (showPasswordDialog == null) {
                        ToastUtils.showToast(mContext, "" + NetErrCode.WALLET_PHOTO);
                        return;
                    }
                    showPasswordDialog.setPasswordBitmap(null);
                    showPasswordDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                        @Override
                        public void OnItemClick(CustomDialog dialog, View view) {
                            if (view.getId() == com.shanchain.common.R.id.iv_dialog_add_picture) {
                                selectImage(ActivityStackManager.getInstance().getTopActivity());
                            } else if (view.getId() == com.shanchain.common.R.id.tv_dialog_sure) {
                                ToastUtils.showToastLong(ActivityStackManager.getInstance().getTopActivity(), "请上传二维码图片");
                            }
                        }
                    });
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            showPasswordDialog.show();
                        }
                    });
                }
            });
        }
    }

    //监听用户登录状态
    public void onEventMainThread(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();//获取变更的原因
        UserInfo myInfo = event.getMyInfo();//获取当前被登出账号的信息
        switch (reason) {
            case user_password_change:
                //用户密码在服务器端被修改
                LogUtils.d("LoginStateChangeEvent", "用户密码在服务器端被修改");
//                ToastUtils.showToast(mContext, "您的密码已被修改");
                break;
            case user_logout:
                //用户换设备登录
                LogUtils.d("LoginStateChangeEvent", "账号在其他设备上登录");
                final StandardDialog standardDialog = new StandardDialog(mContext);
                standardDialog.setStandardTitle("提示");
                standardDialog.setStandardMsg("账号已在其他设备上登录，请重新登录");
                standardDialog.setSureText("重新登录");
                standardDialog.setCancelText("取消");
                standardDialog.setCallback(new com.shanchain.data.common.base.Callback() {//确定
                    @Override
                    public void invoke() {
                        readyGo(LoginActivity.class);
                    }
                }, new com.shanchain.data.common.base.Callback() {//取消
                    @Override
                    public void invoke() {

                    }
                });
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
//                        ToastUtils.showToast(getApplicationContext(), "账号在其他设备上登录");
                        standardDialog.show();
                    }
                });

                break;
            case user_deleted:
                //用户被删除
                break;
        }
    }


    protected void checkPassword(final File file) {
        //创建requestBody
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("suberUser", "" + SCCacheUtils.getCacheCharacterId())
                .addFormDataPart("userId", "" + SCCacheUtils.getCacheUserId())
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.WALLET_CHECK_USE_PASSWORD + "?token=" + SCCacheUtils.getCacheToken(), multiBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(mContext, "网络异常");

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String code = "";
                String msg = "";
                try {
                    code = SCJsonUtils.parseCode(result);
                    msg = SCJsonUtils.parseMsg(result);
                } catch (JSONException jsonException) {
                    code = "";
                    msg = "密码错误";
                }
                final String finalMsg = msg;
                final String finalCode = code;
                if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, "" + finalMsg);
                            passwordFree(file);
                            mNewCustomDialog.dismiss();
                        }
                    });
                } else {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mNewCustomDialog.dismiss();
                            ToastUtils.showToast(mContext, finalCode + ":" + finalMsg);
                        }
                    });
                }
            }
        });
    }

    protected void passwordFree(final File file) {
        //设置免密操作
        final Handler freePasswordHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final boolean bind = (boolean) msg.obj;
                switch (msg.what) {
                    case 1:
                        if (!TextUtils.isEmpty(SCCacheUtils.getCacheAuthCode())) {
                            Map stringMap = new HashMap();
                            stringMap.put("os", "android");
                            stringMap.put("token", "" + SCCacheUtils.getCacheToken());
                            stringMap.put("deviceToken", "" + JPushInterface.getRegistrationID(mContext));
                            stringMap.put("bind", bind);
                            String modifyUser = JSONObject.toJSONString(stringMap);
                            SCHttpUtils.postWithUserId()
                                    .url(HttpApi.MODIFY_CHARACTER)
                                    .addParams("characterId", "" + SCCacheUtils.getCacheCharacterId())
                                    .addParams("dataString", modifyUser)
                                    .build()
                                    .execute(new SCHttpStringCallBack() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            LogUtils.d("修改角色信息失败");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            String code = JSONObject.parseObject(response).getString("code");
                                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                                LogUtils.d("修改角色信息");
                                            }
                                        }
                                    });

                        }
                        break;
                }
            }
        };

        mStandardDialog = new StandardDialog(mContext);
        mStandardDialog.setStandardTitle("验证成功！");
        mStandardDialog.setStandardMsg("您也可以选择开启免密功能，在下次使用马甲券时便无需再次上传安全码，让使用更加方便快捷，是否开通免密功能？");
        mStandardDialog.setCancelText("暂不需要");
        mStandardDialog.setSureText("立即开通");
        //开通免密
        mStandardDialog.setCallback(new com.shanchain.data.common.base.Callback() {
            @Override
            public void invoke() {
                String userId = SCCacheUtils.getCacheUserId();
                String passwordCode = getAuthCode(file);
                SCCacheUtils.setCache(userId, Constants.CACHE_AUTH_CODE, passwordCode);
                SCCacheUtils.setCache(userId, Constants.TEMPORARY_CODE, passwordCode);
                Message message = new Message();
                message.what = 1;
                message.obj = true;
                ActivityStackManager.getInstance().getTopActivity();
                freePasswordHandler.sendMessage(message);
            }
        }, new com.shanchain.data.common.base.Callback() {//不开启免密
            @Override
            public void invoke() {
                Message message = new Message();
                message.what = 1;
                message.obj = false;
                freePasswordHandler.sendMessage(message);
                authCode = getAuthCode(file);
                String userId = SCCacheUtils.getCacheUserId();
                SCCacheUtils.setCache(userId, Constants.CACHE_AUTH_CODE, "");
                SCCacheUtils.setCache(userId, Constants.TEMPORARY_CODE, authCode);
//                EventBus eventBus = new EventBus();
//                EventBusObject<String> eventBusObject = new EventBusObject<String>(EventConstant.EVENT_RELEASE, authCode);
//                eventBus.post(eventBusObject);
            }
        });
        mStandardDialog.show();
    }

    protected String getAuthCode(File file) {
        //创建requestBody,获取密码凭证
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        RequestBody fileBody = MultipartBody.create(MEDIA_TYPE, file);
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("deviceToken", "" + registrationId)
                .setType(MultipartBody.FORM);
        RequestBody multiBody = multiBuilder.build();
        SCHttpUtils.postByBody(HttpApi.WALLET_BIND_PHONE_IMEI + SCCacheUtils.getCacheToken(), multiBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(mContext, "网络异常");

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final String code = SCJsonUtils.parseCode(result);
                final String msg = SCJsonUtils.parseMsg(result);
                if (NetErrCode.COMMON_SUC_CODE.equals(code) || NetErrCode.SUC_CODE.equals(code)) {
                    String data = SCJsonUtils.parseData(result);
                    authCode = data;
                } else {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtils.showToast(mContext, code + ":" + msg);
                        }
                    });
                }
            }
        });
        return authCode;
    }

    /* ===================================================================
     *                        app全局变量封装方法
     * ===================================================================
     */
    protected String getRegistrationId() {
        return registrationId;
    }

    protected void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    protected boolean isBindPwd() {
        return isBindPwd;
    }

    protected void setBindPwd(boolean bindPwd) {
        isBindPwd = bindPwd;
    }

    protected boolean isAllowNotify() {
        return allowNotify;
    }

    protected void setAllowNotify(boolean allowNotify) {
        this.allowNotify = allowNotify;
    }

    protected void setRealName(boolean realName) {
        isRealName = realName;
    }

    protected boolean getRealName() {
        return isRealName;
    }

    /**
     * 描述：打开相册
     */
    protected void selectImage(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, NetErrCode.WALLET_PHOTO);
        }
    }

    /**
     * 描述: 重写finish使Activity出栈
     */
    @Override
    public void finish() {
        // 调用父类方法
        super.finish();
        // Activity出栈
        ActivityManager.getInstance().removeActivity(this);
        ActivityStackManager.getInstance().finishActivity(this);
    }

    /**
     * 描述: 重写onDestroy实现反注册EventBus
     */
    @Override
    protected void onDestroy() {
        // 调用父类方法
        super.onDestroy();
        // 解除注解绑定
        ButterKnife.unbind(this);
        // 反注册EventBus
        EventBus.getDefault().unregister(this);
        // 解除网络状态监听器
        OkHttpUtils.getInstance().cancelTag(this);
        //极光消息解绑
//        JMessageClient.unRegisterEventReceiver(this);
    }

    /**
     * 描述：友盟数据统计埋点
     */
    public void onResume() {
        super.onResume();
        //  MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();

        //   MobclickAgent.onPageEnd(this.getClass().getSimpleName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }


    /**
     * 描述: 获取Bundle数据
     *
     * @param extras
     */
    public void getBundleExtras(Bundle extras) {

    }


    /* ===================================================================
     *                        父类抽象方法需实现
     * ===================================================================
     */


    /**
     * 描述: 绑定布局资源文件
     *
     * @return layout ID
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 描述: 初始化View和Event
     */
    protected abstract void initViewsAndEvents();


    /* ===================================================================
     *                        Activity跳转方法
     * ===================================================================
     */

    /**
     * 描述: Activity跳转
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 描述: Activity跳转 传参
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 描述: Activity跳转 关闭当前Activity
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * 描述: Activity跳转 传参且关闭当前Activity
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 描述: Activity跳转 回调
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 描述: Activity跳转 传参且回调
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    private void initDeviceToken() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                setDeviceToken();
            }
        });
        thread.start();
    }

    private void checkApkVersion() {
        final String localVersion = VersionUtils.getVersionName(mContext);
        SCHttpUtils.postNoToken()
                .url(HttpApi.OSS_APK_GET_LASTEST)
                .addParams("type", "android")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取版本信息失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PackageManager packageManager = getApplicationContext().getPackageManager();
                        String packagerName = getApplicationContext().getPackageName();
                        try {
                            //获取当前版本号
                            String versionCode = packageManager.getPackageInfo(packagerName, 0).versionName;
//                            ToastUtils.showToast(HomeActivity.this, versionCode);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        try {
                            LogUtils.i("获取到版本信息 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = SCJsonUtils.parseData(response);
                                String forceUpdate = JSONObject.parseObject(data).getString("forceUpdate");
                                boolean force = Boolean.parseBoolean(forceUpdate);
                                String url = JSONObject.parseObject(data).getString("url");
                                String version = JSONObject.parseObject(data).getString("version");
                                boolean isUpdata = VersionUtils.compareVersion(localVersion, version);
                                if (isUpdata) {
                                    showUpdateDialog(url, force, version);
                                } else {
                                    return;
                                }

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //版本更新提示弹窗
    private void showUpdateDialog(final String url, final boolean force, String version) {
        String msg = "";
        if (force) {
            msg = "新版本有较大改进，马上更新吧";
        } else {
            msg = "确定要更新吗？";
        }
        final StandardDialog dialog = new StandardDialog(this);
        dialog.setStandardTitle("发现新版本 (" + version + ")");
        dialog.setStandardMsg(msg);
        dialog.setSureText("确定");
        dialog.setCancelText("取消");
        dialog.setCallback(new com.shanchain.data.common.base.Callback() {
            @Override
            public void invoke() {  //确定
                downLoadApk(url);
            }
        }, new com.shanchain.data.common.base.Callback() {
            @Override
            public void invoke() {  //取消
                if (force) {
                    dialog.dismiss();
                    ActivityStackManager.getInstance().finishAllActivity();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.setCancelable(!force);
        dialog.setCanceledOnTouchOutside(!force);
    }

    //下载APK版本
    private void downLoadApk(String url) {
        DownloadManager manager;
//        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        /*DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        query.setFilterByStatus(DownloadManager.STATUS_RUNNING);//正在下载
        Cursor c = manager.query(query);
        if (c.moveToNext()) {
        } else {
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            down.setVisibleInDownloadsUi(true);
            down.setTitle("马甲App");
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//            //down.setDestinationInExternalFilesDir(this, null, "arkspot-release.apk");
//            String filePath = getCacheDir().getAbsoluteFile() + "arkspot-release.apk";
//            File apkFile = new File(filePath);
//            Uri.withAppendedPath(Uri.fromFile(getCacheDir()),"arkspot-release.apk");
            down.setDestinationUri(Uri.withAppendedPath(Uri.fromFile(getExternalCacheDir()), "arkspot-release.apk"));
            downloadId = manager.enqueue(down);
        }*/

        UpdateAppBean updateAppBean = new UpdateAppBean();
        //设置 apk 的下载地址
        updateAppBean.setApkFileUrl(url);
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            try {
                path = getExternalCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            }
        } else {
            path = getCacheDir().getAbsolutePath();
        }

        //设置apk 的保存路径
        updateAppBean.setTargetPath(path);
        //实现网络接口，只实现下载就可以
        updateAppBean.setHttpManager(new UpdateAppHttpUtil());

        UpdateAppManager.download(this, updateAppBean, new DownloadService.DownloadCallback() {
            @Override
            public void onStart() {
//                HProgressDialogUtils.showHorizontalProgressDialog(JavaActivity.this, "下载进度", false);
                Log.d(TAG, "onStart() called");
            }

            @Override
            public void onProgress(float progress, long totalSize) {
//                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                Log.d(TAG, "onProgress() called with: progress = [" + progress + "], totalSize = [" + totalSize + "]");

            }

            @Override
            public void setMax(long totalSize) {
                Log.d(TAG, "setMax() called with: totalSize = [" + totalSize + "]");
            }

            @Override
            public boolean onFinish(File file) {
//                HProgressDialogUtils.cancel();
                Log.d(TAG, "onFinish() called with: file = [" + file.getAbsolutePath() + "]");
                return true;
            }

            @Override
            public void onError(String msg) {
//                HProgressDialogUtils.cancel();
                Log.e(TAG, "onError() called with: msg = [" + msg + "]");
            }

            @Override
            public boolean onInstallAppAndAppOnForeground(File file) {
                Log.d(TAG, "onInstallAppAndAppOnForeground() called with: file = [" + file + "]");
                return false;
            }
        });

    }

    private void setDeviceToken() {
        final String userId = SCCacheUtils.getCacheUserId();

        if (!TextUtils.isEmpty(CommonCacheHelper.getInstance().getCache(userId, CACHE_DEVICE_TOKEN_STATUS)) && CommonCacheHelper.getInstance().getCache(userId, CACHE_DEVICE_TOKEN_STATUS).equalsIgnoreCase("true")) {
            return;
        }
        String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return;
        }
        if (!TextUtils.isEmpty(PrefUtils.getString(AppManager.getInstance().getContext(), SP_KEY_DEVICE_TOKEN, ""))) {
            SCHttpUtils.postWithUserId()
                    .url(HttpApi.SET_DEVICE_TOKEN)
                    .addParams("osType", "android")
                    .addParams("token", token)
                    .addParams("deviceToken", PrefUtils.getString(AppManager.getInstance().getContext(), SP_KEY_DEVICE_TOKEN, ""))
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.i("设置DeviceToken失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                CommonCacheHelper.getInstance().setCache(userId, CACHE_DEVICE_TOKEN_STATUS, "true");
                            }

                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            if (requestCode == NetErrCode.WALLET_PHOTO) {

                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //获取照片路径
                String photoPath = cursor.getString(columnIndex);
//            ToastUtils.showToastLong(mBusContext, "选择的图片途径：" + photoPath);
                cursor.close();
                final Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                mPasswordFile = new File(photoPath);
                if (showPasswordDialog != null && showPasswordDialog.getContext() != null) {
                    showPasswordDialog.dismiss();
                }
                mNewCustomDialog = new CustomDialog(mActivity, true, 1.0, com.shanchain.common.R.layout.dialog_bottom_wallet_password, new int[]{com.shanchain.common.R.id.iv_dialog_add_picture, com.shanchain.common.R.id.tv_dialog_sure});
                mNewCustomDialog.setPasswordBitmap(bitmap);
                mNewCustomDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(CustomDialog dialog, View view) {
                        if (view.getId() == com.shanchain.common.R.id.iv_dialog_add_picture) {
                            selectImage(mActivity);
                        } else if (view.getId() == com.shanchain.common.R.id.tv_dialog_sure) {
                            if (mPasswordFile != null) {
                                String userId = SCCacheUtils.getCacheUserId();
                                String passwordCode = getAuthCode(mPasswordFile);
                                SCCacheUtils.setCache(userId, Constants.CACHE_AUTH_CODE, passwordCode);
                                checkPassword(mPasswordFile);
                                EventBus eventBus = new EventBus();
                                eventBus.post(new EventBusObject<String>(EventConstant.EVENT_RELEASE));
                                if (showPasswordDialog != null && showPasswordDialog.getContext() != null) {
                                    showPasswordDialog.dismiss();
                                }
                            }
                        }
                    }
                });
                mNewCustomDialog.show();

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeLoadingDialog();
        closeProgress();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }

    protected void showLoadingDialog(boolean cancelable) {
        mCustomDialog = new CustomDialog(this, 0.4, R.layout.common_dialog_progress, null);
        mCustomDialog.show();
        mCustomDialog.setCancelable(cancelable);
    }

    protected void showLoadingDialog() {
        mCustomDialog = new CustomDialog(this, 0.4, R.layout.common_dialog_progress, null);
        mCustomDialog.show();
        mCustomDialog.setCancelable(false);
    }

    protected void closeLoadingDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.dismiss();
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    protected void getChatRoomInfo(LatLng latLng) {
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", latLng.longitude + "")
                .addParams("latitude", latLng.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### GET_CHAT_ROOM_INFO 请求失败 #######");
                        closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        closeLoadingDialog();
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("####### " + "获取聊天室信息" + " ########");
                            String data = JSONObject.parseObject(response).getString("data");
                            coordinates = JSONObject.parseObject(data, Coordinates.class);
                            //房间roomId
                            RoleManager.switchRoleCacheRoomId("" + coordinates.getRoomId());
                        }
                    }
                });
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            myLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            HomeActivity.latLng = myLatLng;
            getChatRoomInfo(myLatLng);
        }
    }
}
