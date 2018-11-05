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

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.h5.SCWebViewActivity;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;

import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;

import com.shanchain.shandata.ui.presenter.TaskPresenter;
import com.shanchain.shandata.ui.presenter.impl.TaskPresenterImpl;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.ChatView;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.widgets.arcMenu.ArcMenu;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.ChatRoomInfo;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.eventbus.EventBus;
import cn.jpush.im.api.BasicCallback;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MessageListActivity extends BaseActivity implements View.OnTouchListener,
        EasyPermissions.PermissionCallbacks,
        SensorEventListener, ArthurToolBar.OnLeftClickListener,
        ArthurToolBar.OnRightClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MessageListActivity";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;
    private ArthurToolBar mTbMain;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;
    private List<MyMessage> mData;

    private InputMethodManager mImm;
    private Window mWindow;
    private HeadsetDetectReceiver mReceiver;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private ArcMenu mArcMenu;
    private long roomID = 15188952;
    private Handler handler;
    /**
     * Store all image messages' path, pass it to {@link BrowserImageActivity},
     * so that click image message can browser all images.
     */
    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();
    private ChatEventMessage chatEventMessage;
    private List<Message> messageList = new ArrayList<>();
    private Conversation chatRoomConversation;
    private TextView limitedTime;
    private Thread addTaskThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this, 100);
            JMessageClient.registerEventReceiver(this);
        }

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_seting;
    }

    @Override
    protected void initViewsAndEvents() {
        initData();
        initToolBar();
        initView();
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();
        registerProximitySensorListener();
        mChatView = (ChatView) findViewById(R.id.chat_view);

        mChatView.initModule();

        mChatView.isShowBtnInputJoin(false);
        initMsgAdapter();
        mReceiver = new HeadsetDetectReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, intentFilter);
        mChatView.setOnTouchListener(this);

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

                // 发送聊天室消息
                Conversation conv = JMessageClient.getChatRoomConversation(roomID);
                if (null == conv) {
                    conv = Conversation.createChatRoomConversation(roomID);
                }
                final Message msg = conv.createSendTextMessage(input.toString());//实际聊天室可以支持所有类型的消息发送，demo为了简便，仅仅实现了文本类型的消息发送
                msg.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseMessage) {
                        MyMessage message = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT.ordinal());
                        if (0 == responseCode) {

                            Toast.makeText(MessageListActivity.this, "发送消息成功", Toast.LENGTH_SHORT);

                            message.setUserInfo(new DefaultUser(0, msg.getFromUser().getDisplayName(), msg.getFromUser().getAvatar()));
                            message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                            message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            mAdapter.addToStart(message, true);
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

    //初始化聊天数据
    private void initData() {
        ChatRoomManager.enterChatRoom(roomID, new RequestCallback<Conversation>() {
            @Override
            public void gotResult(int i, String s, Conversation conversation) {
                LogUtils.d("聊天室信息：######### " + i + "回调的信息" + s);
            }
        });

        chatRoomConversation = JMessageClient.getChatRoomConversation(roomID);
        if (null == chatRoomConversation) {
            chatRoomConversation = Conversation.createChatRoomConversation(roomID);
        }

        messageList = chatRoomConversation.getAllMessage();
        mData = getMessages(messageList);
    }

    // 接收聊天室任务消息
//    public void onEvent(ChatRoomMessageEvent event) {
//        Log.d("tag", "ChatRoomMessageEvent received .");
//        ToastUtils.showToastLong(this,"子线程执行了该方法");
//        List<Message> msgs = event.getMessages();
//        for (Message msg : msgs) {
//            //这个页面仅仅展示聊天室会话的消息
////            postMessageToDisplay("MessageReceived", event.getResponseCode(), event.getResponseDesc(), msg);
//            TextContent textcontent=(TextContent) msg.getContent();
//            LogUtils.d("文字消息 "+textcontent.getText());
//            LogUtils.d("聊天室会话事件的消息########## " + event.getResponseDesc() + "事件回调码 " + event.getResponseCode() + "消息数" + event.getMessages().size());
//            messageList.add(msg);
//        }
//
//        mData = getMessages(messageList);
//        mAdapter.addToEnd(mData);
//    }


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
        mArcMenu = findViewById(R.id.fbn_menu);
        mArcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
//                Toast.makeText(MessageListActivity.this, pos + ":" + view.getTag(), Toast.LENGTH_SHORT).show();
                switch (view.getId()) {
                    //查询任务
                    case R.id.linear_add_query:
                        Toast.makeText(MessageListActivity.this, pos + "查询任务:" + view.getTag(), Toast.LENGTH_SHORT).show();
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
        });

        /*
         * 初始化弹窗
         * */
        final int[] idItems = new int[]{R.id.et_input_dialog_describe, R.id.et_input_dialog_bounty, R.id.dialog_select_task_time, R.id.btn_dialog_input_sure, R.id.iv_dialog_close};
        final CustomDialog dialog = new CustomDialog(MessageListActivity.this, false, 1.0, R.layout.common_dialog_chat_room_task, idItems);
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
//                        Toast.makeText(MessageListActivity.this, pos + "添加任务:" + view.getTag(), Toast.LENGTH_SHORT).show();
                        dialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
                            @Override
                            public void OnItemClick(CustomDialog dialog, View view) {
                                switch (view.getId()) {
                                    case R.id.btn_dialog_input_sure:

                                        EditText describeEditText = (EditText) dialog.getByIdView(new EditText(MessageListActivity.this),R.id.et_input_dialog_describe);
                                        EditText bountyEditText = (EditText) dialog.getByIdView(new EditText(MessageListActivity.this),R.id.et_input_dialog_bounty);
                                        if (describeEditText.getText() != null && bountyEditText.getText() != null) {
                                            String spaceId = SCCacheUtils.getCacheSpaceId();//获取当前的空间ID
                                            String bounty = bountyEditText.getText().toString();
                                            String dataString = describeEditText.getText().toString();
                                            String time = String.valueOf(System.currentTimeMillis());
                                            TaskPresenter taskPresenter = new TaskPresenterImpl();
                                            taskPresenter.releaseTask(spaceId, bounty, dataString, time);
                                            ChatEventMessage eventMessage = new ChatEventMessage("chat", IMessage.MessageType.EVENT.ordinal());
                                            eventMessage.setBounty(bounty);
                                            eventMessage.setIntro(dataString);
                                            eventMessage.setLimitedTime(time);
                                            eventMessage.setSupportCount(0);
                                            eventMessage.setCommentCount(0);
                                            mAdapter.addToStart(eventMessage,true);
                                        } else {
                                            ToastUtils.showToast(MessageListActivity.this, "请输入完整信息");
                                        }

                                        break;
                                    case R.id.iv_dialog_close:
                                        dialog.dismiss();
                                        break;
                                    //选择时间
                                    case R.id.dialog_select_task_time:
                                        LogUtils.d("发布任务", "点击发布任务事件");
                                        final Activity topActivity = ActivityStackManager.getInstance().getTopActivity();
                                        topActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                SCTimePickerView pickerView = new SCTimePickerView.Builder(topActivity, new SCTimePickerView.OnTimeSelectListener() {
                                                    @Override
                                                    public void onTimeSelect(Date date, View v) {
                                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                        String format = simpleDateFormat.format(date);
                                                        LogUtils.d("SCTimePickerView", "点击了SCTimePickerView" + format);
                                                    }

                                                })
                                                        .setType(new boolean[]{true, true, true, false, false, false})
                                                        .isCenterLabel(false)
                                                        .setCancelText("清除")
                                                        .setCancelColor(topActivity.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                                                        .setSubmitText("完成")
                                                        .setSubCalSize(14)
                                                        .setTitleBgColor(topActivity.getResources().getColor(com.shanchain.common.R.color.colorWhite))
                                                        .setSubmitColor(topActivity.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                                                        .build();
                                                pickerView.setDate(Calendar.getInstance());
                                                pickerView.show();

                                                pickerView.setOnCancelClickListener(new SCTimePickerView.OnCancelClickListener() {
                                                    @Override
                                                    public void onCancelClick(View v) {
                                                        ToastUtils.showToastLong(topActivity.getBaseContext(), "取消选择");
                                                    }
                                                });
                                            }
                                        });
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

    private void initToolBar() {
        mTbMain = (ArthurToolBar) findViewById(R.id.tb_main);
        mTbMain.setTitleText("聊天室");
        mTbMain.setLeftImage(R.mipmap.my_info);
        mTbMain.setFavoriteImage(R.mipmap.favorite);
        mTbMain.setRightImage(R.mipmap.close);
        mTbMain.isShowChatRoom(true);
        mTbMain.setOnLeftClickListener(this);
        mTbMain.setOnRightClickListener(this);
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

    }

    @Override
    public void onBackPressed() {
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
            obj.put("url","http://www.qianqianshijie.com/#/agreement");
            obj.put("title","我的资产");
            String webParams = obj.toJSONString();
            intent.putExtra("webParams",webParams);
            startActivity(intent);
        } else if (id == R.id.nav_my_task) {
            readyGo(TaskListActivity.class);

        } else if (id == R.id.nav_my_message) {

        } else if (id == R.id.nav_my_favorited) {

        } else if (id == R.id.real_identity) {

        }else if (id == R.id.nav_manage) {

        }

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

    private List<MyMessage> getMessages(List<Message> messageList) {

        List<MyMessage> list = new ArrayList<>();
        Resources res = getResources();
        String[] messages = res.getStringArray(R.array.messages_array);
        chatEventMessage = new ChatEventMessage("chat", IMessage.MessageType.EVENT.ordinal());
        chatEventMessage.setBounty("80");
        chatEventMessage.setIntro("南孚？我觉得555电池挺好用的");
        chatEventMessage.setCommentCount(1180);
        chatEventMessage.setLimitedTime("12:00 2018-10-20");
        chatEventMessage.setSupportCount(1891);
        //聊天室里是否有消息，没有消息加载本地假数据
        if (messageList != null && messageList.size() > 0) {
            for (int i = 0; i < messageList.size(); i++) {
                Message conversationMessage = messageList.get(i);

                MyMessage message = new MyMessage(conversationMessage.getCreateTime() + "时间", conversationMessage.getContentType().ordinal());
                DefaultUser defaultUser = new DefaultUser(conversationMessage.getFromUser().getUserID(),
                        conversationMessage.getFromUser().getDisplayName(), conversationMessage.getFromUser().getAvatar());
                message.setUserInfo(defaultUser);
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                list.add(message);
            }
        } else {
            MyMessage message;
            message = new MyMessage("欢迎来到聊天室", IMessage.MessageType.RECEIVE_TEXT.ordinal());
            message.setUserInfo(new DefaultUser(0, "DeadPool", "R.drawable.deadpool"));
            list.add(message);
        }
        return list;
    }

    //初始化聊天信息
    private void initConversation(int start, int count, long roomID) {
        ChatRoomManager.getChatRoomListByApp(start, count, new RequestCallback<List<ChatRoomInfo>>() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<ChatRoomInfo> chatRoomInfos) {
                String result = null != chatRoomInfos ? chatRoomInfos.toString() : null;
//                postTextToDisplay("getChatRoomListByApp", responseCode, responseMessage, result);
//                mAdapter.addToStart();
            }
        });

        // 获取当前用户所加入的所有聊天室的信息
        ChatRoomManager.getChatRoomListByUser(new RequestCallback<List<ChatRoomInfo>>() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<ChatRoomInfo> chatRoomInfos) {
                String result = null != chatRoomInfos ? chatRoomInfos.toString() : null;
//                postTextToDisplay("getChatRoomListByUser", responseCode, responseMessage, result);
            }
        });

        // 查询指定roomID的聊天室信息
        ChatRoomManager.getChatRoomInfos(Collections.singleton(roomID), new RequestCallback<List<ChatRoomInfo>>() {
            @Override
            public void gotResult(int responseCode, String responseMessage, List<ChatRoomInfo> chatRoomInfos) {
                String result = null != chatRoomInfos ? chatRoomInfos.toString() : null;
//                postTextToDisplay("getChatRoomInfos", responseCode, responseMessage, result);
            }
        });

        // 进入聊天室
        ChatRoomManager.enterChatRoom(roomID, new RequestCallback<Conversation>() {
            @Override
            public void gotResult(int responseCode, String responseMessage, Conversation conversation) {
                String result = null != conversation ? conversation.toString() : null;
//                postTextToDisplay("enterChatRoom", responseCode, responseMessage, result);
            }
        });

        // 离开聊天室
        ChatRoomManager.leaveChatRoom(roomID, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
//                postTextToDisplay("leaveChatRoom", responseCode, responseMessage, null);
            }
        });

    }

    // 接收聊天室消息
    public void onEventMainThread(ChatRoomMessageEvent event) {
        Log.d("tag", "ChatRoomMessageEvent received .");
        List<Message> msgs = event.getMessages();
        for (Message msg : msgs) {
            //这个页面仅仅展示聊天室会话的消息
            postMessageToDisplay("MessageReceived", event.getResponseCode(), event.getResponseDesc(), msg);
        }
    }

    private void postMessageToDisplay(String messageType, int responseCode, String responseDesc, Message msg) {


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
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.message_click_hint),
                            Toast.LENGTH_SHORT).show();
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

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                Toast.makeText(getApplicationContext(),
                        getApplicationContext().getString(R.string.avatar_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
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
        //添加任务事件
//        MyMessage eventMsg = new MyMessage("", IMessage.MessageType.EVENT.ordinal());
        mAdapter.addToStart(chatEventMessage, true);
        mAdapter.setBtnEventTaskClickListener(new MsgListAdapter.OnBtnEventTaskClickListener() {
            @Override
            public void TaskEventMessageClick() {
//                ToastUtils.showToastLong(MessageListActivity.this, "点击了领取任务事件");
                Log.d("###############", "点击领取任务按钮事件");
            }
        });
        mAdapter.setOnTvEventCommentClickListener(new MsgListAdapter.OnTvEventCommentClickListener<MyMessage>() {
            @Override
            public void CommentEventMessageClick() {
                ToastUtils.showToastLong(MessageListActivity.this, "点击了评论按钮事件");
                Log.d("###############", "点击评论按钮事件");
                readyGo(DynamicDetailsActivity.class);
            }
        });

        mAdapter.setOnTvEventLikeClickListener(new MsgListAdapter.OnTvEventLikeClickListener<MyMessage>() {
            @Override
            public void LikeEventMessageClick() {
                ToastUtils.showToastLong(MessageListActivity.this, "点击了点赞按钮事件");
                Log.d("###############", "点击点赞按钮事件");
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
        mAdapter.getLayoutManager().scrollToPosition(0);
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<MyMessage> list = new ArrayList<>();
                Resources res = getResources();
                String[] messages = res.getStringArray(R.array.conversation);
                for (int i = 0; i < messages.length; i++) {
                    MyMessage message;
                    if (i % 2 == 0) {
                        message = new MyMessage(messages[i], IMessage.MessageType.RECEIVE_TEXT.ordinal());
                        message.setUserInfo(new DefaultUser(0, "DeadPool", "R.drawable.deadpool"));
                    } else {
                        message = new MyMessage(messages[i], IMessage.MessageType.SEND_TEXT.ordinal());
                        message.setUserInfo(new DefaultUser(1, "IronMan", "R.drawable.ironman"));
                    }
                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    list.add(message);
                }
//                Collections.reverse(list);
                // MessageList 0.7.2 add this method, add messages chronologically.
//                mAdapter.addToEndChronologically(mData);
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
        addTaskThread.stop();
        unregisterReceiver(mReceiver);
        mSensorManager.unregisterListener(this);
        EventBus.getDefault().unregister(this);
        JMessageClient.unRegisterEventReceiver(this);
    }

}
