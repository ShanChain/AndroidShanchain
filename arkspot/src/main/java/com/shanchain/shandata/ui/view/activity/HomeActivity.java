package com.shanchain.shandata.ui.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.net.UpdateAppHttpUtil;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.RedPaperDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.ImageUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.data.common.utils.VersionUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.push.ExampleUtil;
import com.shanchain.shandata.receiver.MyReceiver;
import com.shanchain.shandata.ui.model.Coordinates;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.model.RedPaper;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.utils.CoordinateTransformUtil;
import com.shanchain.shandata.utils.MyOrientationListener;
import com.shanchain.shandata.utils.PermissionHelper;
import com.shanchain.shandata.utils.PermissionInterface;
import com.shanchain.shandata.utils.RequestCode;
import com.tinkerpatch.sdk.TinkerPatch;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.service.DownloadService;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;
import cn.jiguang.imui.model.MyMessage;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.android.utils.Logger;
import cn.jiguang.share.facebook.Facebook;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;
import cn.jiguang.share.weibo.SinaWeibo;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static com.shanchain.data.common.base.Constants.CACHE_DEVICE_TOKEN_STATUS;
import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;
import static com.shanchain.data.common.base.Constants.SP_KEY_DEVICE_TOKEN;

//import me.shaohui.shareutil.ShareUtil;
//import me.shaohui.shareutil.share.ShareListener;
//import me.shaohui.shareutil.share.SharePlatform;

public class HomeActivity extends BaseActivity implements PermissionInterface {

    public static BaiduMap baiduMap;
    private long downloadId;
    public LocationClient locationClient;
    public static BDLocationListener bdLocationListener;
    private PermissionHelper mPermissionHelper;
    private MapStatusUpdate mapstatusupdate;
    private LatLng markerPoint;
    private double[] WGS84point;
    private Runnable runnable;
    private Handler handler, getRoomIdHandle, activityHandler, redPaperHandle, shareChatRoomHandle;
    private BDLocation myLocation;
    private double[] gcj02point;
    private UiSettings uiSettings;
    private MyOrientationListener myOrientationListener;
    public static Coordinates coordinates;
    private List pointList = new ArrayList();
    private List<Coordinates> coordinatesList;
    private List roomList = new ArrayList();
    private boolean isFirstLoc = true; // 是否首次定位
    private boolean isHide = true; //是否隐藏
    private String roomID = "", clickRoomID = "";
    private String roomName;
    private int joinRoomId = 0, page = 0, pageSize = 10;
    private ProgressDialog mDialog;
    private CustomDialog ruleDialog;
    private LatLng myFocusPoint;
    private boolean isIn, isFirstShow = false;
    private StandardDialog standardDialog;
    public static boolean isForeground = false;
    public static boolean isShowRush = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.shanchain.shandata.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public String percentage;
    private Handler shareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            closeLoadingDialog();
            closeProgress();
            String toastMsg = (String) msg.obj;
            Toast.makeText(HomeActivity.this, toastMsg, Toast.LENGTH_SHORT).show();

        }
    };
    private PlatActionListener mPlatActionListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            if (shareHandler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享成功";
                shareHandler.sendMessage(message);
            }
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            if (shareHandler != null) {
                Message message = shareHandler.obtainMessage();
                message.obj = "分享失败:" + (error != null ? error.getMessage() : "") + "---" + errorCode;
                Logger.dd(TAG, message.obj + "");
                shareHandler.sendMessage(message);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (shareHandler != null) {
                Message message = shareHandler.obtainMessage();
                message.obj = "分享取消";
                shareHandler.sendMessage(message);
            }
        }
    };
    private PlatActionListener shareListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            closeLoadingDialog();
            closeProgress();
            if (shareHandler != null) {
                Message message = shareHandler.obtainMessage();
                message.obj = "分享成功";
                shareHandler.sendMessage(message);
            }
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            closeLoadingDialog();
            closeProgress();
            if (shareHandler != null) {
                Message message = shareHandler.obtainMessage();
                message.obj = "分享失败:" + (error != null ? error.getMessage() : "") + "---" + errorCode;
                Logger.d("shareError", message.obj + "");
                shareHandler.sendMessage(message);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            closeLoadingDialog();
            closeProgress();
            if (shareHandler != null) {
                Message message = shareHandler.obtainMessage();
                message.obj = "分享取消";
                shareHandler.sendMessage(message);
            }
        }
    };
    ;
    TextView tvLocation, tvCountDown, tvSecond;
    ImageView imgInfo;
    ImageView imgHistory;
    MapView mapView;
    RelativeLayout relativeCountDown, relativeRush;
    LinearLayout btJoin;
    LinearLayout linearRule, linearHot, linearReset;
    ImageView hideImageView;

    public static LatLng latLng;
    private CoordinateConverter coordinateConverter;
    private LatLng gpsLatLng;
    private LatLng bd09LatLng;
    private View.OnClickListener onClickListener;
    BaiduMap.OnMapClickListener MapListener;
    private File imgeFile;
    private Bitmap cropScreenBitmap;
    private List<RedPaper> redPaperList;
    private String ruleDescribe;
    private String startTme;
    private String endTime;
    private TextView totalMoney;
    private TextView addMoney;
    private TextView checkPoint;
    private TextView checkPointNum;
    private TextView tvTimeDown;
    private ShareParams shareParams, redPaperParams;
    private Handler levelHandle;
    private CustomDialog shareBottomDialog, shareChatRoomDialog;
    private RedPaperDialog redPaperDialog;
    private File captureScreenFile;
    private String clearance;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //每次进入主界面自动查询是否有补丁文件更新
        TinkerPatch.with().fetchPatchUpdate(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }
//        DownloadCompleteReceiver completeReceiver = new DownloadCompleteReceiver();
    }

    @Override
    protected void initViewsAndEvents() {
        tvLocation = findViewById(R.id.text_view_location);
        tvCountDown = findViewById(R.id.tv_count_down);//倒计时
        tvSecond = findViewById(R.id.second_day);//显示天/秒
        totalMoney = findViewById(R.id.tv_total_money);//获取的全部金额
        addMoney = findViewById(R.id.tv_add_money);//每次点击的奖励
        checkPoint = findViewById(R.id.tv_check_point);//关卡
        checkPointNum = findViewById(R.id.tv_check_point_num);//点亮区块书数量
        tvTimeDown = findViewById(R.id.tv_time_down);//点亮区块书数量
        relativeCountDown = findViewById(R.id.relative_count_down);//显示活动倒计时
        relativeRush = findViewById(R.id.relative_rush);//显示活动信息
        mapView = findViewById(R.id.map_view_home);
        btJoin = findViewById(R.id.button_join);
        imgHistory = findViewById(R.id.image_view_history);
        imgInfo = findViewById(R.id.image_view_info);
        linearReset = findViewById(R.id.linear_reset_location);
        linearHot = findViewById(R.id.linear_hot);
        linearRule = findViewById(R.id.linear_rule); //活动规则
        hideImageView = findViewById(R.id.img_view_hide);//收起隐藏按钮
        String uId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
        String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
        String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);

        RNGDataBean rngDataBean = new RNGDataBean();
        rngDataBean.setUserId(uId);
        rngDataBean.setToken(token);
        rngDataBean.setSpaceId(spaceId);
        rngDataBean.setCharacterId(characterId);
        String jsonGData = JSON.toJSONString(rngDataBean);
        SCCacheUtils.setCache(uId, Constants.CACHE_GDATA, jsonGData);
        String cacheGData = SCCacheUtils.getCache(uId, Constants.CACHE_GDATA);
        LogUtils.i("缓存的gdata = " + cacheGData);

        getRoomIdHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        joinRoomId = msg.arg1;
//                        if (roomID.equals(clickRoomID)) {
//                            isIn = true;
//                        } else {
//                            isIn = false;
//                        }
                        roomID = String.valueOf(joinRoomId);
                        roomName = (String) msg.obj;
                        btJoin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this, MessageListActivity.class);
                                intent.putExtra("roomId", roomID);
                                intent.putExtra("roomName", roomName);
//                                intent.putExtra("isInCharRoom", isIn);
                                startActivity(intent);
                            }
                        });
                        break;
                    case 2:
                        joinRoomId = msg.arg1;
                        roomID = String.valueOf(joinRoomId);
                        roomName = (String) msg.obj;
                        btJoin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this, MessageListActivity.class);
                                intent.putExtra("roomId", roomID);
                                intent.putExtra("roomName", roomName);
                                startActivity(intent);
                            }
                        });
                        break;
                }
            }
        }
        ;

        //        UMConfigure.setLogEnabled(true); //显示友盟log日记
        //检查apk版本
        checkApkVersion();

        initDeviceToken();

        initView();
        //注册自定义消息广播
        registerMessageReceiver();

        initBaiduMap();

    }

    //Activity跨年活动
    private void transYear(final LatLng point) {
        /*
         * 通用分享
         * */
        //分享回调
        levelHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    final String shareType = (String) msg.obj;
                    SCHttpUtils.postWithUserId()
                            .url(HttpApi.SHARE_COMMON)
                            .addParams("type", shareType + "")
                            .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                            .build()
                            .execute(new SCHttpStringCallBack() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    LogUtils.d("网络异常");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    String code = JSONObject.parseObject(response).getString("code");
                                    if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                                        String data = JSONObject.parseObject(response).getString("data");
                                        percentage = JSONObject.parseObject(data).getString("percentage");
                                        final String shareType = JSONObject.parseObject(data).getString("shareType");
                                        final String background = JSONObject.parseObject(data).getString("background");
                                        final String url = JSONObject.parseObject(data).getString("url");
                                        final String title = JSONObject.parseObject(data).getString("title");
                                        final String intro = JSONObject.parseObject(data).getString("intro");
                                        redPaperParams = new ShareParams();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                shareBottomDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                                                    @Override
                                                    public void OnItemClick(CustomDialog dialog, View view) {
                                                        switch (view.getId()) {
                                                            case R.id.mRlWechat:
                                                                if (shareType.equals("SHARE_IMAGE")) {
                                                                    showLoadingDialog(false);
                                                                    redPaperParams.setShareType(Platform.SHARE_IMAGE);
                                                                    ThreadUtils.runOnSubThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Bitmap bitmap = ImageUtils.returnBitMap(url);
                                                                            String absolutePath = ImageUtils.saveUrlImgFile(
                                                                                    bitmap, "share.png").getAbsolutePath();
                                                                            redPaperParams.setImagePath(absolutePath);
                                                                            //调用分享接口share ，分享到微信平台。
                                                                            JShareInterface.share(Wechat.Name, redPaperParams, shareListener);
//                                                                            closeProgress();
                                                                            closeLoadingDialog();
                                                                        }
                                                                    });
//                                                                    String absolutePath = ImageUtils.saveUrlImgFile(
//                                                                            ImageUtils.drawableToBitmap(
//                                                                                    getResources().getDrawable(R.mipmap.piker)), "share.png").getAbsolutePath();
//                                                                    redPaperParams.setImagePath(absolutePath);
//                                                                    shareParams.setTitle(intro);
                                                                } else {
                                                                    shareBottomDialog.setShareBitmap(null);
                                                                    shareBottomDialog.setPercentage(percentage + "");
                                                                    redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                    redPaperParams.setTitle(title);
                                                                    redPaperParams.setText(intro);
                                                                    redPaperParams.setImageUrl(background);
                                                                    redPaperParams.setUrl(url);
                                                                    //调用分享接口share ，分享到微信平台。
                                                                    JShareInterface.share(Wechat.Name, redPaperParams, shareListener);
//                                                                    closeProgress();
                                                                    closeLoadingDialog();
                                                                }
                                                                break;
                                                            case R.id.mRlWeixinCircle:
                                                                if (shareType.equals("SHARE_IMAGE")) {
                                                                    showLoadingDialog(false);
                                                                    redPaperParams.setShareType(Platform.SHARE_IMAGE);
                                                                    ThreadUtils.runOnSubThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Bitmap bitmap = ImageUtils.returnBitMap(url);
                                                                            String absolutePath = ImageUtils.saveUrlImgFile(
                                                                                    bitmap, "share.png").getAbsolutePath();
                                                                            redPaperParams.setImagePath(absolutePath);
                                                                            //调用分享接口share ，分享到QQ平台。
                                                                            JShareInterface.share(Wechat.Name, redPaperParams, shareListener);
//                                                                            closeProgress();
                                                                            closeLoadingDialog();
                                                                        }
                                                                    });
                                                                    /*String absolutePath = ImageUtils.saveUrlImgFile(
                                                                            ImageUtils.drawableToBitmap(
                                                                                    getResources().getDrawable(R.mipmap.piker)), "share.png").getAbsolutePath();
                                                                    redPaperParams.setImagePath(absolutePath);*/
//                                                                    redPaperParams.setTitle(intro);
                                                                } else {
                                                                    shareBottomDialog.setShareBitmap(null);
                                                                    shareBottomDialog.setPercentage(percentage + "");
                                                                    redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                    redPaperParams.setTitle(title);
                                                                    redPaperParams.setText(intro);
                                                                    redPaperParams.setImageUrl(background);
                                                                    redPaperParams.setUrl(url);
                                                                    //调用分享接口share ，分享到朋友圈平台。
                                                                    JShareInterface.share(WechatMoments.Name, redPaperParams, shareListener);
//                                                                    closeProgress();
                                                                    closeLoadingDialog();
                                                                }
                                                                break;
                                                            case R.id.mRlQQ:
                                                                if (shareType.equals("SHARE_IMAGE")) {
                                                                    showLoadingDialog(false);
                                                                    redPaperParams.setShareType(Platform.SHARE_IMAGE);
                                                                    ThreadUtils.runOnSubThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Bitmap bitmap = ImageUtils.returnBitMap(url);
                                                                            String absolutePath = ImageUtils.saveUrlImgFile(
                                                                                    bitmap, "share.png").getAbsolutePath();
                                                                            redPaperParams.setImagePath(absolutePath);
                                                                            //调用分享接口share ，分享到QQ平台。
                                                                            JShareInterface.share(QQ.Name, redPaperParams, shareListener);
//                                                                            closeProgress();
                                                                            closeLoadingDialog();
                                                                        }
                                                                    });
                                                                    /*Bitmap imgBitmap = ImageUtils.returnBitMap(url);
                                                                    String absolutePath = ImageUtils.saveUrlImgFile(
                                                                            imgBitmap, "share.png").getAbsolutePath();
                                                                    redPaperParams.setImagePath(absolutePath);*/
//                                                                    redPaperParams.setTitle(intro);
                                                                } else {
                                                                    shareBottomDialog.setShareBitmap(null);
                                                                    shareBottomDialog.setPercentage(percentage + "");
                                                                    redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                    redPaperParams.setTitle(title);
                                                                    redPaperParams.setText(intro);
                                                                    redPaperParams.setImageUrl(background);
                                                                    redPaperParams.setUrl(url);
                                                                    //调用分享接口share ，分享到QQ平台。
                                                                    JShareInterface.share(QQ.Name, redPaperParams, shareListener);
//                                                                    closeProgress();
                                                                    closeLoadingDialog();
                                                                }
                                                                break;
                                                            case R.id.mRlWeibo:
                                                                if (shareType.equals("SHARE_IMAGE")) {
                                                                    showLoadingDialog(false);
                                                                    redPaperParams.setShareType(Platform.SHARE_IMAGE);
                                                                    ThreadUtils.runOnSubThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Bitmap bitmap = ImageUtils.returnBitMap(url);
                                                                            String absolutePath = ImageUtils.saveUrlImgFile(
                                                                                    bitmap, "share.png").getAbsolutePath();
                                                                            redPaperParams.setImagePath(absolutePath);
                                                                            //调用分享接口share ，分享到QQ平台。
                                                                            JShareInterface.share(SinaWeibo.Name, redPaperParams, shareListener);
//                                                                            closeProgress();
                                                                            closeLoadingDialog();
                                                                        }
                                                                    });
                                                                   /* String absolutePath = ImageUtils.saveUrlImgFile(
                                                                            ImageUtils.drawableToBitmap(
                                                                                    getResources().getDrawable(R.mipmap.piker)), "share.png").getAbsolutePath();
                                                                    redPaperParams.setImagePath(absolutePath);*/
//                                                                    redPaperParams.setTitle(intro);
                                                                } else {
                                                                    shareBottomDialog.setShareBitmap(null);
                                                                    shareBottomDialog.setPercentage(percentage + "");
                                                                    redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                    redPaperParams.setTitle(title);
                                                                    redPaperParams.setText(intro);
                                                                    redPaperParams.setImageUrl(background);
                                                                    redPaperParams.setUrl(url);
                                                                    //调用分享接口share ，分享到QQ平台。
                                                                    JShareInterface.share(SinaWeibo.Name, redPaperParams, shareListener);
//                                                                    closeProgress();
                                                                    closeLoadingDialog();
                                                                }
                                                                break;
                                                            case R.id.share_close:
                                                                if (System.currentTimeMillis() <= Long.valueOf(endTime)) {
                                                                    lightCube(point);
                                                                }
                                                                shareBottomDialog.dismiss();
                                                                break;
                                                        }
                                                    }
                                                });
                                            }
                                        });

                                    }
                                }
                            });
                    if (System.currentTimeMillis() <= Long.valueOf(endTime)) {
                        lightCube(point);
                    }
                }
            }
        };
    }

    private void lightCube(LatLng point) {
        SCHttpUtils.get()
                .url(HttpApi.LIGHT_RUSH)
                .addParams("token", SCCacheUtils.getCacheToken())
                .addParams("currentUserId", SCCacheUtils.getCacheUserId())
                .addParams("currentCharaterId", SCCacheUtils.getCacheCharacterId())
                .addParams("latitude", point.latitude + "")
                .addParams("longitude", point.longitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String rushActivityVo = JSONObject.parseObject(data).getString("rushActivityVo");
                            clearance = JSONObject.parseObject(rushActivityVo).getString("clearance");//是否通关
                            String level = JSONObject.parseObject(rushActivityVo).getString("level");
                            String activityEndTime = JSONObject.parseObject(rushActivityVo).getString("endTime");
                            String presentTime = JSONObject.parseObject(rushActivityVo).getString("presentTime");
                            String surplusCount = JSONObject.parseObject(rushActivityVo).getString("surplusCount");
                            String totalAmount = JSONObject.parseObject(rushActivityVo).getString("totalAmount");
                            String reward = JSONObject.parseObject(rushActivityVo).getString("reward");

                            addMoney.setText(TextUtils.isEmpty(reward) ? "+ 0.0" : "+ " + reward);
                            totalMoney.setText("￥ " + totalAmount + "");
                            checkPointNum.setText(surplusCount + "");
                            checkPoint.setText("第 " + level + " 关");
                            final DecimalFormat df = new DecimalFormat("00");
                            if (endTime == null) {
                                return;
                            }
                            standardDialog = new StandardDialog(HomeActivity.this);
                            standardDialog.setStandardTitle("点亮活动已结束");
                            standardDialog.setStandardMsg("快去【我的钱包】中看看你赢得了多少奖励");
                            CountDownTimer countDownTimer = new CountDownTimer(Long.valueOf(endTime) - System.currentTimeMillis(), 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    long millisSecond = millisUntilFinished / 1000;
                                    long day = millisSecond / (60 * 60 * 24);//以天为单位，显示倒计时
//                                    long hour = (millisSecond / (60 * 60) - day * 24);（当以天为计时单位时，小时数以24小时显示，减去day*24小时）
                                    long hour = (millisSecond / (60 * 60));//以小时的单位显示倒计时
                                    long min = ((millisSecond / 60) - day * 24 * 60 - hour * 60);
                                    long s = (millisSecond - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                                    tvTimeDown.setText(df.format(hour) + ":" + df.format(min) + ":" + df.format(s));
                                }

                                @Override
                                public void onFinish() {
                                    tvTimeDown.setText("00:00:00");
                                    relativeCountDown.setVisibility(View.GONE);
                                    relativeRush.setVisibility(View.GONE);
//                                    ToastUtils.showToastLong(HomeActivity.this,"点亮活动已结束，");

//                                    if (isFirstShow == true) {
                                    standardDialog.show();
//                                    }
                                }
                            };
                            if (System.currentTimeMillis() <= Long.valueOf(endTime)) {
                                countDownTimer.start();
                                if (clearance.equals("false")) {
                                    if (surplusCount.equals("0")) {
                                        countDownTimer.cancel();
                                        String shareType = "SHARE_WEBPAGE";
                                        shareBottomDialog = new CustomDialog(HomeActivity.this,
                                                true, true, 1.0,
                                                R.layout.layout_bottom_share, new int[]{R.id.share_image,
                                                R.id.mRlWechat, R.id.mRlWeixinCircle, R.id.mRlQQ, R.id.mRlWeibo, R.id.share_close});
                                        shareBottomDialog.setShareBitmap(ImageUtils.drawableToBitmap(getResources().getDrawable(R.mipmap.red_package)), true);
//                                shareBottomDialog.setShow(false);
                                        if (level.equals("4")) {
//                                        shareBottomDialog.setDrawableId(R.mipmap.pig);
                                            shareBottomDialog.setShareBitmap(ImageUtils.drawableToBitmap(getResources().getDrawable(R.mipmap.pig)), false);
                                            shareType = "SHARE_IMAGE";
                                        }
                                        shareBottomDialog.show();
                                        Message message = new Message();
                                        message.what = 1;
                                        message.obj = shareType;
                                        levelHandle.sendMessage(message);
                                    }
                                    if (clearance.equals("true")) {
                                        relativeCountDown.setVisibility(View.GONE);
                                        relativeRush.setVisibility(View.GONE);
                                    }
                                } else {
                                    countDownTimer.cancel();
                                    countDownTimer.onFinish();
                                }
                            }
                        }
                    }
                });
    }


    //点击请求方区
    public void initCubeMap(LatLng point) {
        //获取聊天室信息
        final ArrayList<LatLng> clickPointList = new ArrayList<>();
        showProgress();
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", point.longitude + "")
                .addParams("latitude", point.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### GET_CHAT_ROOM_INFO 请求失败 #######");
                        closeProgress();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("####### " + "获取聊天室信息" + " ########");
                            String data = JSONObject.parseObject(response).getString("data");
                            coordinates = JSONObject.parseObject(data, Coordinates.class);
//                            clickRoomID = coordinates.getRoomId();
                            roomID = coordinates.getRoomId();
                            String latLang = coordinates.getRoomName();
                            String[] strings = latLang.split(",");
                            final double lat = Double.valueOf(strings[0]);
                            final double lang = Double.valueOf(strings[1]);
                            final String latLocation, langLocation;
                            DecimalFormat df = new DecimalFormat("#.00");
                            DecimalFormat enDf = new DecimalFormat("#.00");
//                            String sta = getResources().getConfiguration().locale.getLanguage();
                            String sta = MyApplication.systemLanguge;
                            if (lat < 0) {
                                latLocation = sta.equals("zh") ? "南纬" + df.format(lat) : enDf.format(lat) + "'S";

                            } else {
                                latLocation = sta.equals("zh") ? "北纬" + df.format(lat) : enDf.format(lat) + "'N";
                            }
                            if (lang > 0) {
                                langLocation = sta.equals("zh") ? "东经" + df.format(lang) : enDf.format(lang) + "'E";

                            } else {
                                langLocation = sta.equals("zh") ? "西经" + df.format(lang) : enDf.format(lang) + "'W";
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvLocation.setText(langLocation + " °," + latLocation + " °");
                                }
                            });

                            Message roomMessage = new Message();
                            roomMessage.what = 1;
                            roomMessage.obj = langLocation + "°," + latLocation + "°";
//                            roomMessage.arg1 = Integer.valueOf(clickRoomID);
                            try {
                                roomMessage.arg1 = Integer.valueOf(roomID);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            getRoomIdHandle.sendMessage(roomMessage);
                            /*
                             * 绘制所在位置的方区
                             *
                             * */
                            for (int i = 0; i < coordinates.getCoordinates().size(); i++) {
                                double pointLatitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLatitude());
                                double pointLongitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLongitude());
                                LatLng point = new LatLng(pointLatitude, pointLongitude);
                                // 将GPS设备采集的原始GPS坐标转换成百度坐标
                                coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                                coordinateConverter.coord(point);
                                LatLng desLatLng = coordinateConverter.convert();
//                                pointList.add(desLatLng);

                                clickPointList.add(point);
                            }

                            double focusLatitude = Double.parseDouble(coordinates.getFocusLatitude());
                            double focusLongitude = Double.parseDouble(coordinates.getFocusLongitude());
                            myFocusPoint = new LatLng(focusLatitude, focusLongitude);
                            // 将GPS设备采集的原始GPS坐标转换成百度坐标
                            coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                            coordinateConverter.coord(myFocusPoint);
                            LatLng desLatLng = coordinateConverter.convert();
                            //设置显示地图中心点
//                            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(desLatLng); //转换后的坐标
                            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(myFocusPoint); //未转换的坐标
                            baiduMap.setMapStatus(mapStatusUpdate);
                            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));//设置地图缩放级别
//                            baiduMap.setBaiduHeatMapEnabled(true);


                            //构建Marker图标
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.home_location);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(myFocusPoint)
                                    .icon(bitmap);
                            //在地图上添加Marker，并显示
//                           baiduMap.addOverlay(option);
                            //绘制虚线（需要多添加一个起点坐标，形成矩形）
                            clickPointList.add(clickPointList.get(0));
                            OverlayOptions ooPolyline = new PolylineOptions().width(4)
                                    .color(0xAA121518).points(clickPointList);
                            Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
                            mPolyline.setDottedLine(true);
                        }
                        closeProgress();
                    }

                });
    }

    //请求接口数据
    private void initData(LatLng gpsLatLng) {
//        LatLng myLatLang = new LatLng(20.045082, 110.32447);
        shareRedPaperDialog();
        //获取周边
        showProgress();
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_COORDINATE)
                .addParams("longitude", gpsLatLng.longitude + "")
                .addParams("latitude", gpsLatLng.latitude + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### USER_COORDINATE 请求失败 #######");
                        closeProgress();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        closeProgress();
                        LogUtils.d("####### USER_COORDINATE 请求成功 #######");
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            LogUtils.d("####### " + "获取聊天室信息" + " ########");
                            String data = JSONObject.parseObject(response).getString("data");
                            String room = JSONObject.parseObject(data).getString("room");
                            coordinatesList = JSONObject.parseArray(room, Coordinates.class);

                            /*
                             * 绘制附近的方区
                             *
                             * */
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < coordinatesList.size(); i++) {
                                        LatLng srcLatLang = new LatLng(Double.valueOf(coordinatesList.get(i).getFocusLatitude()), Double.valueOf(coordinatesList.get(i).getFocusLongitude()));
                                        LatLng focusPoint = coordinateConverter.from(CoordinateConverter.CoordType.GPS).coord(srcLatLang).convert();
                                        //构建Marker图标
                                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                                .fromResource(R.mipmap.home_location);
                                        //构建MarkerOption，用于在地图上添加Marker
                                        OverlayOptions option = new MarkerOptions()
                                                .position(srcLatLang)
                                                .icon(bitmap);
                                        //在地图上添加Marker，并显示
//                                baiduMap.addOverlay(option);
                                        for (int j = 0; j < coordinatesList.get(i).getCoordinates().size(); j++) {
                                            double pointLatitude = Double.parseDouble(coordinatesList.get(i).getCoordinates().get(j).getLatitude());
                                            double pointLongitude = Double.parseDouble(coordinatesList.get(i).getCoordinates().get(j).getLongitude());
                                            LatLng point = new LatLng(pointLatitude, pointLongitude);

                                            // 将GPS设备采集的原始GPS坐标转换成百度坐标
                                            coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                                            coordinateConverter.coord(point);
                                            LatLng desLatLng = coordinateConverter.convert();

//                                    roomList.add(desLatLng);
                                            roomList.add(point);//未转换的坐标


                                        }
                                        //
                                        double firstLatitude = Double.parseDouble(coordinatesList.get(i).getCoordinates().get(0).getLatitude());
                                        double firstLongitude = Double.parseDouble(coordinatesList.get(i).getCoordinates().get(0).getLongitude());
                                        LatLng indexPoint = new LatLng(firstLatitude, firstLongitude);
                                        // 将GPS设备采集的原始GPS坐标转换成百度坐标
                                        coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                                        coordinateConverter.coord(indexPoint);
                                        LatLng firstLatLng = coordinateConverter.convert();
//                                roomList.add(firstLatLng);
                                        //绘制虚线（需要多添加一个起点坐标，形成矩形）
                                        roomList.add(indexPoint);
                                        OverlayOptions roomOoPolyline = new PolylineOptions().width(4)
                                                .color(0xAA121518).points(roomList);
                                        Polyline roomPolyline = (Polyline) baiduMap.addOverlay(roomOoPolyline);
                                        roomPolyline.setDottedLine(true);
                                        roomList.clear();
                                    }
                                }
                            }).start();


                        }
                        closeProgress();
                    }
                });

        //获取聊天室信息
        SCHttpUtils.get()
                .url(HttpApi.CHAT_ROOM_INFO)
                .addParams("longitude", gpsLatLng.longitude + "")
                .addParams("latitude", gpsLatLng.latitude + "")
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
                            roomID = coordinates.getRoomId();
                            RoleManager.switchRoleCacheRoomId(roomID);
//                            roomID = "12826211";

                            /*
                             * 绘制所在位置的方区
                             *
                             * */
                            for (int i = 0; i < coordinates.getCoordinates().size(); i++) {
                                double pointLatitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLatitude());
                                double pointLongitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLongitude());
                                LatLng point = new LatLng(pointLatitude, pointLongitude);
                                // 将GPS设备采集的原始GPS坐标转换成百度坐标
                                coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                                coordinateConverter.coord(point);
                                LatLng desLatLng = coordinateConverter.convert();
//                                pointList.add(desLatLng);
                                pointList.add(point);
                            }
                            double focusLatitude = Double.parseDouble(coordinates.getFocusLatitude());
                            double focusLongitude = Double.parseDouble(coordinates.getFocusLongitude());
                            LatLng point = new LatLng(focusLatitude, focusLongitude);
                            // 将GPS设备采集的原始GPS坐标转换成百度坐标
                            coordinateConverter.from(CoordinateConverter.CoordType.GPS);
                            coordinateConverter.coord(point);
                            LatLng desLatLng = coordinateConverter.convert();
                            //设置显示地图中心点
//                            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(desLatLng); //转换后的坐标
                            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(point); //未转换的坐标
                            baiduMap.setMapStatus(mapStatusUpdate);

                            //构建Marker图标
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.map_maker_my);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap);
                            //在地图上添加Marker，并显示
//                                baiduMap.addOverlay(option);
                            //绘制虚线（需要多添加一个起点坐标，形成矩形）
                            pointList.add(pointList.get(0));
                            OverlayOptions ooPolyline = new PolylineOptions().width(4)
                                    .color(0xAA121518).points(pointList);
                            Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
                            mPolyline.setDottedLine(true);
                            String latLang = coordinates.getRoomName();
                            String[] strings = latLang.split(",");
                            final double lat = Double.valueOf(strings[0]);
                            final double lang = Double.valueOf(strings[1]);
                            final String latLocation, langLocation;
                            DecimalFormat df = new DecimalFormat("#.00");
                            if (lat < 0) {
                                latLocation = "南纬" + df.format(lat);

                            } else {
                                latLocation = "北纬" + df.format(lat);
                            }
                            if (lang > 0) {
                                langLocation = "东经" + df.format(lang);

                            } else {
                                langLocation = "西经" + df.format(lang);
                            }

                            roomName = langLocation + "°," + latLocation + "°";
                            Message roomMessage = new Message();
                            roomMessage.what = 2;
                            roomMessage.obj = roomName;
                            roomMessage.arg1 = Integer.valueOf(roomID);
                            getRoomIdHandle.sendMessage(roomMessage);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvLocation.setText(langLocation + "," + latLocation);
                                }
                            });
                        }
                        closeProgress();
                    }
                });

        /*点亮活动信息*/
        SCHttpUtils.get()
                .url(HttpApi.LIGHT_ACTIVE)
                .addParams("token", SCCacheUtils.getCacheToken() + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### GET_LIGHT_ACTIVE 请求失败 #######");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("####### GET_LIGHT_ACTIVE 请求成功 #######");
                        String code = JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data") != null ? JSONObject.parseObject(response).getString("data") : "暂无活动";
                            if (data.equals("暂无活动")) return;
                            ruleDescribe = JSONObject.parseObject(data).getString("ruleDescribe");
                            startTme = JSONObject.parseObject(data).getString("startTime");
                            endTime = JSONObject.parseObject(data).getString("endTime");
                            activityHandler.sendEmptyMessage(1);
                            if (System.currentTimeMillis() <= Long.valueOf(startTme)) {
                                relativeCountDown.setVisibility(View.VISIBLE);
                                relativeRush.setVisibility(View.GONE);
                                isShowRush = false;
                            }
                            if (System.currentTimeMillis() <= Long.valueOf(endTime) && System.currentTimeMillis() >= Long.valueOf(startTme)) {
                                relativeCountDown.setVisibility(View.GONE);
                                relativeRush.setVisibility(View.VISIBLE);
                            }
                            if (System.currentTimeMillis() > Long.valueOf(endTime)) {
                                relativeCountDown.setVisibility(View.GONE);
                                relativeRush.setVisibility(View.GONE);
                                linearRule.setVisibility(View.GONE);
                            }
                        }
                    }
                });
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
        dialog.setCallback(new Callback() {
            @Override
            public void invoke() {  //确定
                downLoadApk(url);
            }
        }, new Callback() {
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

    private void initView() {
        //跨年倒计时
        activityHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    Calendar startCalendar = Calendar.getInstance();
                    Calendar endCalendar = Calendar.getInstance();
                    final int mHour = startCalendar.get(Calendar.HOUR_OF_DAY);
                    final int minutes = startCalendar.get(Calendar.MINUTE);
                    final int second = startCalendar.get(Calendar.SECOND);

                    endCalendar.setTime(new Date(Long.valueOf(endTime)));//活动结束时间
                    startCalendar.setTime(new Date(Long.valueOf(startTme)));//活动开始时间
                    final int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
                    final int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
                    final long allCountDown = Long.valueOf(startTme) - System.currentTimeMillis();//(endDay - startDay) * 24 * 60 * 60 * 1000
//                    if (allCountDown> 0){
//
//                    }
                    CountDownTimer countDownTimer = new CountDownTimer(allCountDown, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            DecimalFormat df = new DecimalFormat("00");
                            long millisSecond = millisUntilFinished / 1000;
                            long day = millisSecond / (60 * 60 * 24);
                            long hour = (millisSecond / (60 * 60) - day * 24);
                            long min = ((millisSecond / 60) - day * 24 * 60 - hour * 60);
                            long s = (millisSecond - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                            if (allCountDown < 2 * 24 * 60 * 60 * 1000) {
                                long mHour = (millisSecond / (60 * 60));
                                long mMin = ((millisSecond / 60) - day * 24 * 60 - hour * 60);
                                long mS = (millisSecond - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//                                tvCountDown.setText(df.format(hour - mHour) + ":" + df.format(min - minutes) + ":" + df.format(s) + "");
                                tvCountDown.setText(df.format(mHour) + ":" + df.format(mMin) + ":" + df.format(mS) + "");
                                tvSecond.setText("秒");
                            } else {
                                tvCountDown.setText(day + "");
                            }
                        }

                        @Override
                        public void onFinish() {
                            tvCountDown.setText("活动结束");
                            relativeRush.setVisibility(View.GONE);
                            relativeCountDown.setVisibility(View.GONE);
                        }
                    };

//                    if (System.currentTimeMillis() <= Long.valueOf(startTme)) {
                    if (allCountDown > 0) {
                        countDownTimer.start();
                    } else {
                        countDownTimer.cancel();

                    }

                }
            }
        };

        //初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(this, this);
        mPermissionHelper.requestPermissions();
        linearRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ruleDialog = new CustomDialog(HomeActivity.this, false, 1.0, R.layout.common_dialog_costom, new int[]{R.id.btn_dialog_task_detail_sure});
                ruleDialog.setMessageContent(ruleDescribe);
                ruleDialog.show();
                ruleDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(CustomDialog dialog, View view) {
                        switch (view.getId()) {
                            case R.id.btn_dialog_task_detail_sure:
                                ruleDialog.dismiss();
                                break;
                        }
                    }
                });

            }
        });
        //通知栏
        setStyleCustom();
    }

    private void initBaiduMap() {
        /*
         * 初始化定位
         * */
        baiduMap = mapView.getMap();
        locationClient = new LocationClient(getApplicationContext());//创建LocationClient对象
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，设置定位模式,LocationMode.Hight_Accuracy：高精度；
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");
        option.setScanSpan(60 * 1000);//可选，设置发起定位请求的间隔，int类型，单位ms,需设置1000ms以上才有效
        option.setOpenGps(true);//使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        //可选，定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
//        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);//可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5 * 60 * 1000);//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setEnableSimulateGps(false);//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        locationClient.setLocOption(option);

        /*
         * 初始化地图显示
         * */
//        baiduMap = mapView.getMap();
        mapView.showZoomControls(false);
        //baiduMap.getUiSettings().setAllGesturesEnabled(false);//设置禁用所以有手势
        baiduMap.setMyLocationEnabled(true);//开启定位
//        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.map_marker); //定位图标
        int accuracyCircleFillColor = 0xAAFFFF88;//精度圈填充颜色
        int accuracyCircleStrokeColor = 0xAA00FF00; //精度圈边框颜色
//        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
//                MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker,accuracyCircleFillColor,accuracyCircleStrokeColor));

        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));//设置地图缩放级别
        uiSettings = baiduMap.getUiSettings();
        uiSettings.setOverlookingGesturesEnabled(false);


        bdLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(bdLocationListener);//注册监听函数
        locationClient.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    /*
                     * 绘制所在位置的方区
                     *
                     * */
                    for (int i = 0; i < coordinates.getCoordinates().size(); i++) {
                        double pointLatitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLatitude());
                        double pointLongitude = Double.parseDouble(coordinates.getCoordinates().get(i).getLongitude());
                        double[] pointGCJ02 = CoordinateTransformUtil.wgs84togcj02(pointLongitude, pointLatitude);
                        LatLng point = new LatLng(pointGCJ02[1], pointGCJ02[0]);
                        pointList.add(point);
                    }
                    //设置地图中心
                    double focusLatitude = Double.parseDouble(coordinates.getFocusLatitude());
                    double focusLongitude = Double.parseDouble(coordinates.getFocusLongitude());
                    double[] focusGCJ02 = CoordinateTransformUtil.wgs84togcj02(focusLongitude, focusLatitude);
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(focusGCJ02[1], focusGCJ02[0]));
                    baiduMap.setMapStatus(mapStatusUpdate);
                    //绘制虚线（需要多添加一起起点坐标，形成矩形）
                    pointList.add(pointList.get(0));
                    OverlayOptions ooPolyline = new PolylineOptions().width(4)
                            .color(0xAA121518).points(pointList);
                    Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
                    mPolyline.setDottedLine(true);

                }
            }
        };
        BaiduMap.OnMarkerClickListener listener = new BaiduMap.OnMarkerClickListener() {
            /**
             * 地图 Marker 覆盖物点击事件监听函数
             * @param marker 被点击的 marker
             */
            public boolean onMarkerClick(Marker marker) {
//                ToastUtils.showToast(HomeActivity.this, "点击MapMarkr");

                marker.getPosition();
                return false;
            }
        };
        baiduMap.setOnMarkerClickListener(listener);

        MapListener = new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             * @param point 点击的地理坐标
             */
            public void onMapClick(LatLng point) {
                initCubeMap(point);
                transYear(point);
//                isShowRush = true;
//                if (isShowRush) {
//                    relativeCountDown.setVisibility(View.GONE);
//                    relativeRush.setVisibility(View.VISIBLE);
//                } else {
//                    relativeRush.setVisibility(View.GONE);
//                }
//                shareRedPaperDialog();
                locationClient.requestLocation();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }

        };
    }


    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(HomeActivity.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.mipmap.app_logo;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
//        Toast.makeText(HomeActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
    }

    /**
     * 通知栏适配 - 定义通知栏渠道
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sharedChatRoom(EventMessage eventMessage) {
        if (eventMessage.getCode() == RequestCode.SCREENSHOT) {
            LogUtils.d("sharedChatRoom:", "分享元社区");
            final List imgList = new ArrayList<String>();
            shareChatRoomHandle = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        imgList.add(captureScreenFile.getAbsolutePath());
                        showLoadingDialog(true);
                        SCUploadImgHelper helper = new SCUploadImgHelper();
                        helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
                            @Override
                            public void onUploadSuc(List<String> urls) {
                                closeLoadingDialog();
                                SCHttpUtils.postWithUserId()
                                        .url(HttpApi.SHARE_CHAT_ROOM)
                                        .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                        .addParams("Img", urls.get(0) + "")
                                        .addParams("id", roomID + "")
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                LogUtils.d("网络异常");
                                                ThreadUtils.runOnMainThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ToastUtils.showToast(mContext, getResources().getString(R.string.internet_error));
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                String code = JSONObject.parseObject(response).getString("code");
                                                if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                                                    String data = JSONObject.parseObject(response).getString("data");
                                                    final String chatRoomShareType = JSONObject.parseObject(data).getString("ShareType");
                                                    String characterId = JSONObject.parseObject(data).getString("characterId");
                                                    final String intro = JSONObject.parseObject(data).getString("intro");
                                                    String background = JSONObject.parseObject(data).getString("background");
                                                    final String url = JSONObject.parseObject(data).getString("url");
                                                    final String title = JSONObject.parseObject(data).getString("title");
                                                    //分享参数
                                                    shareParams = new ShareParams();
                                                    shareChatRoomDialog = new CustomDialog(HomeActivity.this, true, true, 1.0, R.layout.layout_bottom_share, new int[]{R.id.share_image, R.id.mRlWechat, R.id.mRlWeixinCircle, R.id.mRlQQ, R.id.mRlFacebook, R.id.share_facebook, R.id.share_close});
                                                    shareChatRoomDialog.setViewId(R.id.share_image);
                                                    shareChatRoomDialog.setShareBitmap(ImageUtils.getBitmapByFile(captureScreenFile));
                                                    shareChatRoomDialog.show();
                                                    shareChatRoomDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                                                        @Override
                                                        public void OnItemClick(CustomDialog dialog, View view) {
                                                            switch (view.getId()) {
                                                                case R.id.mRlWechat:
//                                                                    if (chatRoomShareType.equals("SHARE_WEBPAGE")) {
                                                                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                    String absolutePath = captureScreenFile.getAbsolutePath();
                                                                    shareParams.setImagePath(absolutePath);
                                                                    shareParams.setText(intro);
                                                                    shareParams.setTitle(title);
                                                                    shareParams.setUrl(url);
                                                                    //调用分享接口share ，分享到微信平台。
                                                                    JShareInterface.share(Wechat.Name, shareParams, shareListener);
                                                                    closeLoadingDialog();
//                                                                    }
                                                                    break;
                                                                case R.id.mRlWeixinCircle:
                                                                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                    shareParams.setImagePath(captureScreenFile.getAbsolutePath());
                                                                    shareParams.setText(intro);
                                                                    shareParams.setTitle(title);
                                                                    shareParams.setUrl(url);
                                                                    //调用分享接口share ，分享到朋友圈平台。
                                                                    JShareInterface.share(WechatMoments.Name, shareParams, mPlatActionListener);
                                                                    break;
                                                                case R.id.mRlQQ:
                                                                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                                                                        ToastUtils.showToastLong(HomeActivity.this, getResources().getString(R.string.third_platform));
                                                                    } else {
                                                                        shareParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                        shareParams.setImagePath(captureScreenFile.getAbsolutePath());
                                                                        shareParams.setTitle(title);
                                                                        shareParams.setText(intro);
                                                                        shareParams.setUrl(url);
                                                                        //调用分享接口share ，分享到QQ平台。
                                                                        JShareInterface.share(QQ.Name, shareParams, mPlatActionListener);
                                                                    }
                                                                    break;
                                                                case R.id.mRlFacebook:
                                                                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                                                                        ToastUtils.showToastLong(HomeActivity.this, getResources().getString(R.string.third_platform));
                                                                    } else {
                                                                        shareParams.setShareType(Platform.SHARE_IMAGE);
                                                                        shareParams.setImagePath(captureScreenFile.getAbsolutePath());
                                                                        shareParams.setText(intro);
                                                                        shareParams.setTitle(title);
                                                                        shareParams.setUrl(url);
                                                                        //调用分享接口share ，分享到Facebook平台。
                                                                        JShareInterface.share(Facebook.Name, shareParams, mPlatActionListener);
                                                                    }
                                                                    break;
                                                                case R.id.share_facebook:
                                                                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                                                                        ToastUtils.showToastLong(HomeActivity.this, getResources().getString(R.string.third_platform));
                                                                    } else {
                                                                        shareParams.setShareType(Platform.SHARE_IMAGE);
                                                                        shareParams.setImagePath(captureScreenFile.getAbsolutePath());
                                                                        shareParams.setText(intro);
                                                                        shareParams.setTitle(title);
                                                                        shareParams.setUrl(url);
                                                                        //调用分享接口share ，分享到Facebook平台。
                                                                        JShareInterface.share(Facebook.Name, shareParams, mPlatActionListener);
                                                                    }
                                                                    break;
                                                                case R.id.share_close:
                                                                    shareChatRoomDialog.dismiss();
                                                                    break;
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void error() {
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showToast(mContext, getResources().getString(R.string.internet_error));
                                    }
                                });
                                closeLoadingDialog();
                            }
                        });
                        helper.upLoadImg(HomeActivity.this, imgList);
                    }
                }
            };

//            for (int i = 0; i < coordinates.size(); i++) {
//                int left = coordinatesList
//                new Rect()
//            }
//            baiduMap.snapshotScope();
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
            baiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap bitmap) {
                    FileOutputStream out;
                    try {
                        String filePath = ImageUtils.getSDPath() + File.separator + "shanchain";
                        //创建文件夹
                        File fPath = new File(filePath);
                        if (!fPath.exists()) {
                            fPath.mkdir();
                        }
                        captureScreenFile = new File(filePath + File.separator + ImageUtils.getTempFileName() + ".png");
                        out = new FileOutputStream(captureScreenFile);
                        if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                            out.flush();
                            out.close();
                        }
                        ImageUtils.displayToGallery(HomeActivity.this, captureScreenFile);
                        shareChatRoomHandle.sendEmptyMessage(1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void shareRedPaperDialog() {
        redPaperList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RedPaper redPaper = new RedPaper();
//            redPaperList.add(redPaper);
        }
        redPaperHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    redPaperDialog.setCallback(new com.shanchain.data.common.base.Callback() {
                        @Override
                        public void invoke() {
                            SCHttpUtils.get()
                                    .url(HttpApi.RED_PAPER_RECEIVE)
                                    .addParams("token", SCCacheUtils.getCacheToken())
                                    .addParams("characterId", SCCacheUtils.getCacheCharacterId())
                                    .addParams("userId", SCCacheUtils.getCacheUserId())
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            LogUtils.d("网络异常");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            String code = JSONObject.parseObject(response).getString("code");
                                            if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                                                String data = JSONObject.parseObject(response).getString("data");
                                                ThreadUtils.runOnMainThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ToastUtils.showToastLong(HomeActivity.this, "领取成功，可在钱包中查看");
                                                    }
                                                });

                                            }
                                        }
                                    });
                        }
                    }, new com.shanchain.data.common.base.Callback() {
                        @Override
                        public void invoke() {

                        }
                    });
                }
            }
        };
        SCHttpUtils.get()
                .url(HttpApi.RED_PAPER_LIST)
                .addParams("token", SCCacheUtils.getCacheToken())
                .addParams("characterId", "" + SCCacheUtils.getCacheCharacterId())
                .addParams("userId", "" + SCCacheUtils.getCacheUserId())
                .addParams("page", "" + page)
                .addParams("pageSize", "" + pageSize)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String content = JSONObject.parseObject(data).getString("content");
                            String totalElements = JSONObject.parseObject(data).getString("totalElements");
                            redPaperList = JSONObject.parseArray(content, RedPaper.class);
                            redPaperDialog = new RedPaperDialog(HomeActivity.this);
                            redPaperDialog.setDialogAdapter(new BaseQuickAdapter<RedPaper, BaseViewHolder>(R.layout.layout_coupon_detail_person_info, redPaperList) {
                                @Override
                                protected void convert(BaseViewHolder helper, RedPaper item) {
                                    CircleImageView avatar = helper.getView(R.id.iv_item_story_avatar);
                                    RequestOptions options = new RequestOptions();
                                    options.placeholder(R.mipmap.aurora_headicon_default);
                                    Glide.with(HomeActivity.this).load(item.getImgUrl()).apply(options).into(avatar);
                                    TextView name = helper.getView(R.id.tv_item_story_name);
                                    TextView from = helper.getView(R.id.tv_item_story_from);
                                    TextView amount = helper.getView(R.id.even_message_bounty);
                                    name.setText(item.getName());
                                    from.setText("来自" + item.getShareUserName() + "的分享");
                                    amount.setText("" + item.getAmount());

                                }
                            });
                            if (!totalElements.equals("0")) {
                                redPaperDialog.show();
                                redPaperHandle.sendEmptyMessage(1);
                            }
                        }
                    }
                });

    }

    @Override
    public int getPermissionsRequestCode() {
        return 0;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,

        };
    }

    @Override
    public void requestPermissionsSuccess() {
        initBaiduMap();
        locationClient.restart();
    }

    @Override
    public void requestPermissionsFail() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)) {
            //权限请求结果，并已经处理了该回调
//            if (requestCode==10002){
//                mLocationClient.restart();
//            }

            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        locationClient.stop();
//        ChatRoomManager.leaveChatRoom(Long.valueOf(roomID), new BasicCallback() {
//            @Override
//            public void gotResult(int i, String s) {
//                LogUtils.d("leaveChatRoom","离开聊天室");
//            }
//        });
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        isForeground = false;
        super.onPause();
    }


    @Override
    public void onResume() {
        mapView.onResume();
        isForeground = true;
        super.onResume();
    }

    @OnClick({R.id.image_view_history, R.id.img_view_hide})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_history:
                readyGo(FootPrintActivity.class);
                break;
            case R.id.img_view_hide:

//                shareRedPaperDialog();
                if (isHide) {
                    hideImageView.setBackground(getResources().getDrawable(R.mipmap.home_show));
                    btJoin.setVisibility(View.GONE);
                    linearReset.setVisibility(View.GONE);
                    linearHot.setVisibility(View.GONE);
                    tvLocation.setVisibility(View.GONE);
                    isHide = false;
                } else {
                    hideImageView.setBackground(getResources().getDrawable(R.mipmap.home_hide));
                    btJoin.setVisibility(View.VISIBLE);
                    linearReset.setVisibility(View.VISIBLE);
                    linearHot.setVisibility(View.VISIBLE);
                    tvLocation.setVisibility(View.VISIBLE);
                    isHide = true;
                }
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);//加载动画资源文件
                hideImageView.setAnimation(shake);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeProgress();
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            // 构造定位数据
            //构建MarkerOption，用于在地图上添加Marker
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.mipmap.map_marker);
            //设置定位跟随
            MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
            //显示定位图标
            baiduMap.setMyLocationConfiguration(config);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
            //baiduMap.setMyLocationEnabled(false);

            imgInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                bdLocation
                    initCubeMap(latLng);
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(myFocusPoint);
                    baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));//设置地图缩放级别
                    baiduMap.setMapStatus(mapStatusUpdate);
                }
            });
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                myLocation = bdLocation;

                coordinateConverter = new CoordinateConverter();
                //百度转为gps坐标
                gpsLatLng = coordinateConverter.from(CoordinateConverter.CoordType.BD09LL).coord(latLng).convert();
                //服务端请求数据
//                handler.sendEmptyMessage(1);
                //设置显示地图中心点
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.setMapStatus(mapStatusUpdate);

                initData(latLng);
                transYear(latLng);
                baiduMap.setOnMapClickListener(MapListener);

//                LogUtils.d("百度地图定位回调 " + bdLocation.getLocType() + "百度地图坐标 " + bdLocation.getCoorType() + "类型 " + bdLocation.getLongitude() + " " + bdLocation.getLatitude());
//                LogUtils.d(bdLocation.getLocType() + "百度地图坐标 转换为 WGS84类型 经度" + WGS84point[0] + " 纬度： " + WGS84point[1]);
                if (bdLocation.getLocationWhere() == BDLocation.LOCATION_WHERE_OUT_CN) {
                    //判断位置是否为国外
                }


            }

        }

    }

    public void showProgress() {
        mDialog = new ProgressDialog(this);
        mDialog.setMax(100);
        mDialog.setMessage("数据请求中..");
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    ToastUtils.showToast(HomeActivity.this, showMsg.toString());

                    //极光本地通知
             /*       JPushLocalNotification ln = new JPushLocalNotification();
                    ln.setBuilderId(0);
                    ln.setContent(showMsg.toString());
                    ln.setTitle("收到一条消息");
                    ln.setNotificationId(11111111) ;

                    Map<String , Object> map = new HashMap<String, Object>() ;
                    map.put("name", "jpush") ;
                    map.put("test", "111") ;
                    JSONObject json = new JSONObject(map) ;
                    ln.setExtras(json.toString()) ;
                    JPushInterface.addLocalNotification(getApplicationContext(), ln);*/
                    //设置点击通知栏的动作为启动另外一个广播
                    Intent broadcastIntent = new Intent(context, MyReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.
                            getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(HomeActivity.this, "subscribe")
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setContentTitle("收到一条订阅消息")
                            .setContentText(showMsg.toString())
                            .setWhen(System.currentTimeMillis())
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.mipmap.app_logo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo))
                            .setAutoCancel(true)
                            .build();
                    manager.notify(1, notification);
                }
            } catch (Exception e) {
            }
        }

        public void onEventMainThread(ChatRoomMessageEvent event) {
            Log.d("tag", "ChatRoomMessageEvent received .");
            Conversation chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
            List chatRoomList = chatRoomConversation.getAllMessage();
            final List<cn.jpush.im.android.api.model.Message> msgs = event.getMessages();
            final MyMessage myMessage;
            int size = msgs.size();

        }
    }
}
