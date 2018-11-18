package com.shanchain.shandata.ui.view.activity.jmessageui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.common.utils.DateUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.h5.SCWebViewActivity;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.rn.AppReactPackage;
import com.shanchain.data.common.rn.modules.SettingModule;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;

import cn.jiguang.imui.commons.models.IUser;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;

import com.shanchain.shandata.rn.fragment.RNMineFragment;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.HistoryMessage;
import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.presenter.impl.TaskPresenterImpl;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.ChatView;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.ui.view.fragment.view.TaskView;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.widgets.arcMenu.ArcMenu;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.jiguang.imui.chatinput.ChatInputView;
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
import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.ChatRoomInfo;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.eventbus.EventBus;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MessageListActivity extends BaseActivity implements View.OnTouchListener,
        EasyPermissions.PermissionCallbacks,
        SensorEventListener, ArthurToolBar.OnLeftClickListener,
        ArthurToolBar.OnRightClickListener,
        NavigationView.OnNavigationItemSelectedListener, TaskView {

    private final static String TAG = "MessageListActivity";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;
    private ArthurToolBar mTbMain;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    //        private String roomID = "15217562";
//        private String roomID = "12826211";
    private String roomID;

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData;
    private String formatDate;

    private InputMethodManager mImm;
    private Window mWindow;
    private HeadsetDetectReceiver mReceiver;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private ArcMenu mArcMenu;
    private ArcMenu.OnMenuItemClickListener onMenuItemClickListener;
    private Handler handler;
    private List<Conversation> conversationList;

    /**
     * Store all image messages' path, pass it to {@link BrowserImageActivity},
     * so that click image message can browser all images.
     */
    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();
    private ChatEventMessage chatEventMessage;
    private List<MyMessage> messageList = new ArrayList<>();
    private Conversation chatRoomConversation;
    private List<Conversation> allRoomConversation;

    private TextView limitedTime;
    private Thread addTaskThread;
    private long timeStamp;
    private TaskPresenter taskPresenter;
    private SCTimePickerView scTimePickerView;
    private SCTimePickerView.OnTimeSelectListener onTimeSelectListener;
    private ChatEventMessage chatEventMessage1;
    private MyMessage evenMyMessage = new MyMessage();;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_seting;
    }

    @Override
    protected void initViewsAndEvents() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //极光消息监听注册
        JMessageClient.registerEventReceiver(this, 1000);
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomId");
        enterChatRoom();


        allRoomConversation = JMessageClient.getChatRoomConversationList();
        JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
        chatRoomConversation = Conversation.createChatRoomConversation(Long.valueOf(roomID));
        initData();
        initToolBar();
        initView();
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();
        registerProximitySensorListener();
        mChatView = (ChatView) findViewById(R.id.chat_view);
        mArcMenu = findViewById(R.id.fbn_menu);
        mArcMenu.setVisibility(View.GONE);

        mChatView.initModule();

        mChatView.isShowBtnInputJoin(true);
        mChatView.getChatInputView().setShowBottomMenu(false);
        initMsgAdapter();
        mReceiver = new HeadsetDetectReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, intentFilter);
        mChatView.setOnTouchListener(this);
        mArcMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        //加入聊天室按钮
        mChatView.setOnBtnInputClickListener(new ChatView.OnBtnInputClickListener() {
            @Override
            public void OnBtnInputClick(View view) {
//                showLoadingDialog();
                String[] hxUserName = new String[]{SCCacheUtils.getCacheHxUserName()};
                String jArray = JSONArray.toJSONString(hxUserName);

                mChatView.isShowBtnInputJoin(false);
                mChatView.getChatInputView().setShowBottomMenu(true);
                mArcMenu.setVisibility(View.VISIBLE);
//                mArcMenu.setOnMenuItemClickListener(onMenuItemClickListener);

                /*SCHttpUtils.postWithUserId()
                        .url(HttpApi.CHAT_ROOM_MEMBERS)
                        .addParams("jArray",jArray)
                        .addParams("roomId",roomID)
                        .build()
                        .execute(new SCHttpStringCallBack() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                String code = JSONObject.parseObject(response).getString("code");
                                if (TextUtils.equals(NetErrCode.COMMON_SUC_CODE,code)){
//                                    mChatView.isShowBtnInputJoin(false);
//                                    mChatView.getChatInputView().setShowBottomMenu(true);
//                                    mArcMenu.setOnMenuItemClickListener(onMenuItemClickListener);
                                    closeLoadingDialog();
                                }
                            }
                        });*/
            }
        });

        /*
         * 向聊天室发送消息
         *
         * */
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(final CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
                chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
                if (null == chatRoomConversation) {
                    chatRoomConversation = Conversation.createChatRoomConversation(Long.valueOf(roomID));
                }
                final Message msg = chatRoomConversation.createSendTextMessage(input.toString());//实际聊天室可以支持所有类型的消息发送，demo为了简便，仅仅实现了文本类型的消息发送
                msg.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseMessage) {
                        MyMessage message = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT.ordinal());
                        if (0 == responseCode) {

                            Toast.makeText(MessageListActivity.this, "发送消息成功", Toast.LENGTH_SHORT);
                            LogUtils.d("发送聊天室消息", "code: " + responseCode + " 回调信息：" + responseMessage);

                            message.setUserInfo(new DefaultUser(0, msg.getFromUser().getDisplayName(), msg.getFromUser().getAvatar()));
                            message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                            message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            mAdapter.addToStart(message, true);
                        } else if (847001 == responseCode) {

                        } else {
                            Toast.makeText(MessageListActivity.this, "发送消息失败", Toast.LENGTH_SHORT);
                        }
                    }
                });
                JMessageClient.sendMessage(msg);

                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }

                MyMessage message;
                for (FileItem item : list) {
                    if (item.getType() == FileItem.Type.Image) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                        mPathList.add(item.getFilePath());
                        mMsgIdList.add(message.getMsgId());
                    } else if (item.getType() == FileItem.Type.Video) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
                        message.setDuration(((VideoItem) item).getDuration());

                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }

                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    message.setMediaFilePath(item.getFilePath());
                    message.setUserInfo(new DefaultUser(1, "Ironman", "R.drawable.ironman"));

                    final MyMessage fMsg = message;
                    MessageListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg, true);
                        }
                    });
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

        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
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
            public void onFinishRecord(File voiceFile, int duration) {
                MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE.ordinal());
                message.setUserInfo(new DefaultUser(1, "Ironman", "R.drawable.ironman"));
                message.setMediaFilePath(voiceFile.getPath());
                message.setDuration(duration);
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                mAdapter.addToStart(message, true);
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
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                message.setMediaFilePath(photoPath);
                mPathList.add(photoPath);
                mMsgIdList.add(message.getMsgId());
                message.setUserInfo(new DefaultUser(1, "Ironman", "R.drawable.ironman"));
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

    private void enterChatRoom() {
        if (roomID != null) {
            RoleManager.switchRoleCacheRoomId(roomID);
            showLoadingDialog();
            ChatRoomManager.enterChatRoom(Long.valueOf(roomID), new RequestCallback<Conversation>() {
                @Override
                public void gotResult(int i, String s, Conversation conversation) {
                    if (i == 0) {
                        closeLoadingDialog();
                        JMessageClient.getChatRoomConversation(Long.valueOf(roomID)).getAllMessage();

                    } else if (i == 851003) {
                        ChatRoomManager.leaveChatRoom(Long.valueOf(roomID), new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                enterChatRoom();
                            }
                        });
                        closeLoadingDialog();
                    } else if (i == 871300) {
                        closeLoadingDialog();
                        readyGo(LoginActivity.class);
                        finish();
                    } else {
                        ToastUtils.showToast(MessageListActivity.this, "加入聊天室失败");
                        closeLoadingDialog();
                        finish();
                    }
                }
            });
        } else {
            ToastUtils.showToast(MessageListActivity.this, "加入聊天室失败");
        }
    }

    //初始化聊天数据
    private void initData() {
        taskPresenter = new TaskPresenterImpl(this);

        chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
        if (null == chatRoomConversation) {
            chatRoomConversation = Conversation.createChatRoomConversation(Long.valueOf(roomID));
        }

        mData = getMessages(messageList);
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

        /*
         * 初始化悬浮按钮
         * */
        onMenuItemClickListener = new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                mChatView.getChatInputView().setFocusable(false);
                switch (view.getId()) {
                    //查询任务
                    case R.id.linear_add_query:

                        Intent intent = new Intent(MessageListActivity.this, TaskListActivity.class);
                        intent.putExtra("roodId", roomID);
                        startActivity(intent);

                        break;
                    //添加任务
                    case R.id.linear_add_task:
                        final android.os.Message handleMessage = new android.os.Message();
                        handleMessage.what = 1;
                        handleMessage.obj = view;
                        addTaskThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendMessage(handleMessage);
                            }
                        });
                        addTaskThread.start();


                        break;

                }

            }
        };

        initTaskDialog();


    }

    /*
     * 初始化弹窗
     *
     * */
    private void initTaskDialog() {

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
                                EditText describeEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_describe);
                                EditText bountyEditText = (EditText) dialog.getByIdView(R.id.et_input_dialog_bounty);
                                limitedTime = (TextView) dialog.getByIdView(R.id.dialog_select_task_time);

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
        builder.setType(new boolean[]{true, true, true, true, false, false})//设置显示年、月、日、时、分、秒
                .setDecorView((ViewGroup) findViewById(android.R.id.content).getRootView())
//                .setDecorView((ViewGroup) dialog.getWindow().getDecorView().getRootView())
                .isCenterLabel(true)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setCancelText("清除")
                .setCancelColor(MessageListActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .setSubmitText("完成")
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
            ToastUtils.showToast(MessageListActivity.this, "请输入完整信息");
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
                    .addParams("characterId", characterId + "")
                    .addParams("bounty", bounty + "")
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
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                String publishTime = JSONObject.parseObject(data).getString("PublishTime");
                                String task = JSONObject.parseObject(data).getString("Task");
                                chatEventMessage1 = JSONObject.parseObject(task, ChatEventMessage.class);

                                Map customMap = new HashMap();
                                customMap.put("taskId", chatEventMessage1.getTaskId() + "");
                                customMap.put("bounty", chatEventMessage1.getBounty() + "");
                                customMap.put("dataString", chatEventMessage1.getIntro() + "");
                                customMap.put("time", LimitedTtime);

                                Message sendCustomMessage = chatRoomConversation.createSendCustomMessage(customMap);
                                sendCustomMessage.setOnSendCompleteCallback(new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        String s1 = s;
                                        MyMessage myMessage = new MyMessage();
                                        if (0 == i) {
                                            Toast.makeText(MessageListActivity.this, "发送任务消息成功", Toast.LENGTH_SHORT);
                                            LogUtils.d("发送任务消息", "code: " + i + " 回调信息：" + s);
                                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                            myMessage.setChatEventMessage(chatEventMessage1);
                                            mAdapter.addToStart(myMessage, true);
                                        } else {
                                            Toast.makeText(MessageListActivity.this, "发送任务消息失败", Toast.LENGTH_SHORT);
                                            chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                            mAdapter.addToStart(myMessage, true);
                                        }
                                    }
                                });
                                JMessageClient.sendMessage(sendCustomMessage);

//                                chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
//                                mAdapter.addToStart(chatEventMessage1, true);
                                dialog.dismiss();
                            } else if (code == "1001") {
                                //余额不足
                                Toast.makeText(MessageListActivity.this, "您的钱包余额不足", Toast.LENGTH_SHORT);
                            }else {
                                Toast.makeText(MessageListActivity.this, "当前不在聊天室内", Toast.LENGTH_SHORT);
                            }
                        }
                    });

        }
    }

    private void initToolBar() {
        mTbMain = (ArthurToolBar) findViewById(R.id.tb_main);
        mTbMain.setTitleText("聊天室");
        mTbMain.setLeftImage(R.mipmap.my_info);
        mTbMain.setFavoriteImage(R.mipmap.favorite);
        mTbMain.setRightImage(R.mipmap.close);
        mTbMain.isShowChatRoom(true);
        mTbMain.setOnLeftClickListener(this);
        mTbMain.setOnRightClickListener(this);
        mTbMain.isShowChatRoom(false);
        //设置收藏按钮的监听
        mTbMain.setOnFavoriteClickListener(new ArthurToolBar.OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(View v) {
                mTbMain.getFavoriteView().setBackground(getResources().getDrawable(R.mipmap.favorite_selected));
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

    //标题栏左侧按钮实现
    @Override
    public void onLeftClick(View v) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        toggle.onDrawerOpened(drawer);
    }

    //标题栏右侧按钮实现
    @Override
    public void onRightClick(View v) {
        finish();

    }

    @Override
    public void onBackPressed() {
        closeLoadingDialog();
        ChatRoomManager.leaveChatRoom(Long.valueOf(roomID), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                int i1 = i;
                String s1 = s;
            }
        });
        JMessageClient.logout();
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
            Intent intent = new Intent(mContext, SCWebViewActivity.class);
            JSONObject obj = new JSONObject();
            obj.put("url", "http://m.qianqianshijie.com/wallet");
            obj.put("title", "我的资产");
            String webParams = obj.toJSONString();
            intent.putExtra("webParams", webParams);
            startActivity(intent);
        } else if (id == R.id.nav_my_task) {
            Intent intent = new Intent(MessageListActivity.this, TaskListActivity.class);
            intent.putExtra("roodId", roomID);
            startActivity(intent);

        } else if (id == R.id.nav_my_message) {
            readyGo(MyMessageActivity.class);

        } else if (id == R.id.nav_my_favorited) {
            readyGo(FootPrintActivity.class);

        } else if (id == R.id.real_identity) {

        } else if (id == R.id.nav_manage) {
            AppReactPackage.registerNativeModule(SettingModule.class);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void initTask(List<ChatEventMessage> list, boolean isLast) {

    }

    @Override
    public void addSuccess(boolean success) {
        if (success == true) {

        }
    }

    @Override
    public void releaseTaskView(List<ChatEventMessage> list, boolean isSuccess) {
        chatEventMessage1.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
        mAdapter.addToStart(chatEventMessage1, true);
    }

    @Override
    public void supportSuccess(boolean isSuccess, int position) {

    }

    @Override
    public void supportCancelSuccess(boolean isSuccess, int position) {

    }

    @Override
    public void deleteTaskView(boolean isSuccess, int position) {

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private List<MyMessage> getMessages(List messageList) {

        List<MyMessage> list = new ArrayList<>();
//        Resources res = getResources();
//        String[] messages = res.getStringArray(R.array.messages_array);

//        chatEventMessage = new ChatEventMessage("chat", IMessage.MessageType.EVENT.ordinal());
//        chatEventMessage.setBounty("80");
//        chatEventMessage.setIntro("分享马甲APP");
//        chatEventMessage.setExpiryTime(new Date().getTime());
//        evenMyMessage.setChatEventMessage(chatEventMessage);
        //聊天室里是否有消息，没有消息加载本地假数据
        if (messageList != null && messageList.size() > 0) {
            for (int i = 0; i < messageList.size(); i++) {
                MyMessage myMessage = (MyMessage) messageList.get(i);
                list.add(myMessage);
            }
        }
        return list;
    }

    // 接收聊天室消息
    public void onEventMainThread(ChatRoomMessageEvent event) {
        Log.d("tag", "ChatRoomMessageEvent received .");
        chatRoomConversation = JMessageClient.getChatRoomConversation(Long.valueOf(roomID));
        chatRoomConversation.getAllMessage();
        List<Message> msgs = event.getMessages();
        MyMessage myMessage;
        for (Message msg : msgs) {
            //这个页面仅仅展示聊天室会话的消息
            switch (msg.getContentType()) {
                case text:
                    TextContent textContent = (TextContent) msg.getContent();
                    myMessage = new MyMessage(textContent.getText(), IMessage.MessageType.RECEIVE_TEXT.ordinal());
                    long id = msg.getFromUser().getUserID();
                    long currentId = JMessageClient.getMyInfo().getUserID();
                    if (currentId==id){
                        myMessage.setType(IMessage.MessageType.SEND_TEXT.ordinal());
                    }
                    String displayName = msg.getFromUser().getDisplayName();
                    String avatar = msg.getFromUser().getAvatar();
                    IUser user = new DefaultUser(id, displayName, avatar);
                    myMessage.setUserInfo(user);
                    myMessage.setText(textContent.getText());
                    messageList.add(myMessage);
                    mAdapter.addToStart(myMessage, true);
                    break;
                case custom:
                    CustomContent customContent = (CustomContent) msg.getContent();
                    myMessage = new MyMessage("event", IMessage.MessageType.EVENT.ordinal());
                    Map eventMap = customContent.getAllStringValues();
//                    String s = customContent.getStringValue()
//                    long userId = (long) customContent.getNumberValue("id");//获取发布任务的用户ID
//                    String userName = customContent.getStringValue("userName");//获取发布任务的用户名
                    ChatEventMessage eventMessage = new ChatEventMessage("event", IMessage.MessageType.EVENT.ordinal());
                    //设置任务参数
                    eventMessage.setTaskId((String) eventMap.get("taskId"));
                    eventMessage.setBounty((String)eventMap.get("bounty"));
                    eventMessage.setIntro((String) eventMap.get("dataString"));
                    eventMessage.setTimeString((String) eventMap.get("time"));

                    myMessage.setChatEventMessage(eventMessage);
                    messageList.add(myMessage);
                    mAdapter.addToStart(myMessage, true);
                    break;
                default:
                    mData = messageList;
                    initMsgAdapter();
                    break;
            }
        }
        closeLoadingDialog();
    }

    public void onEventMainThread(MessageEvent event) {
        Message msg = event.getMessage();
        MyMessage myMessage;
        initData();
        initView();
        initMsgAdapter();
    }

    public void onEvent(OfflineMessageEvent event) {
        event.getConversation().getAllMessage();
        initData();
        initView();
        initMsgAdapter();
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

                    RequestOptions options = new RequestOptions();
                    options.placeholder(R.mipmap.aurora_headicon_default);
                    Glide.with(MessageListActivity.this)
                            .load(string)
                            .apply(options)
                            .into(avatarImageView);
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
                Glide.with(MessageListActivity.this)
                        .asBitmap()
                        .load(uri)
                        // Resize image view by change override size.
                        .apply(new RequestOptions().frame(interval).override(200, 400))
                        .into(imageCover);
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


//        View itemView = getLayoutInflater().inflate(R.layout.item_custom_event_message,null);
//        CustomEvenMsgHolder customEvenMsgHolder = new CustomEvenMsgHolder(itemView);
        holdersConfig.setEventMessage(CustomEvenMsgHolder.class, R.layout.item_custom_event_message);

        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);

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
                    Intent intent = new Intent(MessageListActivity.this, BrowserImageActivity.class);
                    intent.putExtra("msgId", message.getMsgId());
                    intent.putStringArrayListExtra("pathList", mPathList);
                    intent.putStringArrayListExtra("idList", mMsgIdList);
                    startActivity(intent);
                } else {
//                    Toast.makeText(getApplicationContext(),
//                            getApplicationContext().getString(R.string.message_click_hint),
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(View view, MyMessage message) {
                Toast.makeText(getApplicationContext(),
                        getApplicationContext().getString(R.string.message_long_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        //头像点击事件
        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = new DefaultUser(message.getFromUser().getId(), message.getFromUser().getDisplayName(), message.getFromUser().getAvatarFilePath());
                Bundle bundle = new Bundle();
                bundle.putParcelable("userInfo", userInfo);
                readyGo(SetFragmentActivity.class, bundle);
            }
        });

        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
            @Override
            public void onStatusViewClick(MyMessage message) {
                // message status view click, resend or download here
            }
        });


        //显示接收的文字消息
/*        MyMessage message = new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT.ordinal());
        message.setUserInfo(new DefaultUser(0, "Deadpool", "R.drawable.deadpool"));
        mAdapter.addToStart(message, true);*/
        //显示接收的语音消息
/*        MyMessage voiceMessage = new MyMessage("", IMessage.MessageType.RECEIVE_VOICE.ordinal());
        voiceMessage.setUserInfo(new DefaultUser(0, "Deadpool", "R.drawable.deadpool"));
        voiceMessage.setMediaFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/voice/2018-02-28-105103.m4a");
        voiceMessage.setDuration(4);
        mAdapter.addToStart(voiceMessage, true);*/
        //显示发送的语音消息
/*        MyMessage sendVoiceMsg = new MyMessage("", IMessage.MessageType.SEND_VOICE.ordinal());
        sendVoiceMsg.setUserInfo(new DefaultUser(1, "Ironman", "R.drawable.ironman"));
        sendVoiceMsg.setMediaFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/voice/2018-02-28-105103.m4a");
        sendVoiceMsg.setDuration(4);
        mAdapter.addToStart(sendVoiceMsg, true);*/

        //查看任务点击事件
//        mAdapter.addToStart(evenMyMessage, true);
        mAdapter.setBtnEventTaskClickListener(new MsgListAdapter.OnBtnEventTaskClickListener<MyMessage>() {
            @Override
            public void TaskEventMessageClick(MyMessage message) {
                ChatEventMessage chatEventMessage = (ChatEventMessage) message.getChatEventMessage();
                Intent intent = new Intent(MessageListActivity.this, TaskDetailActivity.class);
                intent.putExtra("chatEventMessage", chatEventMessage);
                startActivity(intent);
            }
        });

        //显示接受的语音消息
/*        MyMessage receiveVideo = new MyMessage("", IMessage.MessageType.RECEIVE_VIDEO.ordinal());
        receiveVideo.setMediaFilePath(Environment.getExternalStorageDirectory().getPath() + "/Pictures/Hangouts/video-20170407_135638.3gp");
        receiveVideo.setDuration(4);
        receiveVideo.setUserInfo(new DefaultUser(0, "Deadpool", "R.drawable.deadpool"));
        mAdapter.addToStart(receiveVideo, true);*/

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
        mChatView.setFocusable(false);
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
                }
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
    protected void onDestroy() {
        super.onDestroy();
//        addTaskThread.stop();
        EventBus.getDefault().unregister(this);
        JMessageClient.unRegisterEventReceiver(this);
        ChatRoomManager.leaveChatRoom(Long.valueOf(roomID), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                String s1 = s;
                LogUtils.d("leaveChatRoom", "离开聊天室");
            }
        });
//        JMessageClient.logout();
        unregisterReceiver(mReceiver);
        mSensorManager.unregisterListener(this);
    }

}
