package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;
import com.shanchain.data.common.utils.ImageUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.AppsAdapter;
import com.shanchain.shandata.adapter.DynamicImagesAdapter;
import com.shanchain.shandata.adapter.ImagePickerAdapter;
import com.shanchain.shandata.adapter.SimpleAppsGridView;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.db.ConversationEntryDao;
import com.shanchain.shandata.db.MessageEntryDao;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.ConversationEntry;
import com.shanchain.shandata.ui.model.Coordinates;
import com.shanchain.shandata.ui.model.HotChatRoom;
import com.shanchain.shandata.ui.model.JmAccount;
import com.shanchain.shandata.ui.model.MessageEntry;
import com.shanchain.shandata.ui.model.MessageModel;
import com.shanchain.shandata.ui.model.ModifyUserInfo;
import com.shanchain.shandata.ui.model.UserEntry;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.ModifyUserInfoActivity;
import com.shanchain.shandata.ui.view.activity.coupon.CouponListActivity;
import com.shanchain.shandata.ui.view.activity.coupon.MyCouponListActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.ChatView;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.ui.view.activity.settings.SettingsActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.ui.view.fragment.view.TaskView;
import com.shanchain.shandata.utils.CountDownTimeUtils;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.utils.GlideImageLoader;
import com.shanchain.shandata.utils.MyEmojiFilter;
import com.shanchain.shandata.utils.MyOrientationListener;
import com.shanchain.shandata.utils.RequestCode;
import com.shanchain.shandata.utils.SelectDialog;
import com.shanchain.shandata.widgets.GuideView;
import com.shanchain.shandata.widgets.XhsEmoticonsKeyBoard;
import com.shanchain.shandata.widgets.arcMenu.ArcMenu;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.TipItem;
import com.shanchain.shandata.widgets.other.TipView;
import com.shanchain.shandata.widgets.photochoose.ChoosePhoto;
import com.shanchain.shandata.widgets.photochoose.DialogCreator;
import com.shanchain.shandata.widgets.photochoose.PhotoUtils;
import com.shanchain.shandata.widgets.pickerimage.PickImageActivity;
import com.shanchain.shandata.widgets.pickerimage.utils.Extras;
import com.shanchain.shandata.widgets.pickerimage.utils.StorageType;
import com.shanchain.shandata.widgets.pickerimage.utils.StorageUtil;
import com.shanchain.shandata.widgets.pickerimage.utils.StringUtil;
import com.shanchain.shandata.widgets.takevideo.CameraActivity;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.emoji.DefEmoticons;
import cn.jiguang.imui.chatinput.emoji.EmojiBean;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.CustomEvenMsgHolder;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ViewHolderController;
import cn.jiguang.imui.messages.ptr.PtrHandler;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.android.utils.Logger;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;
import cn.jiguang.share.weibo.SinaWeibo;
import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MediaContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.ChatRoomInfo;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import sj.keyboard.adpater.EmoticonsAdapter;
import sj.keyboard.adpater.PageSetAdapter;
import sj.keyboard.data.EmoticonPageEntity;
import sj.keyboard.data.EmoticonPageSetEntity;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.interfaces.EmoticonDisplayListener;
import sj.keyboard.interfaces.PageViewInstantiateListener;
import sj.keyboard.utils.EmoticonsKeyboardUtils;
import sj.keyboard.utils.imageloader.ImageBase;
import sj.keyboard.widget.EmoticonPageView;
import sj.keyboard.widget.FuncLayout;

public class MessageListActivity extends BaseActivity implements View.OnTouchListener,
        EasyPermissions.PermissionCallbacks,
        SensorEventListener,
        ArthurToolBar.OnRightClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        ImagePickerAdapter.OnRecyclerViewItemClickListener {


    private final static String TAG = "MessageListActivity";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int IMAGE_PICKER = 1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private UserInfo mMyInfo;
    private ShareParams shareParams;
    private com.shanchain.data.common.ui.widgets.CustomDialog shareChatRoomDialog;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList = new ArrayList<>(); //当前选择的所有图片
    private int maxImgCount = 1;               //允许选择图片最大数
    public static final String JPG = ".jpg";
    //百度地图
    private LocationClient locationClient;
    private BDLocationListener bdLocationListener;
    private UiSettings uiSettings;
    private MyOrientationListener myOrientationListener;
    private Coordinates coordinates;
    private List pointList = new ArrayList();
    private List<Coordinates> coordinatesList;
    private List roomList = new ArrayList();
    private boolean isFirstLoc = true; // 是否首次定位
    private int joinRoomId;
    private com.shanchain.data.common.ui.widgets.CustomDialog mDialog;

    private ArthurToolBar mTbMain;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private TextView userNikeView;
    private TextView tvUserSign;
    private ImageView ivUserModify;
    private ImageView userHeadView;
    private RecyclerView recyclerView;
    private String roomID;
    private String newRoomId, roomName, mImgPath;
    private ArrayList<String> photos = new ArrayList<>();
    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData;
    private ArrayList imgData;
    private String formatDate;
    private String imgUrl;

    private InputMethodManager mImm;
    private Window mWindow;
    private HeadsetDetectReceiver mReceiver;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private ArcMenu mArcMenu;
    private ArcMenu.OnMenuItemClickListener onMenuItemClickListener;
    private Handler handler, messageHandler, dialogHandler;
    private List<Conversation> conversationList;
    private double seatRmbRate;
    private TextView tvSeatRate;
    private ChoosePhoto mChoosePhoto = new ChoosePhoto();
    private boolean mShowSoftInput = false;
    private boolean isRestartActivity = false;
    private GuideView guideView;
    private int memberCount;


    /**
     * Store all image messages' path, pass it to {@link BrowserImageActivity},
     * so that click image message can browser all images.
     */
    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();
    private ChatEventMessage chatEventMessage;
    private List<MyMessage> messageList = new ArrayList<>();
    private List<MyMessage> mEventMessageList = new ArrayList<>();
    private Conversation chatRoomConversation, localConversation;
    private List<Conversation> allRoomConversation;

    private TextView limitedTime;
    private Thread addTaskThread;
    private long timeStamp;
    private TaskPresenter taskPresenter;
    private SCTimePickerView scTimePickerView;
    private SCTimePickerView.OnTimeSelectListener onTimeSelectListener;
    private ChatEventMessage chatEventMessage1;
    private MyMessage evenMyMessage = new MyMessage();
    private TextView roomNum;
    private RelativeLayout relativeChatRoom;
    private XhsEmoticonsKeyBoard xhsEmoticonsKeyBoard;
    private DynamicImagesAdapter mImagesAdapter;
    private Handler baiduHandler;
    private boolean isIn, isHotChatRoom;
    private String isSuper;
    private GuideView guideView3;
    private GuideView guideView2;
    private File captureScreenFile;
    private List imgList = new ArrayList();
    private LatLng myLatLng;
    private Bundle mBundle;
    private WebSocketManager mWebSocketManager;
    private Handler shareHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            closeLoadingDialog();
            closeProgress();
            String toastMsg = (String) msg.obj;
            Toast.makeText(MessageListActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
        }
    };
    private PlatActionListener mPlatActionListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            if (shareHandler != null) {
                android.os.Message message = handler.obtainMessage();
                message.obj = "分享成功";
                shareHandler.sendMessage(message);
            }
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            if (shareHandler != null) {
                android.os.Message message = shareHandler.obtainMessage();
                message.obj = "分享失败:" + (error != null ? error.getMessage() : "") + "---" + errorCode;
                Logger.dd(TAG, message.obj + "");
                shareHandler.sendMessage(message);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (shareHandler != null) {
                android.os.Message message = shareHandler.obtainMessage();
                message.obj = "分享取消";
                shareHandler.sendMessage(message);
            }
        }
    };
    private List<Message> mMsgs;
    private MessageEntryDao mEntryDao;
    private List<MessageEntry> mMessageEntryList;
    private boolean mIsHasRoom;
    private MessageEntryDao mMessageEntryDao;
    private com.shanchain.data.common.ui.widgets.CustomDialog sureDialog;
    //webSocket监听
    private SocketListener mSocketListener = new SimpleListener() {
        @Override
        public <T> void onMessage(final String message, T data) {
//                super.onMessage(message, data);
            com.shanchain.shandata.widgets.takevideo.utils.LogUtils.d("WebSocketHandler", "接收到的message:" + message);
            String webSocketCode = SCJsonUtils.parseString(message, "code");
            String webSocketData = SCJsonUtils.parseString(message, "data");
            String type = SCJsonUtils.parseString(message, "type");
            if ("00000000".equals(webSocketCode)) {
                switch (type) {
                    case "ARSPaySuccess"://支付成功进入首层
                        String userId = SCJsonUtils.parseString(webSocketData, "userId");
                        if (userId.equals(SCCacheUtils.getCacheUserId())) {

                        }
                        break;
                    case "makeSure"://确认进入下一层
//                        if (sureDialog != null) {
//                            sureDialog.dismiss();
//                        }
//                        String joinNextUse = SCJsonUtils.parseString(webSocketData, "userId");
//                        if (joinNextUse.equals(SCCacheUtils.getCacheUserId())) {
//                            showLoadingDialog(false);
//                            String s = SCCacheUtils.getCacheUserId();
//                            enterNextRoom();
//                        }
                        break;
                    case "confirmNum":
                        String num = SCJsonUtils.parseString(webSocketData, "num");
                        String confirmString = SCJsonUtils.parseString(webSocketData, "confirmList");
                        List<String> confirmList = SCJsonUtils.parseArr(confirmString, String.class);
                        //是否点击确认
                        isConfirm(num, confirmList);
                        break;
                    case "SysMessage": //显示确认进入
                        final String endTime = SCJsonUtils.parseString(webSocketData, "endTime");
                        String list = SCJsonUtils.parseString(webSocketData, "list");
                        List<String> users = SCJsonUtils.parseArr(list, String.class);
                        final long overTime = Long.valueOf(endTime);//结束时间
                        final long currentTime = System.currentTimeMillis(); //当前系统时间
                        if (users.indexOf("" + SCCacheUtils.getCacheUserId()) != -1) {
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    Activity topActivity = ActivityStackManager.getInstance().getTopActivity();
                                    sureDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(topActivity, false, 1.0,
                                            R.layout.common_dialog_costom, new int[]{R.id.btn_dialog_task_detail_sure});
                                    sureDialog.setDialogTitle("下一层元社区已开启");
                                    sureDialog.setMessageContent("点击确认进入元社区，否则您将被踢出聊天室");
                                    sureDialog.setSureText("确认");
                                    sureDialog.setMessageContentSize(16);
                                    sureDialog.show();
                                    sureDialog.setCanceledOnTouchOutside(false);
                                    if (currentTime < overTime) {
                                        TextView countDown = sureDialog.findViewById(R.id.text_count_down);
                                        CountDownTimeUtils countDownTimeUtils = new CountDownTimeUtils(countDown, "确认倒计时：", overTime - currentTime, 1000);
                                        countDownTimeUtils.start();
                                    }
                                    sureDialog.setOnItemClickListener(new com.shanchain.data.common.ui.widgets.CustomDialog.OnItemClickListener() {
                                        @Override
                                        public void OnItemClick(com.shanchain.data.common.ui.widgets.CustomDialog dialog, View view) {
                                            switch (view.getId()) {
                                                case R.id.btn_dialog_task_detail_sure:
                                                    Map dataMap = new HashMap();
                                                    dataMap.put("userId", SCCacheUtils.getCacheUserId());
                                                    dataMap.put("charcterId", SCCacheUtils.getCacheCharacterId());
                                                    dataMap.put("type", "makeSure");
                                                    final String dataString = JSONObject.toJSONString(dataMap);
                                                    WebSocketHandler.getDefault().send(dataString);
                                                    if (currentTime > overTime) {
                                                        sureDialog.dismiss();
                                                        ThreadUtils.runOnMainThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ToastUtils.showToast(MessageListActivity.this, "进入下一层时间已过期");
                                                            }
                                                        });
                                                    } else {
                                                        //进入下一层
                                                        showLoadingDialog(false);
                                                        enterNextRoom();
                                                        sureDialog.dismiss();
                                                    }

                                                    break;
                                                case R.id.linear_layout:

                                                    break;
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        break;
                }
            }
        }

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            super.onSendDataError(errorResponse);
            com.shanchain.shandata.widgets.takevideo.utils.LogUtils.d("WebSocketHandler", "发送消息失败:" + errorResponse.toString());
        }

        @Override
        public <T> void onMessage(ByteBuffer bytes, T data) {
            super.onMessage(bytes, data);
            com.shanchain.shandata.widgets.takevideo.utils.LogUtils.d("WebSocketHandler", "接收到的bytes:");
        }
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_seting;
    }

    @Override
    protected void initViewsAndEvents() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mChatView = (ChatView) findViewById(R.id.chat_view);
        xhsEmoticonsKeyBoard = findViewById(R.id.ek_bar);
        mArcMenu = findViewById(R.id.fbn_menu);
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();
        registerProximitySensorListener();
        mArcMenu.setVisibility(View.GONE);
        initToolBar(roomID);
        initChatView();
        initView();
        initEmojiData();
        initMsgAdapter();
        initData(roomID);
//        getChatRoomMessage(roomID); //获取聊天室历史消息
        //必须执行在initView()方法之后；
//        装载本地缓存消息
//        ConversationEntry conversationEntry = new ConversationEntry();
//        conversationEntry.setTargetName(roomID);
//        ConversationEntryDao entryDao = MyApplication.getDaoSession().getConversationEntryDao();
//        mIsHasRoom = entryDao.hasKey(conversationEntry);
        loadMessageData(roomID);
        mArcMenu.setOnMenuItemClickListener(onMenuItemClickListener);
//        initEmojiData();

        //是否是超级用户
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    //加入聊天室按钮
                    if (isIn == true || isSuper.equals("true")) {
                        mChatView.isShowBtnInputJoin(false);
                        mChatView.getChatInputView().setShowBottomMenu(true);
//                        mArcMenu.setVisibility(View.VISIBLE);
                        mArcMenu.setVisibility(View.GONE);
                        xhsEmoticonsKeyBoard.getInputJoin().setVisibility(View.GONE);
                        xhsEmoticonsKeyBoard.getXhsEmoticon().setVisibility(View.VISIBLE);
                        mChatView.setOnBtnInputClickListener(new ChatView.OnBtnInputClickListener() {
                            @Override
                            public void OnBtnInputClick(View view) {
//                    String[] hxUserName = new String[]{SCCacheUtils.getCacheHxUserName()};
//                    String jArray = JSONArray.toJSONString(hxUserName);
//                    mChatView.isShowBtnInputJoin(false);
//                    mChatView.getChatInputView().setShowBottomMenu(true);
//                    mArcMenu.setVisibility(View.VISIBLE);
//                    xhsEmoticonsKeyBoard.getInputJoin().setVisibility(View.GONE);
                                if (isIn) {
                                    ToastUtils.showToast(MessageListActivity.this, "您不在该聊天室区域内");
                                }
                                xhsEmoticonsKeyBoard.getXhsEmoticon().setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        ToastUtils.showToast(MessageListActivity.this, "您不在该聊天室区域内");
                    }
                }
            }
        };

        dialogHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    ToastUtils.showToastLong(MessageListActivity.this, "任务发送失败，您的余额不足");
                } else if (msg.what == 2) {
                    ToastUtils.showToastLong(MessageListActivity.this, "任务发送失败，网络连接错误");
                }
            }
        };

    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomId");
        LogUtils.d("roomId", roomID + "");
//        ToastUtils.showToast(MessageListActivity.this,""+roomID);
        roomName = intent.getStringExtra("roomName");
//        isIn = intent.getBooleanExtra("isInCharRoom", true);
        isHotChatRoom = intent.getBooleanExtra("isHotChatRoom", false);
        JMessageClient.registerEventReceiver(this);
        //进入聊天室
        showLoadingDialog();
        enterChatRoom();
//        获取WebSocket管理对象
        WebSocketHandler.getDefault().addListener(mSocketListener);
        super.onCreate(savedInstanceState);
    }

    //显示遮罩层
    private void setGuideView() {
        // 使用图片
        final ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.img_new_task_guide);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(params);
        guideView = GuideView.Builder
                .newInstance(this)
                .setTargetView(mArcMenu.findViewWithTag("fbutton"))//设置目标
                .setCustomGuideView(null)
                .setDirction(GuideView.Direction.LEFT_BOTTOM)
                .setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
                .setBgColor(getResources().getColor(R.color.shadow))
                .setOnclickListener(new GuideView.OnClickCallback() {
                    @Override
                    public void onClickedGuideView() {
                        guideView.hide();
//                        mArcMenu.toggleMenu(500);

                    }
                })
                .build();
    }

    //进入聊天室
    private void enterChatRoom() {
        if (!TextUtils.isEmpty(roomID)) {
            ChatRoomManager.enterChatRoom(Long.valueOf(roomID), new RequestCallback<Conversation>() {
                @Override
                public void gotResult(int i, String s, Conversation conversation) {
                    LogUtils.d("enterChatRoom", "roomID:" + roomID);
                    if (i == 0) {
                        LogUtils.d("enterChatRoom", "加入聊天室成功 code:" + i);
                        chatRoomConversation = conversation;
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                closeLoadingDialog();
                            }
                        });
                        //本地存储会话
                        if (chatRoomConversation != null) {
                            ConversationEntry conversationEntry = new ConversationEntry();
                            conversationEntry.setTargetName(roomID);
                            ConversationEntryDao entryDao = MyApplication.getDaoSession().getConversationEntryDao();
                            mIsHasRoom = entryDao.hasKey(conversationEntry);
                            if (mIsHasRoom == false) {
                                entryDao.insertOrReplace(conversationEntry);
                            }
                        }
                        //从本地加载会话
//                        loadMessageData(roomID);
                        closeLoadingDialog();
                    } else if (i == 851003) {//成员已在聊天室
                        LogUtils.d("enterChatRoom", "成员已在聊天室 code:" + i);
                        ChatRoomManager.leaveChatRoom(Long.valueOf(roomID), new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {

                                }
                                enterChatRoom();
                            }
                        });
                        closeLoadingDialog();
                    } else if (i == 871300) {//未登录
                        LogUtils.d("enterChatRoom", "成员未登录 code:" + i);
                        JMessageClient.login(SCCacheUtils.getCacheHxUserName(), SCCacheUtils.getCacheHxUserName(), new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    enterChatRoom();
                                    closeLoadingDialog();
                                } else {
                                    closeLoadingDialog();
                                    ToastUtils.showToastLong(MessageListActivity.this, "账号登录失败,请重新登录");
//                                            new Handler().postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    readyGo(LoginActivity.class);
//                                                }
//                                            }, 1000);

                                }
                            }
                        });
                        closeLoadingDialog();
                    } else if (i == 852001) {
                        closeLoadingDialog();
                    }
                    closeLoadingDialog();
                }
            });
        } else {
            ToastUtils.showToast(MessageListActivity.this, "加入聊天室失败");
            closeLoadingDialog();
        }
    }

    //初始化聊天数据
    private void initData(final String roomID) {
//        taskPresenter = new TaskPresenterImpl(this);
        myLatLng = HomeActivity.latLng;
        //获取是否是超级用户
        showLoadingDialog();
        SCHttpUtils.get()
                .url(HttpApi.SUPER_USER + "?token=" + SCCacheUtils.getCacheToken())
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("####### USER_COORDINATE 请求失败 #######");
                        closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("####### USER_COORDINATE 请求成功 #######");
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            isSuper = data;
                            //极光消息监听注册
                            JMessageClient.registerEventReceiver(MessageListActivity.this, 1000);
                            handler.sendEmptyMessage(1);
                        }
                        closeLoadingDialog();
                    }
                });
    }

    //进入下一层
    private void enterNextRoom() {
        SCHttpUtils.post()
                .url(HttpApi.ENTER_NEXT_CHAT_ROOM)
                .addParams("userId", "" + SCCacheUtils.getCacheUserId())
                .addParams("subuserId", "" + SCCacheUtils.getCacheCharacterId())
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = SCJsonUtils.parseCode(response);
                        LogUtils.d("进入下一层", "response:" + response);
                        closeLoadingDialog();
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(MessageListActivity.this, "已进入下一层房间");
                            }
                        });
                        if (sureDialog != null) {
                            sureDialog.dismiss();
                        }
                    }
                });
    }

    //是否点击确认
    private void isConfirm(String num, List<String> confirmList) {
        if (!num.equals("0")) {
            if (confirmList.indexOf(SCCacheUtils.getCacheUserId()) != -1) {
                if (sureDialog != null) {
                    sureDialog.dismiss();
                }
            }
        } else {
            if (sureDialog != null) {
                sureDialog.dismiss();
            }
//            ThreadUtils.runOnMainThread(new Runnable() {
//                @Override
//                public void run() {
//                    ToastUtils.showToast(MessageListActivity.this, "进入下一层时间已过期");
//                }
//            });
        }
    }

    private void initView() {
        /*
         * 初始化侧滑栏
         * */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


//        drawer.setStatusBarBackgroundColor(Color.parseColor("#00000"));
        drawer.setStatusBarBackground(R.drawable.selector_bg_msg_send_theme);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mTbMain.setOnUserHeadClickListener(new ArthurToolBar.OnUserHeadClickListener() {
            @Override
            public void onUserHeadClick(View v) {
//                ToastUtils.showToast(MessageListActivity.this,"头像按钮");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                toggle.onDrawerOpened(drawer);
            }
        });

        View headView = navigationView.getHeaderView(0);
        userNikeView = headView.findViewById(R.id.tv_nike_name);//用户昵称
        tvUserSign = headView.findViewById(R.id.tv_user_sign);//用户签名
        ivUserModify = headView.findViewById(R.id.iv_user_modify);//修改资料按钮
        userHeadView = headView.findViewById(R.id.iv_user_head);//用户头像
        recyclerView = findViewById(R.id.recyclerView);

        initUserInfo();
        //初始化图片选择器
        pickImages();
        initWidget();
        userNikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageListActivity.this, ModifyUserInfoActivity.class);
                String nikeName = userNikeView.getText().toString();
                String userSign = tvUserSign.getText().toString();
                intent.putExtra("nikeName", nikeName);
                intent.putExtra("userSign", userSign);
                startActivity(intent);
            }
        });

        userHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置头像
                selectImage();
            }
        });

        //修改用户资料
        ivUserModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageListActivity.this, ModifyUserInfoActivity.class);
                String charater = SCCacheUtils.getCacheCharacterInfo();
                CharacterInfo characterInfo = JSONObject.parseObject(charater, CharacterInfo.class);
                String headImg = characterInfo.getHeadImg();
                String cacheHeadImg = SCCacheUtils.getCacheHeadImg();
                intent.putExtra("headImg", SCCacheUtils.getCacheHeadImg());
                startActivity(intent);
            }
        });

//        RequestOptions options = new RequestOptions();
//                    options.placeholder(R.mipmap.aurora_headicon_default);
//                    Glide.with(MessageListActivity.this)
//                            .load(headUrl)
//                            .apply(options)
//                            .into(userHeadView);
        /*
         * 悬浮按钮蒙层
         * */
//        mArcMenu.setOnMenuClickListener(new ArcMenu.OnMenuClickListener() {
//            @Override
//            public void onMenuClick(View view) {
////                setGuideView();
//                ToastUtils.showToastLong(MessageListActivity.this,"弹窗");
//            }
//        });
        /*
         * 初始化悬浮按钮
         * */
        onMenuItemClickListener = new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                mChatView.getChatInputView().setFocusable(false);
                switch (view.getId()) {
                    //马甲劵
                    case R.id.linear_add_coupon:
                        mArcMenu.getChildAt(0).findViewWithTag("circelText").setBackground(getResources().getDrawable(R.drawable.shape_guide_point_default));
                        Intent couponIntent = new Intent(MessageListActivity.this, CouponListActivity.class);
                        couponIntent.putExtra("roomId", roomID);
                        startActivity(couponIntent);
                        break;
                    //查询任务
                    case R.id.linear_add_query:
                        mArcMenu.getChildAt(0).findViewWithTag("circelText").setBackground(getResources().getDrawable(R.drawable.shape_guide_point_default));
                        Intent taskIntent = new Intent(MessageListActivity.this, TaskDetailActivity.class);
                        taskIntent.putExtra("roomId", roomID);
                        startActivity(taskIntent);
                        break;
                    //添加任务
                    case R.id.linear_play:
                        mArcMenu.getChildAt(0).findViewWithTag("circelText").setBackground(getResources().getDrawable(R.drawable.shape_guide_point_default));
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
                                            if (data.equals("暂无活动")) {
                                                ToastUtils.showToastLong(MessageListActivity.this, "新玩法开发中，敬请期待");
                                            } else {
                                                String ruleDescribe = JSONObject.parseObject(data).getString("ruleDescribe");
                                                String startTme = JSONObject.parseObject(data).getString("startTime");
                                                String endTime = JSONObject.parseObject(data).getString("endTime");
                                                if (System.currentTimeMillis() > Long.valueOf(endTime)) {
                                                    ToastUtils.showToastLong(MessageListActivity.this, "新玩法开发中，敬请期待");
                                                } else {
                                                    finish();
                                                }
                                            }
                                        }
                                    }
                                });
                        break;

                }

            }
        };
//        initTaskDialog();


    }

    /*
     * 初始化输入框
     * */
    private void initEmojiData() {
        ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sEmojiArray);
        xhsEmoticonsKeyBoard.getBtnSend().setBackgroundColor(getResources().getColor(R.color.colorViolet));
        xhsEmoticonsKeyBoard.addOnFuncKeyBoardListener(new FuncLayout.OnFuncKeyBoardListener() {
            @Override
            public void OnFuncPop(int i) {
                scrollToBottom();
            }

            @Override
            public void OnFuncClose() {

            }
        });
        SimpleAppsGridView gridView = new SimpleAppsGridView(this);
        xhsEmoticonsKeyBoard.addFuncView(gridView);
        // emoticon click
        final EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
            @Override
            public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
                if (isDelBtn) {
                    int action = KeyEvent.ACTION_DOWN;
                    int code = KeyEvent.KEYCODE_DEL;
                    KeyEvent event = new KeyEvent(action, code);
                    xhsEmoticonsKeyBoard.getEtChat().onKeyDown(KeyEvent.KEYCODE_DEL, event);
                } else {
                    if (o == null) {
                        return;
                    }
                    String content = null;
                    if (o instanceof EmojiBean) {
                        content = ((EmojiBean) o).emoji;
                    }
                    int index = xhsEmoticonsKeyBoard.getEtChat().getSelectionStart();
                    Editable editable = xhsEmoticonsKeyBoard.getEtChat().getText();
                    editable.insert(index, content);
                }
            }
        };

        // emoticon instantiate
        final EmoticonDisplayListener emoticonDisplayListener = new EmoticonDisplayListener() {
            @Override
            public void onBindView(int i, ViewGroup viewGroup, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
                final EmojiBean emojiBean = (EmojiBean) object;
                if (emojiBean == null && !isDelBtn) {
                    return;
                }

                viewHolder.ly_root.setBackgroundResource(com.keyboard.view.R.drawable.bg_emoticon);

                if (isDelBtn) {
                    viewHolder.iv_emoticon.setImageResource(R.mipmap.emoji);
                } else {
                    viewHolder.iv_emoticon.setImageResource(emojiBean.icon);
                }

                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emoticonClickListener.onEmoticonClick(emojiBean, 0, isDelBtn);
                    }
                });
            }
        };

        //  page instantiate
        PageViewInstantiateListener pageViewInstantiateListener = new PageViewInstantiateListener<EmoticonPageEntity>() {
            @Override
            public View instantiateItem(ViewGroup viewGroup, int i, EmoticonPageEntity pageEntity) {
                if (pageEntity.getRootView() == null) {
                    EmoticonPageView pageView = new EmoticonPageView(viewGroup.getContext());
                    pageView.setNumColumns(pageEntity.getRow());
                    pageEntity.setRootView(pageView);
                    try {
                        EmoticonsAdapter adapter = new EmoticonsAdapter(viewGroup.getContext(), pageEntity, null);
                        // emoticon instantiate
                        adapter.setOnDisPlayListener(emoticonDisplayListener);
                        pageView.getEmoticonsGridView().setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return pageEntity.getRootView();
            }
        };

        // build
        EmoticonPageSetEntity xhsPageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(3)
                .setRow(7)
                .setEmoticonList(emojiArray)
                .setIPageViewInstantiateItem(pageViewInstantiateListener)
                .setShowDelBtn(EmoticonPageEntity.DelBtnStatus.LAST)
                .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("emoji"))
                .build();

        PageSetAdapter pageSetAdapter = new PageSetAdapter();
        pageSetAdapter.add(xhsPageSetEntity);
        xhsEmoticonsKeyBoard.setAdapter(pageSetAdapter);
        xhsEmoticonsKeyBoard.getEtChat().addEmoticonFilter(new MyEmojiFilter());

    }

    private void initUserInfo() {
        final Dialog dialog = DialogCreator.createLoadingDialog(MessageListActivity.this,
                MessageListActivity.this.getString(R.string.jmui_loading));
        String character = SCCacheUtils.getCacheCharacterInfo();
        final CharacterInfo cacheCharacter = JSONObject.parseObject(character, CharacterInfo.class);
        mMyInfo = JMessageClient.getMyInfo();
        if (mMyInfo != null) {
//            userNikeView.setText(mMyInfo.getNickname());
//            SharePreferenceManager.setRegisterUsername(mMyInfo.getNickname());
            userNikeView.setText("" + mMyInfo.getNickname());
            tvUserSign.setText(mMyInfo.getSignature());
            mMyInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int responseCode, String responseMessage, Bitmap avatarBitmap) {
                    String s = responseMessage;
                    if (responseCode == 0) {
                        userHeadView.setImageBitmap(avatarBitmap);
                    } else {
                        userHeadView.setImageResource(R.mipmap.aurora_headicon_default);
                    }
                }
            });
        } else {
            CharacterInfo characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
            if (characterInfo != null && characterInfo.getCharacterId() != 0) {
                String nikeName = characterInfo.getName() != null ? characterInfo.getName() : "";
                String signature = characterInfo.getSignature() != null ? characterInfo.getSignature() : "";
                final String headImg = characterInfo.getHeadImg() != null ? characterInfo.getHeadImg() : "";
                JmAccount jmUserInfo = new JmAccount();
                if (!TextUtils.isEmpty(nikeName)) {
                    userNikeView.setText(nikeName);
                    jmUserInfo.setNickname(nikeName);//设置昵称
                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, jmUserInfo, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            String s1 = s;
                        }
                    });
                }
                if (!TextUtils.isEmpty(signature)) {
                    tvUserSign.setText(signature);
                    jmUserInfo.setSignature(signature);//设置签名
                    JMessageClient.updateMyInfo(UserInfo.Field.signature, jmUserInfo, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            String s1 = s;
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(headImg)) {
                            RequestOptions options = new RequestOptions();
                            options.placeholder(R.mipmap.aurora_headicon_default);
                            Glide.with(MessageListActivity.this).load(headImg).apply(options).into(userHeadView);
                        }
                    }
                });
            }
        }

    }

    private void upLoad() {
        showLoadingDialog();
        List<String> imgSrc = new ArrayList<>();
        imgSrc.add(mImgPath);
        SCUploadImgHelper helper = new SCUploadImgHelper();
        helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
            @Override
            public void onUploadSuc(List<String> urls) {
                closeLoadingDialog();
                imgUrl = urls.get(0);
            }

            @Override
            public void error() {
                closeLoadingDialog();
                LogUtils.i("oss上传失败");
            }
        });

        helper.upLoadImg(mContext, imgSrc);
    }

    private void selectImage() {
        int from = PickImageActivity.FROM_LOCAL;
        int requestCode = PhotoUtils.INTENT_SELECT;
        if (ContextCompat.checkSelfPermission(MessageListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
//            PickImageActivity.start(MessageListActivity.this, requestCode, from, tempFile(), true, 1,
//                    true, false, 0, 0);
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 描述： //上传用户头像
     */
    private void pickImages() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        initWidget();
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:

                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.TAKE_PHOTO:
                if (data != null) {
                    final String photoPath = data.getStringExtra("take_photo");
                    Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                    final Message msg;
                    final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                    if (chatRoomConversation != null) {
                        try {
                            msg = chatRoomConversation.createSendImageMessage(new File(photoPath));
//                            message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                            message.setMediaFilePath(photoPath);
                            mPathList.add(photoPath);
                            mMsgIdList.add(message.getMsgId());
                            String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
                            message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getUserName(), avatar));
                            mAdapter.addToStart(message, true);
                            msg.setOnSendCompleteCallback(new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        List<Message> lastMessage = new ArrayList<Message>();
                                        lastMessage.add(msg);
                                        localSaveMessage(lastMessage);
                                        message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                    } else {
                                        message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                    }
                                    MessageListActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.updateMessage(message);

                                        }
                                    });
                                }
                            });
                            JMessageClient.sendMessage(msg);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }


                    /*ImageContent.createImageContentAsync(bitmap, new ImageContent.CreateImageContentCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMessage, ImageContent imageContent) {
                            if (responseCode == 0) {
                                Message msg = chatRoomConversation.createSendMessage(imageContent);
//                                handleSendMsg(msg.getId());
                                LogUtils.d("handleSendMsg", photoPath);
                            }
                        }
                    });*/
                }
                break;
            case RequestCode.TAKE_VIDEO:
                if (data != null) {
                    String path = data.getStringExtra("video");
                    long videoDuration = data.getLongExtra("duration", 0);
                    final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
                    message.setDuration(videoDuration);
                    File videoFile = new File(path);
                    try {
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(path);
                        Bitmap bitmap = media.getFrameAtTime();
                        VideoContent video = new VideoContent(bitmap, "mp4", videoFile, videoFile.getName(), (int) videoDuration);
                        final Message msg = chatRoomConversation.createSendMessage(video);
//                            Message msg = chatRoomConversation.createSendFileMessage(videoFile, item.getFileName());
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setMediaFilePath(path);
                        message.setDuration(videoDuration);
                        String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
                        message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), avatar));
                        mAdapter.addToStart(message, true);
                        msg.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    List<Message> lastMessage = new ArrayList<Message>();
                                    lastMessage.add(msg);
                                    localSaveMessage(lastMessage);
                                    message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                } else {
                                    message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                }
                                MessageListActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.updateMessage(message);

                                    }
                                });
                            }
                        });
                        JMessageClient.sendMessage(msg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case RequestCode.PICK_IMAGE://1
                if (data == null) {
                    return;
                }
                boolean local = data.getBooleanExtra(Extras.EXTRA_FROM_LOCAL, false);
                if (local) {
                    // 本地相册
                    final String photoPath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                    Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                    final Message msg;
                    final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                    if (chatRoomConversation != null) {
                        try {
                            msg = chatRoomConversation.createSendImageMessage(new File(photoPath));
//                            message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                            message.setMediaFilePath(photoPath);
                            mPathList.add(photoPath);
                            mMsgIdList.add(message.getMsgId());
                            String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
                            message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getUserName(), avatar));
                            mAdapter.addToStart(message, true);
                            msg.setOnSendCompleteCallback(new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        List<Message> lastMessage = new ArrayList<Message>();
                                        lastMessage.add(msg);
                                        localSaveMessage(lastMessage);
                                        message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                    } else {
                                        message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                    }
                                    MessageListActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.updateMessage(message);
                                        }
                                    });
                                }
                            });
                            JMessageClient.sendMessage(msg);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case PhotoUtils.INTENT_SELECT:
                if (data == null) {
                    return;
                }
                Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String photoPath = cursor.getString(columnIndex);  //获取照片路径
                cursor.close();
                final Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                //个人中心换头像
                userHeadView.setImageBitmap(bitmap);
                //标题栏换头像
                ImageView toolBarUserHead = (ImageView) mTbMain.getUserHeadImg();
                toolBarUserHead.setImageBitmap(bitmap);
                SCUploadImgHelper helper = new SCUploadImgHelper();
                helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
                    @Override
                    public void onUploadSuc(List<String> urls) {
                        RoleManager.switchRoleCacheHeadImg(urls.get(0));
                        //更改头像
                        String characterInfo = SCCacheUtils.getCacheCharacterInfo();
                        CharacterInfo character = JSONObject.parseObject(characterInfo, CharacterInfo.class);
                        ModifyUserInfo modifyUserInfo = new ModifyUserInfo();
                        modifyUserInfo.setName(character.getName());
                        modifyUserInfo.setSignature(character.getSignature());
                        modifyUserInfo.setHeadImg(urls.get(0));
                        String modifyUser = JSONObject.toJSONString(modifyUserInfo);
//                        org.greenrobot.eventbus.EventBus.getDefault().postSticky(modifyUserInfo);
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
                                            String data = JSONObject.parseObject(response).getString("data");
                                            String signature = JSONObject.parseObject(data).getString("signature");
                                            String headImg = JSONObject.parseObject(data).getString("headImg");
                                            String name = JSONObject.parseObject(data).getString("name");
                                            String avatar = JSONObject.parseObject(data).getString("avatar");
                                            UserInfo jmUserInfo = JMessageClient.getMyInfo();
                                            CharacterInfo characterInfo = new CharacterInfo();
                                            characterInfo.setHeadImg(headImg);
//                                            characterInfo.setSignature(signature);
//                                            characterInfo.setName(name);
                                            String character = JSONObject.toJSONString(characterInfo);
                                            RoleManager.switchRoleCacheCharacterInfo(character);
                                            RoleManager.switchRoleCacheHeadImg(avatar);
                                        }
                                    }
                                });

                        File headFiel = null;
                        try {
                            Bitmap bitmap1 = ImageUtils.returnBitMap(urls.get(0));
                            headFiel = ImageUtils.saveUrlImgFile(bitmap, "head_img.jpg");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //调用极光更新头像
                        UserInfo userInfo = JMessageClient.getMyInfo();
                        JMessageClient.updateUserAvatar(headFiel, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                String s1 = s;
                            }
                        });
                    }

                    @Override
                    public void error() {
                        LogUtils.i("oss上传失败");
                    }
                });
                List list = new ArrayList();
                list.add(photoPath);
                helper.upLoadImg(mContext, list);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setIvUserModify(ModifyUserInfo modifyUserInfo) {
        isRestartActivity = modifyUserInfo.getRestartActivity();
        if (!TextUtils.isEmpty(modifyUserInfo.getName())) {
            userNikeView.setText(modifyUserInfo.getName());
        }
        if (!TextUtils.isEmpty(modifyUserInfo.getSignature())) {
            tvUserSign.setText(modifyUserInfo.getSignature());
        }

    }

    /*
     * 初始化弹窗
     *
     * */
    private void initTaskDialog() {
        SCHttpUtils.get()
                .url(HttpApi.GET_SEAT_CURRENCY)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String rate = JSONObject.parseObject(data).getString("rate");//汇率，获取当前汇率
                            String currency = JSONObject.parseObject(data).getString("currency");

                            seatRmbRate = Double.valueOf(rate);
                        }
                    }
                });

        final int[] idItems = new int[]{R.id.et_input_dialog_describe, R.id.et_input_dialog_bounty, R.id.dialog_select_task_time, R.id.btn_dialog_input_sure, R.id.iv_dialog_close};
        final CustomDialog dialog = new CustomDialog(MessageListActivity.this, false, 1.0, R.layout.common_dialog_chat_room_task, idItems);
        View layout = View.inflate(MessageListActivity.this, R.layout.common_dialog_chat_room_task, null);
        dialog.setView(layout);
        handler = new Handler() {
            @Override
            public void handleMessage(final android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                            @Override
                            public void OnItemClick(final CustomDialog dialog, View view) {
                                final EditText describeEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_describe);
                                final EditText bountyEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_bounty);
                                bountyEditText.setFocusable(true);
                                bountyEditText.setFocusableInTouchMode(true);
                                bountyEditText.requestFocus();
                                limitedTime = (TextView) dialog.getByIdView(R.id.dialog_select_task_time);
                                tvSeatRate = (TextView) dialog.getByIdView(R.id.seatRate);
                                tvSeatRate.setText("1 SEAT");
                                //输入框监听
                                bountyEditText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (s.toString().length() > 0) {
                                            float bounty = Float.parseFloat(s.toString());
                                            tvSeatRate.setText(("" + bounty / seatRmbRate) + " SEAT");
                                            bountyEditText.setText("￥" + s.toString());
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                switch (view.getId()) {
                                    case R.id.et_input_dialog_describe:
                                        ToastUtils.showToast(MessageListActivity.this, "输入任务描述");
                                        break;
                                    case R.id.et_input_dialog_bounty:
                                        ToastUtils.showToast(MessageListActivity.this, "输入赏金");
                                        break;
                                    case R.id.btn_dialog_input_sure:
                                        showLoadingDialog();
                                        releaseTask(dialog, describeEditText, bountyEditText, limitedTime);
                                        break;
                                    case R.id.iv_dialog_close:
                                        dialog.dismiss();
                                        break;
                                    //选择时间
                                    case R.id.dialog_select_task_time:
                                        initPickerView();
                                        scTimePickerView.setOnTimeSelectListener(onTimeSelectListener);
                                        scTimePickerView.setOnCancelClickListener(new SCTimePickerView.OnCancelClickListener() {
                                            @Override
                                            public void onCancelClick(View v) {

                                            }
                                        });
                                        scTimePickerView.show(limitedTime);
                                        break;
                                }
                            }
                        });
                        dialog.show();
                        break;
                }
            }
        };
    }

    //初始化时间选择弹窗
    private void initPickerView() {
        SCTimePickerView.Builder builder = new SCTimePickerView.Builder(MessageListActivity.this);
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        int year = startDate.get(Calendar.YEAR);
        startDate.set(year, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE));//设置起始年份
        Calendar endDate = Calendar.getInstance();
        endDate.set(year + 10, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE));//设置结束年份
        builder.setType(new boolean[]{true, true, true, true, false, false})//设置显示年、月、日、时、分、秒
                .setDecorView((ViewGroup) findViewById(android.R.id.content).getRootView())
//                .setDecorView((ViewGroup) dialog.getWindow().getDecorView().getRootView())
                .isCenterLabel(true)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setCancelText("清除")
                .setCancelColor(MessageListActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .setSubmitText("完成")
                .setRangDate(startDate, endDate)
                .setSubCalSize(14)
                .setTitleBgColor(MessageListActivity.this.getResources().getColor(com.shanchain.common.R.color.colorWhite))
                .setSubmitColor(MessageListActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .isDialog(true)
                .build();

        scTimePickerView = new SCTimePickerView(builder);
        scTimePickerView.setDate(Calendar.getInstance());

        onTimeSelectListener = new SCTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatDate = simpleDateFormat.format(date);
                timeStamp = date.getTime();
                LogUtils.d("SCTimePickerView", "点击了SCTimePickerView" + formatDate);
                TextView clickView = (TextView) v;
                clickView.setText(formatDate);
                scTimePickerView.show(limitedTime);
            }
        };
    }

    /*
     * 发布任务
     *
     * */
    private void releaseTask(final CustomDialog dialog, EditText describeEditText, EditText bountyEditText, TextView textViewTime) {
        if (TextUtils.isEmpty(describeEditText.getText().toString()) && TextUtils.isEmpty(bountyEditText.getText().toString()) && TextUtils.isEmpty(textViewTime.getText().toString())) {
            ToastUtils.showToast(MessageListActivity.this, getResources().getString(R.string.toast_no_empty));
        } else {
            final String spaceId = SCCacheUtils.getCacheSpaceId();//获取当前的空间ID
            final String bounty = bountyEditText.getText().toString();
            final String dataString = describeEditText.getText().toString();
            final String LimitedTtime = textViewTime.getText().toString();

            final String characterId = SCCacheUtils.getCacheCharacterId();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            final String createTime = simpleDateFormat.format(LimitedTtime);
            //向服务器请求添加任务
//          taskPresenter.releaseTask(characterId, String.valueOf(roomID), bounty, dataString, timeStamp);

            SCHttpUtils.postWithUserId()
                    .url(HttpApi.CHAT_TASK_ADD)
                    .addParams("authCode", SCCacheUtils.getCacheAuthCode() + "")
                    .addParams("deviceToken", registrationId + "")
                    .addParams("characterId", characterId + "")
                    .addParams("price", bounty)
                    .addParams("roomId", roomID + "")
                    .addParams("dataString", dataString + "") //任务内容
                    .addParams("time", timeStamp + "")
                    .build()
                    .execute(new SCHttpStringCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.d("TaskPresenterImpl", "添加任务失败");
                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                            MyMessage myMessage = new MyMessage(IMessage.MessageType.EVENT.ordinal());
                            myMessage.setChatEventMessage(chatEventMessage1);
                            mAdapter.addToStart(myMessage, true);
                            closeLoadingDialog();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            dialog.dismiss();
                            closeLoadingDialog();
                            String code = JSONObject.parseObject(response).getString("code");
                            final String message = JSONObject.parseObject(response).getString("message");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                String publishTime = JSONObject.parseObject(data).getString("PublishTime");
                                String task = JSONObject.parseObject(data).getString("Task");
                                chatEventMessage1 = JSONObject.parseObject(task, ChatEventMessage.class);
                                Map customMap = new HashMap();
                                customMap.put("taskId", chatEventMessage1.getTaskId() + "");
                                customMap.put("bounty", chatEventMessage1.getBounty() + "");
                                customMap.put("dataString", chatEventMessage1.getIntro());
                                customMap.put("time", timeStamp + "");

                                Message sendCustomMessage = chatRoomConversation.createSendCustomMessage(customMap);
                                final MyMessage myMessage = new MyMessage();
                                mAdapter.addToStart(myMessage, true);
                                sendCustomMessage.setOnSendCompleteCallback(new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        String s1 = s;
                                        if (0 == i) {
                                            Toast.makeText(MessageListActivity.this, "发送任务消息成功", Toast.LENGTH_SHORT);
                                            LogUtils.d("发送任务消息", "code: " + i + " 回调信息：" + s);
                                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                            myMessage.setChatEventMessage(chatEventMessage1);
                                        } else {
                                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                            myMessage.setChatEventMessage(chatEventMessage1);
                                        }
                                        MessageListActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAdapter.updateMessage(myMessage);
                                            }
                                        });
                                    }
                                });
                                JMessageClient.sendMessage(sendCustomMessage);

//                                chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//                                mAdapter.addToStart(chatEventMessage1, true);
                                dialog.dismiss();
                            } else if (code.equals("10001")) {
                                dialogHandler.sendEmptyMessage(1);
                                dialog.dismiss();
                                //余额不足
                                dialogHandler.sendEmptyMessage(1);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MessageListActivity.this, "您的钱包余额不足", Toast.LENGTH_SHORT);

                                    }
                                });
                            } else {
                                dialogHandler.sendEmptyMessage(2);
                                dialog.dismiss();
                                dialogHandler.sendEmptyMessage(2);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MessageListActivity.this, "错误消息" + message, Toast.LENGTH_SHORT);
                                    }
                                });
                            }
                        }
                    });
        }
    }

    private void initToolBar(final String roomId) {
        mTbMain = (ArthurToolBar) findViewById(R.id.tb_main);
        mTbMain.setTitleText(roomName);//聊天室名字
        mTbMain.setTitleTextSize(14);
//        mTbMain.setLeftImage(R.mipmap.my_info);
        final UserInfo userInfo = JMessageClient.getMyInfo();
        if (userInfo != null && userInfo.getAvatarFile() != null) {
            mTbMain.setUserHeadImg(MessageListActivity.this, userInfo.getAvatarFile().getAbsolutePath());
        } else {
            mTbMain.setUserHeadImg(MessageListActivity.this, SCCacheUtils.getCacheHeadImg());
        }

        mTbMain.setFavoriteImage(R.mipmap.share);
        mTbMain.setRightImage(R.mipmap.fb_close);
        mTbMain.isShowChatRoom(true);
        mTbMain.setOnRightClickListener(this);
        mTbMain.isShowChatRoom(true);//显示聊天室成员信息
        roomNum = mTbMain.findViewById(R.id.mRoomNum);
        roomNum.setTextColor(getResources().getColor(R.color.colorViolet));
        relativeChatRoom = mTbMain.findViewById(R.id.relative_chatRoom);
        //获取聊天室信息
        final Set<Long> roomIds = new HashSet();
        final long chatRoomId = Long.valueOf(roomId);
        roomIds.add(chatRoomId);
        ChatRoomManager.getChatRoomInfos(roomIds, new RequestCallback<List<ChatRoomInfo>>() {
            @Override
            public void gotResult(int i, String s, List<ChatRoomInfo> chatRoomInfos) {
                if (i == 0) {
                    int totalMember = chatRoomInfos.get(0).getTotalMemberCount();
                    memberCount = totalMember;
                    if (roomName.equals("加密聊天社区")) {
                        roomNum.setText("16968");
                    } else {
                        roomNum.setText("" + memberCount);
                    }
                }
            }
        });

        relativeChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showToast(MessageListActivity.this, "群成员列表");

                Intent intent = new Intent(MessageListActivity.this, MemberActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("count", memberCount);
                startActivity(intent);
            }
        });

        //分享社区
        mTbMain.setOnFavoriteClickListener(new ArthurToolBar.OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(View v) {
                final HotChatRoom hotChatRoom = getIntent().getParcelableExtra("hotChatRoom");
                if (isHotChatRoom == false) {
                    EventMessage eventMessage = new EventMessage(RequestCode.SCREENSHOT);
                    org.greenrobot.eventbus.EventBus.getDefault().post(eventMessage);
                    finish();
                } else {
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = ImageUtils.returnBitMap(hotChatRoom.getThumbnails());
                            captureScreenFile = ImageUtils.saveUrlImgFile(
                                    bitmap, "shareHotRoom.png");
                            SCHttpUtils.postWithUserId()
                                    .url(HttpApi.SHARE_CHAT_ROOM)
                                    .addParams("characterId", SCCacheUtils.getCacheCharacterId() + "")
                                    .addParams("Img", hotChatRoom.getThumbnails() + "")
                                    .addParams("id", roomID + "")
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            closeLoadingDialog();
                                            LogUtils.d("网络异常");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            closeLoadingDialog();
                                            String code = JSONObject.parseObject(response).getString("code");
                                            if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                                                String data = JSONObject.parseObject(response).getString("data");
                                                final String chatRoomShareType = JSONObject.parseObject(data).getString("ShareType");
                                                String characterId = JSONObject.parseObject(data).getString("characterId");
                                                final String intro = JSONObject.parseObject(data).getString("intro");
                                                String background = JSONObject.parseObject(data).getString("background");
                                                final String url = JSONObject.parseObject(data).getString("url");
                                                final String title = JSONObject.parseObject(data).getString("title");
                                                ThreadUtils.runOnMainThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //分享参数
                                                        shareParams = new ShareParams();
                                                        shareChatRoomDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(MessageListActivity.this, true, true, 1.0, R.layout.layout_bottom_share, new int[]{R.id.share_image, R.id.mRlWechat, R.id.mRlWeixinCircle, R.id.mRlQQ, R.id.mRlWeibo, R.id.share_close});
                                                        shareChatRoomDialog.setViewId(R.id.share_image);
                                                        shareChatRoomDialog.setShareBitmap(ImageUtils.getBitmapByFile(captureScreenFile));
                                                        shareChatRoomDialog.show();
                                                        shareChatRoomDialog.setOnItemClickListener(new com.shanchain.data.common.ui.widgets.CustomDialog.OnItemClickListener() {
                                                            @Override
                                                            public void OnItemClick(com.shanchain.data.common.ui.widgets.CustomDialog dialog, View view) {
                                                                switch (view.getId()) {
                                                                    case R.id.mRlWechat:
//                                                                    if (chatRoomShareType.equals("SHARE_WEBPAGE")) {
                                                                        shareParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                        String absolutePath = captureScreenFile.getAbsolutePath();
                                                                        shareParams.setImagePath(absolutePath);
                                                                        shareParams.setText(hotChatRoom.getRoomName());
                                                                        shareParams.setTitle(title);
                                                                        shareParams.setUrl(url);
                                                                        //调用分享接口share ，分享到微信平台。
                                                                        JShareInterface.share(Wechat.Name, shareParams, mPlatActionListener);
                                                                        closeLoadingDialog();
//                                                                    }
                                                                        break;
                                                                    case R.id.mRlWeixinCircle:
                                                                        shareParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                        shareParams.setImagePath(captureScreenFile.getAbsolutePath());
                                                                        shareParams.setText(hotChatRoom.getRoomName());
                                                                        shareParams.setTitle(title);
                                                                        shareParams.setUrl(url);
                                                                        //调用分享接口share ，分享到朋友圈平台。
                                                                        JShareInterface.share(WechatMoments.Name, shareParams, mPlatActionListener);
                                                                        break;
                                                                    case R.id.mRlQQ:
                                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                            ToastUtils.showToastLong(MessageListActivity.this, "暂时不支持安卓8.0系统版本分享");
                                                                        } else {
                                                                            shareParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                            shareParams.setImagePath(captureScreenFile.getAbsolutePath());
                                                                            shareParams.setTitle(title);
                                                                            shareParams.setText(hotChatRoom.getRoomName());
                                                                            shareParams.setUrl(url);
                                                                            //调用分享接口share ，分享到QQ平台。
                                                                            JShareInterface.share(QQ.Name, shareParams, mPlatActionListener);
                                                                        }
                                                                        break;
                                                                    case R.id.mRlWeibo:
                                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                            ToastUtils.showToastLong(MessageListActivity.this, "暂时不支持安卓8.0系统版本分享");
                                                                        } else {
                                                                            shareParams.setShareType(Platform.SHARE_WEBPAGE);
                                                                            shareParams.setImagePath(captureScreenFile.getAbsolutePath());
                                                                            shareParams.setText(hotChatRoom.getRoomName());
                                                                            shareParams.setTitle(title);
                                                                            shareParams.setUrl(url);
                                                                            //调用分享接口share ，分享到新浪微博平台。
                                                                            JShareInterface.share(SinaWeibo.Name, shareParams, mPlatActionListener);
                                                                        }
                                                                        break;
                                                                    case R.id.share_close:
                                                                        shareChatRoomDialog.dismiss();
                                                                        break;
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    });


                }

//                Rect rect = new Rect()
//                HomeActivity.baiduMap.snapshotScope();
            }
        });
    }

    @SuppressLint("InvalidWakeLockTag")
    private void registerProximitySensorListener() {
        try {
            mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        try {
            if (audioManager.isBluetoothA2dpOn() || audioManager.isWiredHeadsetOn()) {
                return;
            }
            if (mAdapter.getMediaPlayer() == null) return;
            if (mAdapter.getMediaPlayer().isPlaying()) {
                float distance = event.values[0];
                if (distance >= mSensor.getMaximumRange()) {
                    mAdapter.setAudioPlayByEarPhone(0);
                    setScreenOn();
                } else {
                    mAdapter.setAudioPlayByEarPhone(2);
                    ViewHolderController.getInstance().replayVoice();
                    setScreenOff();
                }
            } else {
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                    mWakeLock = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setScreenOn() {
        if (mWakeLock != null) {
            mWakeLock.setReferenceCounted(false);
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private void setScreenOff() {
        if (mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
        }
        mWakeLock.acquire();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //标题栏右侧按钮实现
    @Override
    public void onRightClick(final View v) {
        StandardDialog standardDialog = new StandardDialog(this);
        standardDialog.setStandardMsg("");
        standardDialog.setTitle(getResources().getString(R.string.str_dialog_title));
        //第一个回调是确认，第二个是取消
        standardDialog.setCallback(new Callback() {
            @Override
            public void invoke() {
                readyGo(FootPrintActivity.class);
            }
        }, new Callback() {
            @Override
            public void invoke() {

            }
        });
        standardDialog.show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (EmoticonsKeyboardUtils.isFullScreen(this)) {
            boolean isConsum = xhsEmoticonsKeyBoard.dispatchKeyEventInFullScreen(event);
            return isConsum ? isConsum : super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        closeLoadingDialog();
//        ChatRoomManager.leaveChatRoom(Long.valueOf(roomID), new BasicCallback() {
//            @Override
//            public void gotResult(int i, String s) {
//                int i1 = i;
//                String s1 = s;
//                LogUtils.d("leaveChatRoom", "离开聊天室 code" + i);
//            }
//        });
        if (mShowSoftInput) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(xhsEmoticonsKeyBoard.getEtChat().getWindowToken(), 0);
                mShowSoftInput = false;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //侧滑栏按钮实现
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_my_wallet) {
            Intent intent = new Intent(mContext, com.shanchain.shandata.rn.activity.SCWebViewActivity.class);
            JSONObject obj = new JSONObject();
            obj.put("url", HttpApi.SEAT_WALLET);
            obj.put("title", getResources().getString(R.string.nav_my_wallet));
            String webParams = obj.toJSONString();
            intent.putExtra("webParams", webParams);
            startActivity(intent);
        } else if (id == R.id.nav_my_coupon) {
            Intent intent = new Intent(MessageListActivity.this, MyCouponListActivity.class);
            intent.putExtra("roomId", roomID);
            startActivity(intent);
        } else if (id == R.id.nav_my_task) {
            Intent intent = new Intent(MessageListActivity.this, TaskListActivity.class);
            intent.putExtra("roomId", roomID);
            startActivity(intent);

        } else if (id == R.id.nav_my_message) {
            readyGo(MyMessageActivity.class);

        } else if (id == R.id.nav_my_favorited) {
            readyGo(FootPrintActivity.class);

        } else if (id == R.id.real_identity) {
//            readyGo(VerifiedActivity.class);

        } else if (id == R.id.nav_setting) {
            readyGo(SettingsActivity.class);
        }
//        else if (id == R.id.nav_logout) {
//            JMessageClient.logout();
//            readyGoThenKill(LoginActivity.class);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class HeadsetDetectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                if (intent.hasExtra("state")) {
                    int state = intent.getIntExtra("state", 0);
                    mAdapter.setAudioPlayByEarPhone(state);
                }
            }
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    //从服务器接口获取消息
    private void getChatRoomMessage(String roomID) {
        SCHttpUtils.postWithUserId()
                .url(HttpApi.CHAT_ROOM_HISTORY_MESSAGE)
                .addParams("roomId", roomID + "")
                .addParams("timeStamp", System.currentTimeMillis() + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d("网络错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (code.equals(NetErrCode.COMMON_SUC_CODE)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            String total = JSONObject.parseObject(data).getString("total");
                            String count = JSONObject.parseObject(data).getString("count");
                            String cursor = JSONObject.parseObject(data).getString("cursor");
                            LogUtils.d(TAG, "获取聊天室历史消息 total: " + total + " count: " + count + " cursor: " + cursor);
                            String messages = JSONObject.parseObject(data).getString("messages");
                            List<MessageModel> messageModels = JSONObject.parseArray(messages, MessageModel.class);
                            for (int i = 0; i < messageModels.size(); i++) {
                                final MessageModel messageModel = messageModels.get(i);
//                                String mediaID = messageModel.getMsgBody().getMediaId();
                                LogUtils.d("getMsgType", messageModel.getMsgType() + "");
                                final MyMessage myMessage = new MyMessage(messageModel.getMsgBody().getText(), IMessage.MessageType.RECEIVE_TEXT.ordinal());
                                String fromId = messageModel.getFromId(); //
                                String userName = JMessageClient.getMyInfo().getUserName();//用户名
                                final int msgId = messageModel.getMsgId();
                                if (i > 0) {
                                    long messageTime = messageModel.getCreateTime();
                                    long preTime = messageModels.get(i - 1).getCreateTime();
                                    long diff = messageTime - preTime;
                                    //显示消息时间间隔3分钟，3分钟内不显示发送消息的时间
                                    if (diff > 3 * 60 * 1000) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        String timeString = sdf.format(new Date(messageTime));
                                        myMessage.setTimeString(timeString);
                                    }
                                }
                                if (fromId.equals(userName)) {
                                    myMessage.setType(IMessage.MessageType.SEND_TEXT.ordinal());
                                    JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
                                        @Override
                                        public void gotResult(int i, String s, UserInfo userInfo) {
                                            //获取头像
                                            String avatar = userInfo.getAvatarFile().getAbsolutePath() != null ?
                                                    userInfo.getAvatarFile().getAbsolutePath() : "";
                                            String displayName = userInfo.getDisplayName() != null ?
                                                    userInfo.getDisplayName() : userInfo.getUserName();
                                            DefaultUser defaultUser = new DefaultUser(userInfo.getUserID(), displayName, avatar);
                                            defaultUser.setSignature(userInfo.getSignature().length() > 0 ? userInfo.getSignature() : "该用户很懒，没有设置签名");
                                            defaultUser.setHxUserId(userInfo.getUserName() + "");
                                            myMessage.setUserInfo(defaultUser);
                                            mAdapter.updateMessage(myMessage);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                } else {
                                    JMessageClient.getUserInfo(fromId, new GetUserInfoCallback() {
                                        @Override
                                        public void gotResult(int i, String s, UserInfo userInfo) {
                                            //获取头像
                                            String avatar = userInfo.getAvatarFile().getAbsolutePath() != null ?
                                                    userInfo.getAvatarFile().getAbsolutePath() : "";
                                            String displayName = userInfo.getDisplayName() != null ?
                                                    userInfo.getDisplayName() : userInfo.getUserName();
                                            DefaultUser defaultUser = new DefaultUser(userInfo.getUserID(), displayName, avatar);
                                            defaultUser.setSignature(userInfo.getSignature().length() > 0 ? userInfo.getSignature() : "该用户很懒，没有设置签名");
                                            defaultUser.setHxUserId(userInfo.getUserName() + "");
                                            myMessage.setUserInfo(defaultUser);
                                            mAdapter.updateMessage(myMessage);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                                switch (messageModel.getMsgType()) {
                                    case "text":
                                        myMessage.setText(messageModel.getMsgBody().getText());
                                        messageList.add(myMessage);
                                        mAdapter.addToStart(myMessage, true);
                                        break;
                                    case "image":
                                        String mediaID = messageModel.getMsgBody().getMediaId();
                                        LogUtils.d("ImageMediaID", mediaID + "");
//                                        mPathList.add(file.getAbsolutePath());
//                                        mMsgIdList.add(messageModel.getMsgId() + "");
//                                        myMessage.setMediaFilePath(mediaID);
//                                        mAdapter.updateMessage(myMessage);
//                                        mAdapter.notifyDataSetChanged();
//                                        if (imageContent.getLocalPath() == null) {
//                                            imageContent.downloadOriginImage(msg, new DownloadCompletionCallback() {
//                                                @Override
//                                                public void onComplete(int i, String s, File file) {
//                                                    mPathList.add(file.getAbsolutePath());
//                                                    mMsgIdList.add(imgMessage.getMsgId() + "");
//                                                    imgMessage.setMediaFilePath(file.getAbsolutePath());
//                                                    mAdapter.updateMessage(imgMessage);
//                                                }
//                                            });
//                                        } else {
//                                            mPathList.add(imageContent.getLocalPath());
//                                            mMsgIdList.add(imgMessage.getMsgId() + "");
//                                            imgMessage.setMediaFilePath(imageContent.getLocalThumbnailPath());
//                                        }
                                        break;
                                    case "voice":
                                        break;
                                    case "video":
                                        break;
                                    case "custom":
                                        break;
                                    case "file":
                                        break;
                                }
                            }
                        }
                    }
                });
    }

    //从本地数据库读取聊天消息
    private void loadMessageData(String roomID) {
        mEntryDao = MyApplication.getDaoSession().getMessageEntryDao();
        mMessageEntryList = mEntryDao._queryConversationEntry_MMessageEntryList(roomID);
        messageList.clear();
        if (mMessageEntryList.size() != 0) {
            for (int i = 0; i < mMessageEntryList.size(); i++) {
                MessageEntry messageEntry = mMessageEntryList.get(i);
                final MyMessage commonMessage = new MyMessage(IMessage.MessageType.RECEIVE_TEXT.ordinal());
                //创建用户获取头像
                String avatar = messageEntry.getAvatar() != null ? messageEntry.getAvatar() : "";
                final DefaultUser defaultUser = new DefaultUser(messageEntry.getUserId(), messageEntry.getDisplayName(), avatar);
                defaultUser.setHxUserId(messageEntry.getJgUserName() + "");
                //从极光获取用户信息
                JMessageClient.getUserInfo(messageEntry.getJgUserName() + "", new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        defaultUser.setSignature(userInfo.getSignature() + "");
                    }
                });
                //设置消息显示时间
                if (i > 0) {
                    long messageTime = messageEntry.getTimeString();
                    long preTime = mMessageEntryList.get(i - 1).getTimeString();
                    long diff = messageTime - preTime;
                    if (diff > 3 * 60 * 1000) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String timeString = sdf.format(new Date(messageTime));
                        commonMessage.setTimeString(timeString);
                    }
                }
                switch (messageEntry.getMessageType()) {
                    case "text":
                        commonMessage.setText(messageEntry.getMessageText());
                        long id = messageEntry.getUserId();
                        long currentId = JMessageClient.getMyInfo().getUserID();
                        if (currentId == id) {
                            commonMessage.setType(IMessage.MessageType.SEND_TEXT.ordinal());
                        }
                        commonMessage.setUserInfo(defaultUser);
                        messageList.add(commonMessage);
                        mAdapter.addToStart(commonMessage, true);
                        break;
                    case "image":
                        long id_img = messageEntry.getUserId();
                        long currentId_img = JMessageClient.getMyInfo().getUserID();
                        if (currentId_img == id_img) {
                            commonMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
                        } else {
                            commonMessage.setType(IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                        }
                        commonMessage.setUserInfo(defaultUser);
                        commonMessage.setMediaFilePath(messageEntry.getMediaFilePath() + "");
                        mPathList.add(messageEntry.getMediaFilePath());
                        mMsgIdList.add(commonMessage.getMsgId() + "");
                        messageList.add(commonMessage);
                        mAdapter.addToStart(commonMessage, true);
                        break;
                    case "voice":
                        long id_voice = messageEntry.getUserId();
                        long currentId_voice = JMessageClient.getMyInfo().getUserID();
                        if (currentId_voice == id_voice) {
                            commonMessage.setType(IMessage.MessageType.SEND_VOICE.ordinal());
                        } else {
                            commonMessage.setType(IMessage.MessageType.RECEIVE_VOICE.ordinal());
                        }
                        commonMessage.setUserInfo(defaultUser);
                        commonMessage.setDuration(messageEntry.getDuration());
                        commonMessage.setMediaFilePath(messageEntry.getMediaFilePath() + "");
                        messageList.add(commonMessage);
                        mAdapter.addToStart(commonMessage, true);
                        break;
                    case "video":
                        long id_video = messageEntry.getUserId();
                        long currentId_video = JMessageClient.getMyInfo().getUserID();
                        if (currentId_video == id_video) {
                            commonMessage.setType(IMessage.MessageType.SEND_VOICE.ordinal());
                        } else {
                            commonMessage.setType(IMessage.MessageType.RECEIVE_VOICE.ordinal());
                        }
                        commonMessage.setUserInfo(defaultUser);
                        commonMessage.setDuration(messageEntry.getDuration());
                        commonMessage.setMediaFilePath(messageEntry.getMediaFilePath() + "");
                        messageList.add(commonMessage);
                        mAdapter.addToStart(commonMessage, true);
                        break;
                    case "file":
                        long idFile = messageEntry.getUserId();
                        long currentIdFile = JMessageClient.getMyInfo().getUserID();
                        if (currentIdFile == idFile) {
                            commonMessage.setType(IMessage.MessageType.SEND_FILE.ordinal());
                        } else {
                            commonMessage.setType(IMessage.MessageType.RECEIVE_FILE.ordinal());
                        }
                        loadFileTypeMessage(messageEntry, commonMessage);
                        break;
                    default:
                        mData = messageList;
                        break;
                }
            }
        }
    }

    private void loadFileTypeMessage(MessageEntry messageEntry, MyMessage commonMessage) {
        switch (messageEntry.getFileFormat()) {
            case "jpg":
                long fileId0 = messageEntry.getUserId();
                long currentFileId0 = JMessageClient.getMyInfo().getUserID();
                if (currentFileId0 == fileId0) {
                    commonMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
                }
                if (!TextUtils.isEmpty(messageEntry.getMediaFilePath())) {
                    commonMessage.setMediaFilePath(messageEntry.getMediaFilePath() + "");
                    mPathList.add(messageEntry.getMediaFilePath());
                    mMsgIdList.add(commonMessage.getMsgId() + "");
                } else {

                }

                break;
            case "mp4":
                commonMessage.setType(IMessage.MessageType.RECEIVE_VIDEO.ordinal());
                long fileId1 = messageEntry.getUserId();
                long currentFileId1 = JMessageClient.getMyInfo().getUserID();
                if (currentFileId1 == fileId1) {
                    commonMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
                }
                if (!TextUtils.isEmpty(messageEntry.getMediaFilePath())) {
                    commonMessage.setMediaFilePath(messageEntry.getMediaFilePath());
                    commonMessage.setDuration(messageEntry.getDuration());
                } else {

                }
                break;
            case "png":
                long fileId2 = messageEntry.getUserId();
                long currentFileId2 = JMessageClient.getMyInfo().getUserID();
                commonMessage.setType(IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                if (currentFileId2 == fileId2) {
                    commonMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
                }
                if (!TextUtils.isEmpty(messageEntry.getMediaFilePath())) {
                    commonMessage.setMediaFilePath(messageEntry.getMediaFilePath());
                    mPathList.add(messageEntry.getMediaFilePath());
                    mMsgIdList.add(commonMessage.getMsgId() + "");
                } else {

                }
                break;
            case "mp3":
                commonMessage.setType(IMessage.MessageType.RECEIVE_VIDEO.ordinal());
                long fileId3 = messageEntry.getUserId();
                long currentFileId3 = JMessageClient.getMyInfo().getUserID();
                if (currentFileId3 == fileId3) {
                    commonMessage.setType(IMessage.MessageType.SEND_VOICE.ordinal());
                }
                if (!TextUtils.isEmpty(messageEntry.getMediaFilePath())) {
                    commonMessage.setMediaFilePath(messageEntry.getMediaFilePath());
                    commonMessage.setDuration(messageEntry.getDuration());
                } else {

                }
            default:
                messageList.add(commonMessage);
                mAdapter.addToStart(commonMessage, true);
                break;
        }
    }

    //保存文件消息
    private void localSaveFileMessage(Message message, final MessageEntry messageEntry, final MessageEntryDao entryDao) {
        entryDao.insertOrReplace(messageEntry);
        FileContent fileContent = (FileContent) message.getContent();
        fileContent.downloadFile(message, new DownloadCompletionCallback() {
            @Override
            public void onComplete(int i, String s, File file) {
                messageEntry.setMediaFilePath(file.getAbsolutePath());
                entryDao.update(messageEntry);
            }
        });
        switch (fileContent.getFormat()) {
            case "jpg":
                final ImageContent imageContent = (ImageContent) message.getContent();
                messageEntry.setFileFormat("jpg");
                if (imageContent.getLocalPath() == null) {
                    imageContent.downloadOriginImage(message, new DownloadCompletionCallback() {
                        @Override
                        public void onComplete(int i, String s, File file) {
                            messageEntry.setMediaFilePath(file.getAbsolutePath());
                        }
                    });
                } else {
                    messageEntry.setMediaFilePath(imageContent.getLocalPath());
                }
                break;
            case "png":
                final ImageContent pngContent = (ImageContent) message.getContent();
                messageEntry.setFileFormat("png");
                if (pngContent.getLocalPath() == null) {
                    pngContent.downloadOriginImage(message, new DownloadCompletionCallback() {
                        @Override
                        public void onComplete(int i, String s, File file) {
                            messageEntry.setMediaFilePath(file.getAbsolutePath());
                        }
                    });
                } else {
                    messageEntry.setMediaFilePath(pngContent.getLocalPath());
                }
                break;
            case "mp4":
                final VoiceContent voiceContent = (VoiceContent) message.getContent();
                messageEntry.setFileFormat("mp4");
                if (voiceContent.getLocalPath() != null) {
                    messageEntry.setMediaFilePath(voiceContent.getLocalPath());
                    messageEntry.setDuration(voiceContent.getDuration());
                } else {
                    voiceContent.downloadVoiceFile(message, new DownloadCompletionCallback() {
                        @Override
                        public void onComplete(int i, String s, File file) {
                            messageEntry.setMediaFilePath(file.getAbsolutePath());
                            messageEntry.setDuration(voiceContent.getDuration());
                        }
                    });
                }
                break;
            case "mp3":
                final VideoContent videoContent = (VideoContent) message.getContent();
                messageEntry.setFileFormat("mp3");
                if (videoContent.getVideoLocalPath() != null) {
                    messageEntry.setMediaFilePath(videoContent.getVideoLocalPath());
                    messageEntry.setDuration(videoContent.getDuration());
                } else {
                    videoContent.downloadVideoFile(message, new DownloadCompletionCallback() {
                        @Override
                        public void onComplete(int i, String s, File file) {
                            messageEntry.setMediaFilePath(file.getAbsolutePath());
                            messageEntry.setDuration(videoContent.getDuration());
                        }
                    });
                }
                break;
        }

    }

    //本地存储聊天室消息
    private void localSaveMessage(List<Message> mMsgs) {

        mMessageEntryDao = MyApplication.getDaoSession().getMessageEntryDao();
        for (int i = 0; i < mMsgs.size(); i++) {
            Message chatMessage = mMsgs.get(i);
            //
            if (!chatMessage.getTargetID().equals(roomID)) {
                return;
            }
            //用户极光ID,userId,displayName,头像
            Long userId = chatMessage.getFromUser().getUserID();
            String jgUserName = chatMessage.getFromUser().getUserName();
            String displayName = chatMessage.getFromUser().getNickname();
            String headImg = chatMessage.getFromUser().getAvatarFile() != null ?
                    chatMessage.getFromUser().getAvatarFile().getAbsolutePath() : " ";
            long msgId = chatMessage.getServerMessageId();
//            LogUtils.d("chatMessageId", msgId + "");
            //构造存储消息体
            final MessageEntry messageEntry = new MessageEntry();
            String messageId = chatMessage.getTargetID() + "_" + msgId;//构造msgId
            messageEntry.setMsgId(messageId);
            messageEntry.setRoomId(chatMessage.getTargetID() + "");
            messageEntry.setUserId(userId);//用户Id
            messageEntry.setJgUserName(jgUserName);//极光ID
            messageEntry.setDisplayName(displayName);//昵称
            messageEntry.setAvatar(headImg);//头像
            messageEntry.setTimeString(chatMessage.getCreateTime());//时间
            switch (chatMessage.getContentType()) {
                case text:
                    TextContent textContent = (TextContent) chatMessage.getContent();
                    messageEntry.setMessageType("text");
                    messageEntry.setMessageText(textContent.getText());
                    if (!mMessageEntryDao.hasKey(messageEntry)) {
                        mMessageEntryDao.insertOrReplace(messageEntry);
                    }
                    break;
                case image:
                    final ImageContent imageContent = (ImageContent) chatMessage.getContent();
                    messageEntry.setMessageType("image");
                    if (imageContent.getLocalPath() == null) {
                        imageContent.downloadOriginImage(chatMessage, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int i, String s, File file) {
                                LogUtils.d("downloadOriginImage", file.getAbsolutePath() + "");
                                messageEntry.setMediaFilePath(file.getAbsolutePath());
                                mMessageEntryDao.update(messageEntry);
                            }
                        });
                        if (!mMessageEntryDao.hasKey(messageEntry)) {
                            mMessageEntryDao.insertOrReplace(messageEntry);
                        }
                    } else {
                        LogUtils.d("getLocalPath", imageContent.getLocalPath() + "");
                        messageEntry.setMediaFilePath(imageContent.getLocalPath());
                        if (!mMessageEntryDao.hasKey(messageEntry)) {
                            mMessageEntryDao.insertOrReplace(messageEntry);
                        }
                    }
                    break;
                case voice:
                    final VoiceContent voiceContent = (VoiceContent) chatMessage.getContent();
                    messageEntry.setMessageType("voice");
                    if (voiceContent.getLocalPath() != null) {
                        messageEntry.setMediaFilePath(voiceContent.getLocalPath());
                        messageEntry.setDuration(voiceContent.getDuration());
                        if (!mMessageEntryDao.hasKey(messageEntry)) {
                            mMessageEntryDao.insertOrReplace(messageEntry);
                        }
                    } else {
                        voiceContent.downloadVoiceFile(chatMessage, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int i, String s, File file) {
                                messageEntry.setMediaFilePath(file.getAbsolutePath());
                                messageEntry.setDuration(voiceContent.getDuration());
                                mMessageEntryDao.update(messageEntry);
                            }
                        });
                        if (!mMessageEntryDao.hasKey(messageEntry)) {
                            mMessageEntryDao.insertOrReplace(messageEntry);
                        }
                    }
                    break;
                case video:
                    final VideoContent videoContent = (VideoContent) chatMessage.getContent();
                    messageEntry.setMessageType("video");
                    if (videoContent.getVideoLocalPath() != null) {
                        messageEntry.setMediaFilePath(videoContent.getVideoLocalPath());
                        messageEntry.setDuration(videoContent.getDuration());
                        if (!mMessageEntryDao.hasKey(messageEntry)) {
                            mMessageEntryDao.insertOrReplace(messageEntry);
                        }
                    } else {
                        videoContent.downloadVideoFile(chatMessage, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int i, String s, File file) {
                                messageEntry.setMediaFilePath(file.getAbsolutePath());
                                messageEntry.setDuration(videoContent.getDuration());
                                mMessageEntryDao.update(messageEntry);
                            }
                        });
                        if (!mMessageEntryDao.hasKey(messageEntry)) {
                            mMessageEntryDao.insertOrReplace(messageEntry);
                        }
                    }
                    break;
                case file:
                    messageEntry.setMessageType("file");
                    localSaveFileMessage(chatMessage, messageEntry, mMessageEntryDao);
                    break;
                case location:
                    messageEntry.setMessageType("location");
                    break;
                case custom:
                    messageEntry.setMessageType("custom");
                    break;
                case unknown:
                    messageEntry.setMessageType("unknown");
                    break;
                default:
                    LogUtils.d("messageEntry", "第" + i + "个本地缓存消息：" + messageEntry.getJgUserName());
                    break;
            }
        }
    }

    // 接收聊天室消息
    public void onEventMainThread(ChatRoomMessageEvent event) {
        Log.d("tag", "ChatRoomMessageEvent received .");
        chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
        List<Message> chatRoomConversationAllMessage = chatRoomConversation.getAllMessage();
//        mEntryDao = MyApplication.getDaoSession().getMessageEntryDao();
        mMessageEntryList = mEntryDao._queryConversationEntry_MMessageEntryList(roomID);
        mMsgs = event.getMessages();
        final MyMessage myMessage;
        //打印消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mMsgs.size(); i++) {
                    Message msg = mMsgs.get(i);
//                    LogUtils.d("ChatRoomMessageEvent message", "第" + i + "个" + msg.getContent().toJson().toString());
                }
                //本地存储聊天室消息
                localSaveMessage(mMsgs);
            }
        }).start();
        if (mMsgs.size() > 1) {
            mEventMessageList.clear();
        }
        //加载消息
        MyMessage commonMessage = new MyMessage(IMessage.MessageType.RECEIVE_TEXT.ordinal());
//        if (mConversationEntry.getMessageEntry() != null) {
////            MessageEntry.deleteMessage(3);
//            List<MessageEntry> entryList = mConversationEntry.getMessageEntry();
//            for (int i = 0; i < entryList.size(); i++) {
//                MessageEntry messageEntry = entryList.get(i);
//                LogUtils.d("mConversationEntry", i + "本地缓存：" + messageEntry.getMessageId());
////                DefaultUser defaultUser = new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), avatar);
////                defaultUser.setSignature(msg.getFromUser().getSignature().length() >= 0 ? msg.getFromUser().getSignature() : "该用户很懒，没有设置签名");
////                defaultUser.setHxUserId(msg.getFromID() + "");
//            }
//        }
        //装载聊天消息
        for (int i = 0; i < mMsgs.size(); i++) {
            final Message msg = mMsgs.get(i);
            if (msg.getTargetID().equals(roomID)) {
                LogUtils.d("TargetID_RoomId", "roomID:" + roomID + " TargetID:" + msg.getTargetID());
                //这个页面仅仅展示聊天室会话的消息
//            if (i > 47) {
                String s1 = msg.getFromUser().getSignature();
                UserInfo userInfo55 = msg.getFromUser();
                String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
                DefaultUser defaultUser = new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), avatar);
                defaultUser.setSignature(msg.getFromUser().getSignature().length() >= 0 ? msg.getFromUser().getSignature() : "该用户很懒，没有设置签名");
                defaultUser.setHxUserId(msg.getFromID() + "");
//            MyMessage commonMessage = new MyMessage(IMessage.MessageType.RECEIVE_TEXT.ordinal());
                //构造缓存消息体
                long msgId = msg.getServerMessageId();
                final MessageEntry messageEntry = new MessageEntry();
                String messageId = roomID + "_" + msgId;//构造msgId
                messageEntry.setRoomId(roomID + "");
                messageEntry.setUserId(msg.getFromUser().getUserID());//用户Id
                messageEntry.setJgUserName(msg.getFromID() + "");//极光ID
                messageEntry.setDisplayName(msg.getFromUser().getNickname());//昵称
                messageEntry.setAvatar(avatar);//头像
                messageEntry.setTimeString(msg.getCreateTime());//时间
                switch (msg.getContentType()) {
                    case text:
                        LogUtils.d("ChatRoomMessageEvent text", "第" + i + "个" + msg.getContent().toJson().toString());
                        TextContent textContent = (TextContent) msg.getContent();
                        if (textContent.getText() != null && !TextUtils.isEmpty(textContent.getText())) {
                            MyMessage textMessage = new MyMessage(textContent.getText(), IMessage.MessageType.RECEIVE_TEXT.ordinal());
                            long id = msg.getFromUser().getUserID();
                            msg.getFromID();
                            long currentId = JMessageClient.getMyInfo().getUserID();
                            if (currentId == id) {
                                textMessage.setType(IMessage.MessageType.SEND_TEXT.ordinal());
                                textMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            }
//                    String s1 = msg.getFromUser().getAvatar();
                            textMessage.setUserInfo(defaultUser);
                            textMessage.setText(textContent.getText());
                            if (i > 0) {
                                long messageTime = msg.getCreateTime();
                                long preTime = mMsgs.get(i - 1).getCreateTime();
                                long diff = messageTime - preTime;
                                if (diff > 3 * 60 * 1000) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                    String timeString = sdf.format(new Date(messageTime));
                                    textMessage.setTimeString(timeString);
                                }
                            } else {
//                            long messageTime = msg.getCreateTime();
//                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                            textMessage.setTimeString(timeString);
                            }
//                            if (mMsgs.size() == 1 || !mIsHasRoom) {
                            if (mMsgs.size() == 1 || mMessageEntryList == null || mMessageEntryList.size() == 0) {
                                messageList.add(textMessage);
                                mAdapter.addToStart(textMessage, true);
//                                messageEntry.setMessageType("text");
//                                messageEntry.setMessageText(textContent.getText() + "");
//                                mMessageEntryDao.insertOrReplace(messageEntry);
//                                localSaveMessage(chatRoomConversationAllMessage);
                            }
                            mEventMessageList.add(textMessage);
                        }
                        break;
                    case image:
                        //处理图片消息
                        LogUtils.d("ChatRoomMessageEvent image", "第" + i + "个" + msg.getContent().toJson().toString());
                        final ImageContent imageContent = (ImageContent) msg.getContent();
                        imageContent.getLocalPath();//图片本地地址
                        imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                        long imageId = msg.getFromUser().getUserID();
                        final MyMessage imgMessage = new MyMessage(null, IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                        long currentimageId = JMessageClient.getMyInfo().getUserID();
                        if (currentimageId == imageId) {
                            imgMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
                            imgMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                        }
                        UserInfo userInfo = msg.getFromUser();
//                    String s11 = userInfo.getAvatarFile().getAbsolutePath();
//                    String s12 = userInfo.getAvatarFile().getPath();
                        imgMessage.setUserInfo(defaultUser);
                        String mediaID = imageContent.getMediaID();
                        if (imageContent.getLocalPath() == null) {
                            imageContent.downloadOriginImage(msg, new DownloadCompletionCallback() {
                                @Override
                                public void onComplete(int i, String s, File file) {
                                    mPathList.add(file.getAbsolutePath());
                                    mMsgIdList.add(imgMessage.getMsgId() + "");
                                    imgMessage.setMediaFilePath(file.getAbsolutePath());
                                    mAdapter.updateMessage(imgMessage);
                                }
                            });
                        } else {
                            mPathList.add(imageContent.getLocalPath());
                            mMsgIdList.add(imgMessage.getMsgId() + "");
                            imgMessage.setMediaFilePath(imageContent.getLocalThumbnailPath());
                            mAdapter.updateMessage(imgMessage);
                        }
                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mMsgs.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                String timeString = sdf.format(new Date(messageTime));
                                imgMessage.setTimeString(timeString);
                            }
                        } else {
//                        long messageTime = msg.getCreateTime();
//                        String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                        imgMessage.setTimeString(timeString);
                        }
                        if (mMsgs.size() == 1 || mMessageEntryList == null || mMessageEntryList.size() == 0) {
                            messageList.add(imgMessage);
                            mAdapter.addToStart(imgMessage, true);
                        }
                        mEventMessageList.add(imgMessage);
                        break;
                    case voice:
                        //处理语音消息
                        LogUtils.d("ChatRoomMessageEvent voice", "第" + i + "个" + msg.getContent().toJson().toString());
                        final VoiceContent voiceContent = (VoiceContent) msg.getContent();
                        voiceContent.getLocalPath();//语音文件本地地址
                        voiceContent.getDuration();//语音文件时长
                        final MyMessage voiceMessage = new MyMessage(null, IMessage.MessageType.RECEIVE_VOICE.ordinal());
                        long voiceId = msg.getFromUser().getUserID();
                        msg.getFromID();
                        long currentVoiceId = JMessageClient.getMyInfo().getUserID();
                        if (currentVoiceId == voiceId) {
                            voiceMessage.setType(IMessage.MessageType.SEND_VOICE.ordinal());
                            voiceMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                        }
                        String avatarMedialD = msg.getFromUser().getAvatar();
//
                        voiceMessage.setUserInfo(defaultUser);
//                    voiceMessage.setMediaFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/voice/2018-02-28-105103.m4a");
                        if (voiceContent.getLocalPath() != null) {
                            voiceMessage.setMediaFilePath(voiceContent.getLocalPath());
                            voiceMessage.setDuration(voiceContent.getDuration());
                        } else {
                            voiceContent.downloadVoiceFile(msg, new DownloadCompletionCallback() {
                                @Override
                                public void onComplete(int i, String s, File file) {
                                    voiceMessage.setMediaFilePath(file.getAbsolutePath());
                                    voiceMessage.setDuration(voiceContent.getDuration());
                                    mAdapter.updateMessage(voiceMessage);
                                }
                            });
                        }

                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mMsgs.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                String timeString = sdf.format(new Date(messageTime));
                                voiceMessage.setTimeString(timeString);
                            }
                        } else {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                        voiceMessage.setTimeString(timeString);
                        }
                        if (mMsgs.size() == 1 || mMessageEntryList == null || mMessageEntryList.size() == 0) {
                            messageList.add(voiceMessage);
                            mAdapter.addToStart(voiceMessage, true);
                        }
                        mEventMessageList.add(voiceMessage);
                        break;
                    case location:
                        break;
                    case video:
                        //处理视频消息
                        LogUtils.d("ChatRoomMessageEvent video", "第" + i + "个" + msg.getContent().toJson().toString());
                        final VideoContent videoContent = (VideoContent) msg.getContent();
                        videoContent.getVideoLocalPath();//视频文件本地地址
                        videoContent.getDuration();//视频文件时长
                        final MyMessage videoMessage = new MyMessage("", IMessage.MessageType.RECEIVE_VIDEO.ordinal());
                        long videoId = msg.getFromUser().getUserID();
                        msg.getFromID();
                        long currentVideoId = JMessageClient.getMyInfo().getUserID();
                        if (currentVideoId == videoId) {
                            videoMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
                            videoMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                        }
                        videoMessage.setUserInfo(defaultUser);
//                    voiceMessage.setMediaFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/voice/2018-02-28-105103.m4a");
                        if (videoContent.getVideoLocalPath() != null) {
                            videoMessage.setMediaFilePath(videoContent.getThumbLocalPath());
                            videoMessage.setDuration(videoContent.getDuration());
                        } else {
                            videoContent.downloadThumbImage(msg, new DownloadCompletionCallback() {
                                @Override
                                public void onComplete(int i, String s, File file) {
                                    videoMessage.setMediaFilePath(file.getAbsolutePath());
                                    long duration = ((VideoContent) msg.getContent()).getDuration();
                                    videoMessage.setDuration(duration);
                                    mAdapter.updateMessage(videoMessage);
                                }
                            });

                            videoContent.downloadVideoFile(msg, new DownloadCompletionCallback() {
                                @Override
                                public void onComplete(int i, String s, File file) {
                                    videoMessage.setMediaFilePath(file.getAbsolutePath());

                                }
                            });
                        }
                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mMsgs.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                String timeString = sdf.format(new Date(messageTime));
                                videoMessage.setTimeString(timeString);
                            }
                        } else {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                        videoMessage.setTimeString(timeString);
                        }
                        if (mMsgs.size() == 1 || mMessageEntryList == null || mMessageEntryList.size() == 0) {
                            messageList.add(videoMessage);
                            mAdapter.addToStart(videoMessage, true);
                        }
                        mEventMessageList.add(videoMessage);
                        break;
                    case file:
                        LogUtils.d("ChatRoomMessageEvent file", "第" + i + "个" + msg.getContent().toJson().toString());
                        final FileContent fileContent = (FileContent) msg.getContent();

                        String format = fileContent.getFormat();
                        final MyMessage fileMessage = new MyMessage("", IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                        if (format == null) {
                            Map hashMap = fileContent.getStringExtras();
                            hashMap.get("video");
                            String resourceId = fileContent.getResourceId();
                            String VideoMediaID = fileContent.getMediaID();
                            String fileName = fileContent.getFileName();
                            fileMessage.setType(IMessage.MessageType.RECEIVE_VIDEO.ordinal());
                            long fileId1 = msg.getFromUser().getUserID();
                            long currentFileId1 = JMessageClient.getMyInfo().getUserID();
                            if (currentFileId1 == fileId1) {
                                fileMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
                                fileMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            }
                            fileMessage.setMediaFilePath(VideoMediaID + ".mp4");
                        } else {
                            switch (fileContent.getFormat()) {
                                case "jpg":
                                    long fileId = msg.getFromUser().getUserID();
                                    long currentFileId = JMessageClient.getMyInfo().getUserID();
                                    if (currentFileId == fileId) {
                                        fileMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
                                        fileMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                    }
                                    if (fileContent.getLocalPath() != null) {
                                        mPathList.add(fileContent.getLocalPath());
                                        mMsgIdList.add(fileMessage.getMsgId() + "");
                                    } else {
                                        fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                                            @Override
                                            public void onComplete(int i, String s, File file) {
                                                fileMessage.setMediaFilePath(file.getAbsolutePath());
                                                mPathList.add(fileContent.getLocalPath());
                                                mMsgIdList.add(fileMessage.getMsgId() + "");
                                            }
                                        });
                                    }

                                    break;
                                case "mp4":
                                    fileMessage.setType(IMessage.MessageType.RECEIVE_VIDEO.ordinal());
                                    long fileId1 = msg.getFromUser().getUserID();
                                    long currentFileId1 = JMessageClient.getMyInfo().getUserID();
                                    if (currentFileId1 == fileId1) {
                                        fileMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
                                        fileMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                    }
//                                final VideoContent video = (VideoContent) msg.getContent();
//                                LogUtils.d("VideoContent",fileContent.toJson().toString()+fileContent.needAutoDownloadWhenRecv());
                                    if (fileContent.getLocalPath() != null) {
                                        fileMessage.setMediaFilePath(fileContent.getLocalPath());
//                                    if (video.getDuration()!=0){
//                                        fileMessage.setDuration(video.getDuration());
//                                    }
                                    } else {
                                        fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                                            @Override
                                            public void onComplete(int i, String s, File file) {
                                                fileMessage.setMediaFilePath(file.getAbsolutePath());
//                                            if (video.getDuration()!=0){
//                                                fileMessage.setDuration(video.getDuration());
//                                            }
                                            }
                                        });
                                    }
                                    break;
                                case "png":
                                    long fileId2 = msg.getFromUser().getUserID();
                                    long currentFileId2 = JMessageClient.getMyInfo().getUserID();
                                    if (currentFileId2 == fileId2) {
                                        fileMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
                                        fileMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                    }
                                    if (fileContent.getLocalPath() != null) {
                                        ImageContent imageContent1 = (ImageContent) msg.getContent();
                                        fileMessage.setMediaFilePath(imageContent1.getLocalThumbnailPath());
                                        mPathList.add(fileContent.getLocalPath());
                                        mMsgIdList.add(fileMessage.getMsgId() + "");
                                    } else {
                                        fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                                            @Override
                                            public void onComplete(int i, String s, File file) {
                                                fileMessage.setMediaFilePath(file.getAbsolutePath());
                                                mPathList.add(fileContent.getLocalPath());
                                                mMsgIdList.add(fileMessage.getMsgId() + "");
                                            }
                                        });
                                    }
                                    break;
//                            case "mp3":
//                                fileMessage.setType(IMessage.MessageType.RECEIVE_VOICE.ordinal());
//                                long fileId3 = msg.getFromUser().getUserID();
//                                long currentFileId3 = JMessageClient.getMyInfo().getUserID();
//                                if (currentFileId3 == fileId3) {
//                                    fileMessage.setType(IMessage.MessageType.SEND_VOICE.ordinal());
//                                }
//                                final VoiceContent voiceContent1 = (VoiceContent) msg.getContent();
//                                if (voiceContent1.getLocalPath() != null) {
//                                    fileMessage.setMediaFilePath(voiceContent1.getLocalPath());
//                                    fileMessage.setDuration(voiceContent1.getDuration());
//                                } else {
//                                    voiceContent1.downloadVoiceFile(msg, new DownloadCompletionCallback() {
//                                        @Override
//                                        public void onComplete(int i, String s, File file) {
//                                            fileMessage.setMediaFilePath(file.getAbsolutePath());
//                                            fileMessage.setDuration(voiceContent1.getDuration());
//                                        }
//                                    });
//                                }
//                                break;
                            }
                        }
                        fileMessage.setUserInfo(defaultUser);
                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mMsgs.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                String timeString = sdf.format(new Date(messageTime));
                                fileMessage.setTimeString(timeString);
                            }
                        } else {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                        fileMessage.setTimeString(timeString);
                        }
                        if (mMsgs.size() == 1 || mMessageEntryList == null || mMessageEntryList.size() == 0) {
                            messageList.add(fileMessage);
                            mAdapter.addToStart(fileMessage, true);
                        }
                        mEventMessageList.add(fileMessage);
                    case eventNotification:
                        break;
                    case custom:
                        LogUtils.d("ChatRoomMessageEvent custom", "第" + i + "个" + msg.getContent().toJson().toString());
                        CustomContent customContent = (CustomContent) msg.getContent();
                        MyMessage customMessage = new MyMessage("event", IMessage.MessageType.EVENT.ordinal());
                        Map eventMap = customContent.getAllStringValues();
                        ChatEventMessage eventMessage = new ChatEventMessage("event", IMessage.MessageType.EVENT.ordinal());
                        //设置任务参数
                        eventMessage.setTaskId((String) eventMap.get("taskId"));
                        eventMessage.setBounty((String) eventMap.get("bounty"));
                        eventMessage.setIntro((String) eventMap.get("dataString"));
                        String expiryTime = (String) eventMap.get("time");
//                    eventMessage.setExpiryTime(Long.valueOf(expiryTime));
                        if (!DateUtils.isValidLong(expiryTime)) {
                            String TimeStamp = DateUtils.date2TimeStamp(expiryTime, "yyyy-MM-dd HH:mm:ss");
                            eventMessage.setExpiryTime(Long.valueOf(TimeStamp));
                        } else {
                            eventMessage.setExpiryTime(Long.valueOf(expiryTime));
                        }

                        customMessage.setChatEventMessage(eventMessage);
                        DefaultUser user1 = new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getDisplayName(), msg.getFromUser().getAvatar());
                        user1.setHxUserId(msg.getFromID());
                        customMessage.setUserInfo(user1);
//                        messageList.add(customMessage);
//                        mAdapter.addToStart(customMessage, true);
                        break;
                    case unknown:
                        LogUtils.d("ChatRoomMessageEvent unknown", "第" + i + "个" + msg.getContent().toJson().toString());
                        break;
                    case prompt:
                        break;
                    default:
                        closeProgress();
                        break;
                }
            }
//        }
        }
        if (mMsgs.size() > 1) {
            mAdapter.clear();
            mAdapter.addToEndChronologically(mEventMessageList);
            mAdapter.notifyDataSetChanged();
        }
//    }
    }

    public void onEventMainThread(ConversationRefreshEvent event) {
        chatRoomConversation = event.getConversation();
        List<Message> messageList = chatRoomConversation.getAllMessage();

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
                final StandardDialog standardDialog = new StandardDialog(MessageListActivity.this);
                standardDialog.setStandardTitle("提示");
                standardDialog.setStandardMsg("账号已在其他设备上登录，请重新登录");
                standardDialog.setSureText("重新登录");
                standardDialog.setCancelText("取消");
                standardDialog.setCallback(new Callback() {//确定
                    @Override
                    public void invoke() {
                        readyGo(LoginActivity.class);
                    }
                }, new Callback() {//取消
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

    /**
     * 当在聊天界面断网再次连接时收离线事件刷新
     */
    public void onEventMainThread(OfflineMessageEvent event) {
        List<Message> offlineMessageList = event.getOfflineMessageList();
        for (int i = 0; i < offlineMessageList.size(); i++) {
            Message message = offlineMessageList.get(i);
            switch (message.getContentType()) {
                case custom:
                    CustomContent customContent = (CustomContent) message.getContent();
                    MyMessage myMessage = new MyMessage("event", IMessage.MessageType.EVENT.ordinal());
                    Map eventMap = customContent.getAllStringValues();
                    ChatEventMessage eventMessage = new ChatEventMessage("event", IMessage.MessageType.EVENT.ordinal());
                    //设置任务参数
                    eventMessage.setTaskId((String) eventMap.get("taskId"));
                    eventMessage.setBounty((String) eventMap.get("bounty"));
                    eventMessage.setIntro((String) eventMap.get("dataString"));
                    String expiryTime = (String) eventMap.get("time");
//                    eventMessage.setExpiryTime(Long.valueOf(expiryTime));
                    if (DateUtils.isValidDate(expiryTime)) {
                        DateUtils.date2TimeStamp(expiryTime, "yyyy-MM-dd HH:mm:ss");
                        eventMessage.setExpiryTime(Long.valueOf(expiryTime));
                    } else {
                        eventMessage.setExpiryTime(Long.valueOf(expiryTime));
                    }

                    myMessage.setChatEventMessage(eventMessage);
                    DefaultUser user1 = new DefaultUser(message.getFromUser().getUserID(), message.getFromUser().getDisplayName(), message.getFromUser().getAvatar());
                    user1.setHxUserId(message.getFromID());
                    myMessage.setUserInfo(user1);
                    messageList.add(myMessage);
                    mAdapter.addToStart(myMessage, true);
                    break;
                case text:
                    TextContent textContent = (TextContent) message.getContent();
                    myMessage = new MyMessage(textContent.getText(), IMessage.MessageType.RECEIVE_TEXT.ordinal());
                    long id = message.getFromUser().getUserID();
                    message.getFromID();
                    long currentId = JMessageClient.getMyInfo().getUserID();
                    if (currentId == id) {
                        myMessage.setType(IMessage.MessageType.SEND_TEXT.ordinal());
                    }
                    String displayName = message.getFromUser().getDisplayName();
                    String avatar = message.getFromUser().getAvatar();
                    String hxUserId = message.getFromID();
                    DefaultUser user = new DefaultUser(id, displayName, avatar);
                    user.setHxUserId(hxUserId);
                    user.setAvatar(message.getFromUser().getAvatar());
                    user.setSignature(message.getFromUser().getSignature() == null ? "" : message.getFromUser().getSignature());
                    myMessage.setUserInfo(user);
                    myMessage.setText(textContent.getText());
                    if (i > 0) {
                        long messageTime = message.getCreateTime();
                        long preTime = offlineMessageList.get(i - 1).getCreateTime();
                        long diff = messageTime - preTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            String timeString = sdf.format(new Date(messageTime));
                            myMessage.setTimeString(timeString);
                        }
                    }
                    messageList.add(myMessage);
                    mAdapter.addToStart(myMessage, true);
                    break;
                default:
                    initMsgAdapter();
                    break;
            }

        }
        mData = messageList;

    }

//    //用户下线事件
//    public void onEventMainThread(LoginStateChangeEvent event) {
//        StandardDialog dialog = new StandardDialog(MessageListActivity.this);
//        dialog.setStandardMsg("该账号已在其他设备上登录");
//    }

    //聊天室输入框多功能界面
    public void onEventMainThread(AppsAdapter.ImageEvent event) {
        Intent intent;
        if (event.getContext() != MessageListActivity.this) {
            return;
        }
        switch (event.getFlag()) {
            case MyApplication.IMAGE_MESSAGE:
                int from = PickImageActivity.FROM_LOCAL;
                int requestCode = RequestCode.PICK_IMAGE;
                if (ContextCompat.checkSelfPermission(MessageListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    PickImageActivity.start(MessageListActivity.this, requestCode, from, tempFile(), true, 1,
                            true, false, 0, 0);
                }

                break;
            case MyApplication.TAKE_PHOTO_MESSAGE:
                int takePhotoPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                if (takePhotoPermission != PackageManager.PERMISSION_GRANTED) {
                    LogUtils.d("未申请权限,正在申请");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    LogUtils.d("已经申请权限");
                    intent = new Intent(MessageListActivity.this, CameraActivity.class);
                    intent.putExtra("camera", "takePhoto");
                    startActivityForResult(intent, RequestCode.TAKE_PHOTO);
                }
                break;
            case MyApplication.TACK_VIDEO:
                int takeVideoPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                if (takeVideoPermission != PackageManager.PERMISSION_GRANTED) {
                    LogUtils.d("未申请权限,正在申请");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    LogUtils.d("已经申请权限");
                    intent = new Intent(MessageListActivity.this, CameraActivity.class);
                    intent.putExtra("camera", "takeVideo");
                    startActivityForResult(intent, RequestCode.TAKE_VIDEO);

                }
                break;
            /*case MyApplication.TAKE_LOCATION:
//                if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "请在应用管理中打开“位置”访问权限！", Toast.LENGTH_LONG).show();
//                } else {
                intent = new Intent(mContext, MapPickerActivity.class);
                intent.putExtra(MyApplication.TARGET_ID, mTargetId);
                intent.putExtra(MyApplication.TARGET_APP_KEY, mTargetAppKey);
                intent.putExtra(MyApplication.GROUP_ID, mGroupId);
                intent.putExtra("sendLocation", true);
                startActivityForResult(intent, MyApplication.REQUEST_CODE_SEND_LOCATION);
//                }
                break;
            case MyApplication.FILE_MESSAGE:
//                if (ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "请在应用管理中打开“读写存储”访问权限！", Toast.LENGTH_LONG).show();
//
//                } else {
                intent = new Intent(mContext, SendFileActivity.class);
                intent.putExtra(JGApplication.TARGET_ID, mTargetId);
                intent.putExtra(JGApplication.TARGET_APP_KEY, mTargetAppKey);
                intent.putExtra(JGApplication.GROUP_ID, mGroupId);
                startActivityForResult(intent, JGApplication.REQUEST_CODE_SEND_FILE);
//                }
                break;
            case MyApplication.BUSINESS_CARD:
                intent = new Intent(mContext, FriendListActivity.class);
                intent.putExtra("isSingle", mIsSingle);
                intent.putExtra("userId", mTargetId);
                intent.putExtra("groupId", mGroupId);
                startActivity(intent);
                break;*/
            case MyApplication.TACK_VOICE:
                break;
            default:
                break;
        }

    }

    private String tempFile() {
        String filename = StringUtil.get32UUID() + JPG;
        return StorageUtil.getWritePath(filename, StorageType.TYPE_TEMP);
    }


    private void initChatView() {
        mChatView.initModule();
        mChatView.isShowBtnInputJoin(true);//显示加入按钮
        mChatView.getChatInputView().setShowBottomMenu(false);
        mReceiver = new HeadsetDetectReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, intentFilter);
        mChatView.setOnTouchListener(this);
        /*
         * 向聊天室发送消息
         *
         * */
        chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
        if (null == chatRoomConversation) {
            chatRoomConversation = Conversation.createChatRoomConversation(Long.valueOf(roomID));
        }
        //发送文字
        xhsEmoticonsKeyBoard.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mcgContent = xhsEmoticonsKeyBoard.getEtChat().getText().toString();
                scrollToBottom();
                if (mcgContent.equals("")) {
                    return;
                }
                final MyMessage message = new MyMessage(mcgContent, IMessage.MessageType.SEND_TEXT.ordinal());
                TextContent content = new TextContent(mcgContent);
                if (chatRoomConversation != null) {
                    final Message msg = chatRoomConversation.createSendMessage(content);
                    //构造消息
                    if (msg != null && msg.getFromUser() != null) {
                        if (msg.getFromUser().getAvatarFile() != null) {
                            DefaultUser defaultUser = new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getDisplayName(), msg.getFromUser().getAvatarFile().getAbsolutePath());
                            message.setUserInfo(defaultUser);
                        } else {
                            DefaultUser defaultUser = new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getDisplayName(), SCCacheUtils.getCacheHeadImg());
                            message.setUserInfo(defaultUser);
                        }
                        message.setText(mcgContent);
                        long messageTime = msg.getCreateTime();
                        long preTime = new Date().getTime();
                        long diff = preTime - messageTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                      message.setTimeString(timeString);
                        }
//                      message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        msg.getServerMessageId();
                        mAdapter.addToStart(message, true);
                        msg.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
//                                    List<Message> allMessages = chatRoomConversation.getAllMessage();
//                                    localSaveMessage(allMessages);
                                    List<Message> lastMessage = new ArrayList<Message>();
                                    lastMessage.add(msg);
                                    localSaveMessage(lastMessage);
//                                long msgId = msg.getServerMessageId();
//                                TextContent textContent = (TextContent) msg.getContent();
//                                LogUtils.d("发送消息的ID:", msgId + "" + textContent.getText());
                                    message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                } else {
                                    message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                }
                                MessageListActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.updateMessage(message);
                                        xhsEmoticonsKeyBoard.getEtChat().setText("");
                                    }
                                });
                            }
                        });
                        JMessageClient.sendMessage(msg);
                    } else {
                        message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                        mAdapter.addToStart(message, true);
                        xhsEmoticonsKeyBoard.getEtChat().setText("");
                    }
                } else {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(MessageListActivity.this, "聊天服务器未初始化，请重试");
                        }
                    });
//                    message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
//                    mAdapter.addToStart(message, true);
//                    xhsEmoticonsKeyBoard.getEtChat().setText("");
                    enterChatRoom();
                }
            }
        });
        //点击隐藏聊天输入框表情栏
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(final CharSequence input) {

//                chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
//                if (null == chatRoomConversation) {
//                    chatRoomConversation = Conversation.createChatRoomConversation(Long.valueOf(roomID));
//                }
                final String inputString;
                inputString = input.toString();
                if (inputString.length() > 0 && chatRoomConversation != null) {
                    final Message msg = chatRoomConversation.createSendTextMessage(inputString + "");//实际聊天室可以支持所有类型的消息发送，demo为了简便，仅仅实现了文本类型的消息发送
                    final MyMessage message = new MyMessage(inputString, IMessage.MessageType.SEND_TEXT.ordinal());
                    mAdapter.addToStart(message, true);
                    msg.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMessage) {
                            if (0 == responseCode) {
                                String s = responseMessage;
//                                Toast.makeText(MessageListActivity.this, "发送消息成功", Toast.LENGTH_SHORT);
                                LogUtils.d("发送聊天室消息", "code: " + responseCode + " 回调信息：" + responseMessage);
                                String s1 = msg.getFromUser().getAvatar();
                                if (msg.getFromUser().getAvatarFile() != null) {
                                    DefaultUser defaultUser = new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getDisplayName(), msg.getFromUser().getAvatarFile().getAbsolutePath());
                                    message.setUserInfo(defaultUser);
                                }
                                message.setText(inputString);
                                long messageTime = msg.getCreateTime();
                                long preTime = new Date().getTime();
                                long diff = preTime - messageTime;
                                if (diff > 3 * 60 * 1000) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                    String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                    message.setTimeString(timeString);
                                }
//                                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                                message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);

//                                messageList.add(message);
                            } else if (847001 == responseCode) {
                                message.setUserInfo(new DefaultUser(0, msg.getFromUser().getDisplayName(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                                message.setText(inputString);
                                message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                            } else {
                                message.setUserInfo(new DefaultUser(0, msg.getFromUser().getDisplayName(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                                message.setText(inputString);
                                message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(MessageListActivity.this, "发送消息失败", Toast.LENGTH_SHORT);
                                    mAdapter.updateMessage(message);
                                }
                            });
                        }
                    });

                    JMessageClient.sendMessage(msg);
                } else {
                    ToastUtils.showToast(MessageListActivity.this, "请输入内容");
                }

                return true;
            }


            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty() || chatRoomConversation == null) {
                    return;
                }
                MyMessage message;
                for (FileItem item : list) {
//                    Message msg = chatRoomConversation.createSendFileMessage(item,)
                    if (item.getType() == FileItem.Type.Image) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                        mPathList.add(item.getFilePath());
                        mMsgIdList.add(message.getMsgId());
//                        Message msg = chatRoomConversation.createSendMessage(video);
                    } else if (item.getType() == FileItem.Type.Video) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
                        message.setDuration(((VideoItem) item).getDuration());
                        File videoFile = new File(item.getFilePath());
                        try {
                            MediaMetadataRetriever media = new MediaMetadataRetriever();
                            media.setDataSource(item.getFilePath());
                            Bitmap bitmap = media.getFrameAtTime();
                            long duration = ((VideoItem) item).getDuration();
                            VideoContent video = new VideoContent(bitmap, "mp4", videoFile, item.getFileName(), (int) duration);
                            Message msg = chatRoomConversation.createSendMessage(video);
//                            Message msg = chatRoomConversation.createSendFileMessage(videoFile, item.getFileName());
                            message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                            message.setMediaFilePath(item.getFilePath());
                            message.setDuration(((VideoItem) item).getDuration());
                            message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                            final MyMessage finalMessage = message;
                            mAdapter.addToStart(message, true);
                            msg.setOnSendCompleteCallback(new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        finalMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                    } else {
                                        finalMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                    }
                                    MessageListActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.updateMessage(finalMessage);

                                        }
                                    });
                                }
                            });
                            JMessageClient.sendMessage(msg);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            final MyMessage myMessage = message;
                            MessageListActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.addToStart(myMessage, true);

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            final MyMessage myMessage = message;
                            MessageListActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.addToStart(myMessage, true);

                                }
                            });
                        }
                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }
                }
            }

            @Override
            public boolean switchToMicrophoneMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_record_voice),
                            RC_RECORD_VOICE, perms);
                }
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_photo),
                            RC_PHOTO, perms);
                }
                // If you call updateData, select photo view will try to update data(Last update over 30 seconds.)
                mChatView.getChatInputView().getSelectPhotoView().updateData();
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                scrollToBottom();
                String[] perms = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_camera),
                            RC_CAMERA, perms);
                    return false;
                } else {
                    File rootDir = getFilesDir();
                    String fileDir = rootDir.getAbsolutePath() + "/photo";
                    mChatView.setCameraCaptureFile(fileDir, new SimpleDateFormat("yyyy-MM-dd-hhmmss",
                            Locale.getDefault()).format(new Date()));
                }
                return true;
            }

            @Override
            public boolean switchToEmojiMode() {
                scrollToBottom();
                return true;
            }
        });
        //发送语音
        xhsEmoticonsKeyBoard.getBtnVoice().initConv(chatRoomConversation, mAdapter, mChatView);

        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                String[] perms = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(MessageListActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MessageListActivity.this,
                            getResources().getString(R.string.rationale_record_voice),
                            RC_RECORD_VOICE, perms);
                }
                // set voice file path, after recording, audio file will save here
                String path = Environment.getExternalStorageDirectory().getPath() + "/voice";
                File destDir = new File(path);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                mChatView.setRecordVoiceFile(destDir.getPath(), DateFormat.format("yyyy-MM-dd-hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(final File voiceFile, final int duration) {
                chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
                if (null == chatRoomConversation) {
                    chatRoomConversation = Conversation.createChatRoomConversation(Long.valueOf(roomID));
                }
                final Message msg;
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE.ordinal());
                try {
                    msg = chatRoomConversation.createSendVoiceMessage(voiceFile, duration);
                    message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                    message.setMediaFilePath(voiceFile.getPath());
                    message.setDuration(duration);
                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    mAdapter.addToStart(message, true);
                    msg.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            } else {
                                message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                            }
                            MessageListActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.updateMessage(message);

                                }
                            });
                        }
                    });
                    JMessageClient.sendMessage(msg);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                    mAdapter.updateMessage(message);
                }


            }

            @Override
            public void onCancelRecord() {

            }

            /**
             * In preview record voice layout, fires when click cancel button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewCancel() {

            }

            /**
             * In preview record voice layout, fires when click send button
             * Add since chatinput v0.7.3
             */
            @Override
            public void onPreviewSend() {
            }
        });

        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                final Message msg;
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                if (chatRoomConversation != null) {
                    try {
                        msg = chatRoomConversation.createSendImageMessage(new File(photoPath));
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setMediaFilePath(photoPath);
                        mPathList.add(photoPath);
                        mMsgIdList.add(message.getMsgId());
                        message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getUserName(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                        JMessageClient.sendMessage(msg);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                MessageListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addToStart(message, true);
                    }
                });
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {

            }

            @Override
            public void onCancelVideoRecord() {

            }
        });

        mChatView.getChatInputView().getInputView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollToBottom();
                return false;
            }
        });

        mChatView.getSelectAlbumBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageListActivity.this, "OnClick select album button",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMsgAdapter() {
        final float density = getResources().getDisplayMetrics().density;
        final float MIN_WIDTH = 60 * density;
        final float MAX_WIDTH = 200 * density;
        final float MIN_HEIGHT = 60 * density;
        final float MAX_HEIGHT = 200 * density;
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                // You can use other image load libraries.
                if (string.contains("R.drawable")) {
                    Integer resId = getResources().getIdentifier(string.replace("R.drawable", ""),
                            "drawable", getPackageName());

                    avatarImageView.setImageResource(resId);
                } else {
                    try {
                        RequestOptions options = new RequestOptions();
                        options.placeholder(R.mipmap.aurora_headicon_default);
                        Glide.with(MessageListActivity.this)
                                .load(string)
                                .apply(options)
                                .into(avatarImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            /**
             * Load image message
             * @param imageView Image message's ImageView.
             * @param string A file path, or a uri or url.
             */
            @Override
            public void loadImage(final ImageView imageView, String string) {
                // You can use other image load libraries.
                RequestOptions options = new RequestOptions();
                options.fitCenter().placeholder(R.drawable.aurora_picture_not_found);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(string)
                        .apply(options)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                int imageWidth = resource.getWidth();
                                int imageHeight = resource.getHeight();
                                Log.d(TAG, "Image width " + imageWidth + " height: " + imageHeight);

                                // 裁剪 bitmap
                                float width, height;
                                if (imageWidth > imageHeight) {
                                    if (imageWidth > MAX_WIDTH) {
                                        float temp = MAX_WIDTH / imageWidth * imageHeight;
                                        height = temp > MIN_HEIGHT ? temp : MIN_HEIGHT;
                                        width = MAX_WIDTH;
                                    } else if (imageWidth < MIN_WIDTH) {
                                        float temp = MIN_WIDTH / imageWidth * imageHeight;
                                        height = temp < MAX_HEIGHT ? temp : MAX_HEIGHT;
                                        width = MIN_WIDTH;
                                    } else {
                                        float ratio = imageWidth / imageHeight;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        height = imageHeight * ratio;
                                        width = imageWidth;
                                    }
                                } else {
                                    if (imageHeight > MAX_HEIGHT) {
                                        float temp = MAX_HEIGHT / imageHeight * imageWidth;
                                        width = temp > MIN_WIDTH ? temp : MIN_WIDTH;
                                        height = MAX_HEIGHT;
                                    } else if (imageHeight < MIN_HEIGHT) {
                                        float temp = MIN_HEIGHT / imageHeight * imageWidth;
                                        width = temp < MAX_WIDTH ? temp : MAX_WIDTH;
                                        height = MIN_HEIGHT;
                                    } else {
                                        float ratio = imageHeight / imageWidth;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        width = imageWidth * ratio;
                                        height = imageHeight;
                                    }
                                }
                                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                                params.width = (int) width;
                                params.height = (int) height;
                                imageView.setLayoutParams(params);
                                Matrix matrix = new Matrix();
                                float scaleWidth = width / imageWidth;
                                float scaleHeight = height / imageHeight;
                                matrix.postScale(scaleWidth, scaleHeight);
                                imageView.setImageBitmap(Bitmap.createBitmap(resource, 0, 0, imageWidth, imageHeight, matrix, true));
                            }
                        });
            }

            /**
             * Load video message
             * @param imageCover Video message's image cover
             * @param uri Local path or url.
             */
            @Override
            public void loadVideo(ImageView imageCover, String uri) {
                long interval = 5000 * 1000;
                if (MessageListActivity.this.isDestroyed()) {
                    return;
                } else {
                    Glide.with(MessageListActivity.this)
                            .asBitmap()
                            .load(uri)
                            // Resize image view by change override size.
                            .apply(new RequestOptions().frame(interval).override(200, 400))
                            .into(imageCover);
                }

            }
        };

        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        /*
         *  If you want to customise your layout, try to create custom ViewHolder:
         * holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
         * holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
         * CustomViewHolder must extends ViewHolders defined in MsgListAdapter.
         * Current ViewHolders are TxtViewHolder, VoiceViewHolder.
         * */

        holdersConfig.setEventMessage(CustomEvenMsgHolder.class, R.layout.item_custom_event_message);

        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);

        //消息点击事件
        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_VIDEO.ordinal()) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(MessageListActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else if (message.getType() == IMessage.MessageType.RECEIVE_IMAGE.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_IMAGE.ordinal()) {
                    if (mMsgIdList.indexOf(message.getMsgId()) != -1) {
                        Intent intent = new Intent(MessageListActivity.this, BrowserImageActivity.class);
                        intent.putExtra("messageId", message.getMsgId());
                        intent.putStringArrayListExtra("pathList", mPathList);
                        intent.putStringArrayListExtra("idList", mMsgIdList);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(mContext, "图片正在加载中，请滑动页面刷新");
                    }
                } else if (message.getType() == IMessage.MessageType.SEND_VOICE.ordinal()
                        || message.getType() == IMessage.MessageType.RECEIVE_VOICE.ordinal()) {
                    if (mAdapter.getMediaPlayer().isPlaying()) {
                        mAdapter.getMediaPlayer().reset();
                    }
                }
            }
        });

        //消息长按事件
        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(View view, final MyMessage message) {
                if (message.getType() == IMessage.MessageType.RECEIVE_TEXT.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_TEXT.ordinal()) {
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    float OldListY = (float) location[1];
                    float OldListX = (float) location[0];
                    new TipView.Builder(MessageListActivity.this, mChatView, (int) OldListX + view.getWidth() / 2, (int) OldListY + view.getHeight())
                            .addItem(new TipItem("复制"))
                            .setOnItemClickListener(new TipView.OnItemClickListener() {
                                @Override
                                public void onItemClick(String str, final int position) {
                                    //复制
                                    if (position == 0) {
//                                        if (msg.getContentType() == ContentType.text) {
                                        if (message.getType() == IMessage.MessageType.RECEIVE_TEXT.ordinal()
                                                || message.getType() == IMessage.MessageType.SEND_TEXT.ordinal()) {
//                                            final String content = ((TextContent) msg.getContent()).getText();
                                            final String content = message.getText();
                                            if (Build.VERSION.SDK_INT > 11) {
                                                ClipboardManager clipboard = (ClipboardManager) mContext
                                                        .getSystemService(Context.CLIPBOARD_SERVICE);
                                                ClipData clip = ClipData.newPlainText("Simple text", content);
                                                clipboard.setPrimaryClip(clip);
                                            } else {
                                                android.text.ClipboardManager clip = (android.text.ClipboardManager) mContext
                                                        .getSystemService(Context.CLIPBOARD_SERVICE);
                                                if (clip.hasText()) {
                                                    clip.getText();
                                                }
                                            }
                                            Toast.makeText(MessageListActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MessageListActivity.this, "只支持复制文字", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void dismiss() {

                                }
                            })
                            .create();
                }
            }
        });

        //头像点击事件
        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                Bundle bundle = new Bundle();
                bundle.putParcelable("userInfo", userInfo);
                bundle.putString("targetId", message.getFromUser().getId() + "");
                if (JMessageClient.getMyInfo().getUserID() == message.getFromUser().getId()) {
                    return;
//                    readyGo(PersonalActivity.class);
                } else {
//                    readyGo(SingerChatInfoActivity.class, bundle);
                    readyGo(FriendInfoActivity.class, bundle);
                }

            }
        });

        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
            @Override
            public void onStatusViewClick(MyMessage message) {
                // message status view click, resend or download here
            }
        });

        //查看任务点击事件
//        mAdapter.addToStart(evenMyMessage, true);
        mAdapter.setBtnEventTaskClickListener(new MsgListAdapter.OnBtnEventTaskClickListener<MyMessage>() {
            @Override
            public void TaskEventMessageClick(MyMessage message) {
                if (message.getChatEventMessage() == null) {
                    ChatEventMessage chatEventMessage = (ChatEventMessage) message;
                    Intent intent = new Intent(MessageListActivity.this, TaskDetailActivity.class);
                    intent.putExtra("chatEventMessage", chatEventMessage);
                    intent.putExtra("roomId", roomID);
                    startActivity(intent);
                } else {
                    ChatEventMessage chatEventMessage = (ChatEventMessage) message.getChatEventMessage();
                    DefaultUser user = new DefaultUser(message.getFromUser().getId(), message.getFromUser().getDisplayName(), message.getFromUser().getAvatarFilePath());
                    chatEventMessage.setiUser(user);
                    Intent intent = new Intent(MessageListActivity.this, TaskDetailActivity.class);
                    intent.putExtra("chatEventMessage", chatEventMessage);
                    intent.putExtra("roomId", roomID);
                    startActivity(intent);
                }
            }
        });

        List<Conversation> roomConversationList = JMessageClient.getChatRoomConversationList();
        if (roomConversationList != null && roomConversationList.size() > 0) {
            for (int i = 0; i < roomConversationList.size(); i++) {
                Conversation conversation = roomConversationList.get(i);
                String cvsaId = conversation.getId();
                if (conversation.getTargetId().equals(roomID)) {
                    List<Message> allMessage = conversation.getAllMessage();
                    String extra = conversation.getExtra();
                }
            }
        }

        mAdapter.addToEndChronologically(mData);
        PullToRefreshLayout layout = mChatView.getPtrLayout();
        layout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PullToRefreshLayout layout) {
                Log.i("MessageListActivity", "Loading next page");
                loadNextPage();
            }
        });
        // Deprecated, should use onRefreshBegin to load next page
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
//                Log.i("MessageListActivity", "Loading next page");
//                loadNextPage();
            }
        });
        mChatView.setAdapter(mAdapter);
        //yin
        mChatView.getMessageListView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xhsEmoticonsKeyBoard.reset();
            }
        });
        mAdapter.getLayoutManager().scrollToPosition(0);
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<MyMessage> list = new ArrayList<>();
                Resources res = getResources();
                String[] messages = res.getStringArray(R.array.conversation);
                mChatView.getPtrLayout().refreshComplete();
            }
        }, 1500);
    }

    private void scrollToBottom() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mChatView.getMessageListView().smoothScrollToPosition(0);
            }
        }, 200);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mArcMenu.isOpen()) {
                    mArcMenu.toggleMenu(300);
//                    mArcMenu.getChildAt(0).findViewWithTag("circelText").setBackground(getResources().getDrawable(R.drawable.fb_close));
                }
//                else {
////                    mArcMenu.getChildAt(0).findViewWithTag("circelText").setBackground(getResources().getDrawable(R.drawable.shape_guide_point_default));
//                }

                ChatInputView chatInputView = mChatView.getChatInputView();
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                try {
                    View v = getCurrentFocus();
                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        view.clearFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                view.performClick();
                break;
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onResume() {
        super.onResume();
        String character = SCCacheUtils.getCacheCharacterInfo();
        final CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
        if (characterInfo != null && !isRestartActivity) {
            if (!TextUtils.isEmpty(characterInfo.getName())) {
                userNikeView.setText("" + characterInfo.getName());
            }
            if (!TextUtils.isEmpty(characterInfo.getSignature())) {
                tvUserSign.setText("" + characterInfo.getSignature());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.getMediaPlayer().stop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        addTaskThread.stop();
        mAdapter.getMediaPlayer().stop();
        EventBus.getDefault().unregister(this);
        JMessageClient.unRegisterEventReceiver(this);
//        ChatRoomManager.leaveChatRoom(Long.valueOf(roomID), new BasicCallback() {
//            @Override
//            public void gotResult(int i, String s) {
//                String s1 = s;
//                LogUtils.d("leaveChatRoom", "离开聊天室");
//            }
//        });
        WebSocketHandler.getDefault().removeListener(mSocketListener);
        unregisterReceiver(mReceiver);
        mSensorManager.unregisterListener(this);
    }

    public void showProgress() {
        mDialog = new com.shanchain.data.common.ui.widgets.CustomDialog(this, 0.4, R.layout.common_dialog_progress, null);
//        mDialog.setMax(100);
        mDialog.setMessage("正在获取该元社区信息," +
                "\n请稍等..");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void closeProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

    }
}
