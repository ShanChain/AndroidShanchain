package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.ChatView;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ViewHolderController;
import cn.jiguang.imui.messages.ptr.PtrHandler;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import cn.jiguang.imui.model.ChatEventMessage;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SingleChatActivity extends BaseActivity implements View.OnTouchListener, EasyPermissions.PermissionCallbacks,
        SensorEventListener, ArthurToolBar.OnLeftClickListener,
        ArthurToolBar.OnRightClickListener {

    @Bind(R.id.tb_main)
    ArthurToolBar mTbMain;

    private final static String TAG = "SingleChatActivity";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;
    private final static String JM_USER = SCCacheUtils.getCacheHxUserName();

    PullToRefreshLayout pullToRefreshLayout;
    //    private final static String FORM_USER_ID = "qwer";
    private String FORM_USER_ID;
    private String FORM_USER_NAME;

    private ChatView mChatView;
    private MsgListAdapter<MyMessage> mAdapter;

    private InputMethodManager mImm;
    private Window mWindow;
    private HeadsetDetectReceiver mReceiver;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    private Conversation mConv;
    private String mTargetId;
    private String mTargetAppKey;
    private Activity mContext;
    private UserInfo mMyInfo;
    private DefaultUser userInfo;


    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();
    private List<MyMessage> mData = new ArrayList<>();
    private List<Message> mConvData = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_singer_chat_room;
    }

    @Override
    protected void initViewsAndEvents() {
        JMessageClient.registerEventReceiver(this);
        mMyInfo = JMessageClient.getMyInfo();
        final Bundle bundle = getIntent().getExtras();
        userInfo = bundle.getParcelable("userInfo");
        if (userInfo != null) {
            FORM_USER_ID = userInfo.getHxUserId();
            FORM_USER_NAME = userInfo.getDisplayName();

            JMessageClient.getUserInfo(FORM_USER_NAME, new GetUserInfoCallback() {
                @Override
                public void gotResult(int i, String s, UserInfo userInfo) {

                }
            });
        } else {
            FORM_USER_NAME = "qwer";
        }

        initToolbar();
        initData();
        pullToRefreshLayout = findViewById(R.id.pull_to_refresh_layout);
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();
        registerProximitySensorListener();
        mChatView = (ChatView) findViewById(R.id.activity_chat_view);
        mChatView.initModule();
        mChatView.isShowBtnInputJoin(false);
        if (FORM_USER_NAME != null) {
            mData = getConversationMessages();
            initMsgAdapter();
        }

        mReceiver = new HeadsetDetectReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, intentFilter);
        mChatView.setOnTouchListener(this);

        //发送单聊消息
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(final CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
                mConv = JMessageClient.getSingleConversation(FORM_USER_NAME);
                if (mConv == null) {
                    mConv = Conversation.createSingleConversation(FORM_USER_NAME);
                }
                TextContent textContent = new TextContent(input.toString());
                Map extrasMap = new HashMap();
                extrasMap.put("userName", JMessageClient.getMyInfo().getUserName() + "");
                extrasMap.put("conversationType", "single");
//                extrasMap.put("appkey",conversationMessage.getFromUser().getDisplayName()+"");
                textContent.setExtras(extrasMap);
                final Message conversationMessage = mConv.createSendMessage(textContent);

                conversationMessage.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        MyMessage myMessage = new MyMessage("" + input.toString(), IMessage.MessageType.SEND_TEXT.ordinal());
                        if (i == 0) {
                            mMyInfo = JMessageClient.getMyInfo();
                            DefaultUser user = new DefaultUser(mMyInfo.getUserID(), mMyInfo.getDisplayName(), mMyInfo.getAvatar());
                            myMessage.setUserInfo(user);
                            myMessage.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                            myMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            myMessage.setText(input.toString());
                            mData.add(myMessage);
                            mAdapter.addToStart(myMessage, true);
                        }
//                        }else {
//                            ToastUtils.showToast(SingleChatActivity.this,"发送消息失败");
//                            DefaultUser user = new DefaultUser(mMyInfo.getUserID(),mMyInfo.getDisplayName(),mMyInfo.getAvatar());
//                            myMessage.setUserInfo(user);
//                            myMessage.setText(input.toString());
//                            myMessage.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
//                            myMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
//                            mAdapter.addToStart(myMessage,true);
//                        }

                    }
                });
                JMessageClient.sendMessage(conversationMessage);
                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }
                mConv = JMessageClient.getSingleConversation(FORM_USER_NAME);
                MyMessage message;
                for (FileItem item : list) {
//                    Message msg = chatRoomConversation.createSendFileMessage(item,)
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
                    File videoFile = new File(item.getFilePath());
                    try {
                        Message msg = mConv.createSendFileMessage(videoFile, item.getFileName());
                        msg.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {

                                }
                            }
                        });
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setMediaFilePath(item.getFilePath());
                        message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                        JMessageClient.sendMessage(msg);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (JMFileSizeExceedException e) {
                        e.printStackTrace();
                    }

                    final MyMessage fMsg = message;
                    SingleChatActivity.this.runOnUiThread(new Runnable() {
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

                if (!EasyPermissions.hasPermissions(SingleChatActivity.this, perms)) {
                    EasyPermissions.requestPermissions(SingleChatActivity.this,
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

                if (!EasyPermissions.hasPermissions(SingleChatActivity.this, perms)) {
                    EasyPermissions.requestPermissions(SingleChatActivity.this,
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

                if (!EasyPermissions.hasPermissions(SingleChatActivity.this, perms)) {
                    EasyPermissions.requestPermissions(SingleChatActivity.this,
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
            public void onFinishRecord(final File voiceFile, final int duration) {
                mConv = JMessageClient.getSingleConversation(FORM_USER_NAME);
                if (mConv == null) {
                    mConv = Conversation.createSingleConversation(FORM_USER_NAME);
                }
                final Message msg;
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE.ordinal());
                try {
                    msg = mConv.createSendVoiceMessage(voiceFile, duration);
                    msg.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            int i1 = i;
                            String s1 = s;
                            if (i == 0) {
                            }
                        }
                    });
                    message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                    message.setMediaFilePath(voiceFile.getPath());
                    message.setDuration(duration);
                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    mAdapter.addToStart(message, true);
                    JMessageClient.sendMessage(msg);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
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
                if (mConv != null) {
                    try {
                        msg = mConv.createSendImageMessage(new File(photoPath));
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
                SingleChatActivity.this.runOnUiThread(new Runnable() {
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
                Toast.makeText(SingleChatActivity.this, "OnClick select album button",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        mMyInfo = JMessageClient.getMyInfo();
        final Bundle bundle = getIntent().getExtras();
        userInfo = bundle.getParcelable("userInfo");
        if (userInfo != null) {
            FORM_USER_NAME = userInfo.getDisplayName();
        } else {
            FORM_USER_NAME = "qwer";
        }

    }

    private void initToolbar() {
        mTbMain.setTitleTextColor(Color.WHITE);
        mTbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
        mTbMain.setBackgroundColor(Color.parseColor("#4FD1F6"));
        mTbMain.setLeftImage(R.mipmap.back);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTbMain.getTitleView().setLayoutParams(layoutParams);
//        mTbMain.setTitleText("陌生好友");
        mTbMain.setRightImage(R.mipmap.more);

        mTbMain.setOnLeftClickListener(this);
        mTbMain.setOnRightClickListener(this);
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

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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

    private List<MyMessage> getConversationMessages() {
        mConv = JMessageClient.getSingleConversation(FORM_USER_NAME);
        if (mConv == null) {
            mConv = Conversation.createSingleConversation(FORM_USER_NAME);
        } else if (mConv != null) {
            mConvData = mConv.getAllMessage();

            MyMessage myMessage;
            for (int i = 0; i < mConvData.size(); i++) {
                final Message msg = mConvData.get(i);
                String s1 = msg.getFromUser().getSignature();
                String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
                DefaultUser defaultUser = new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), avatar);
                defaultUser.setSignature(msg.getFromUser().getSignature().length() >= 0 ? msg.getFromUser().getSignature() : "该用户很懒，没有设置签名");
                defaultUser.setHxUserId(msg.getFromID() + "");
                switch (msg.getContent().getContentType()) {
                    case text:
                        TextContent textContent = (TextContent) msg.getContent();
                        myMessage = new MyMessage(textContent.getText(), IMessage.MessageType.RECEIVE_TEXT.ordinal());
                        if (msg.getFromUser().getUserID() == mMyInfo.getUserID()) {
                            myMessage.setType(IMessage.MessageType.SEND_TEXT.ordinal());
                        }
                        myMessage.setUserInfo(defaultUser);
                        mData.add(myMessage);
                        break;
                    case image:
                        //处理图片消息
                        LogUtils.d("ChatRoomMessageEvent image", "第" + i + "个" + msg.getContent().toJson().toString());
                        final ImageContent imageContent = (ImageContent) msg.getContent();
                        imageContent.getLocalPath();//图片本地地址
                        imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                        long imageId = msg.getFromUser().getUserID();
                        msg.getFromID();
                        final MyMessage imgMessage = new MyMessage(null, IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                        long currentimageId = JMessageClient.getMyInfo().getUserID();
                        if (currentimageId == imageId) {
                            imgMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
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
                                }
                            });
                        } else {
                            mPathList.add(imageContent.getLocalPath());
                            mMsgIdList.add(imgMessage.getMsgId() + "");
                            imgMessage.setMediaFilePath(imageContent.getLocalThumbnailPath());
                        }
                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mConvData.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                imgMessage.setTimeString(timeString);
                            }
                        } else {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            imgMessage.setTimeString(timeString);
                        }
                        mData.add(imgMessage);
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
                        }
                        String avatarMedialD = msg.getFromUser().getAvatar();
//                    UserInfo userInfo = msg.getFromUser();
//                    userInfo.getAvatarFile().getAbsolutePath();
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
                                }
                            });
                        }

                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mConvData.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                voiceMessage.setTimeString(timeString);
                            }
                        } else {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            voiceMessage.setTimeString(timeString);
                        }
                        mData.add(voiceMessage);
                        break;
                    case location:
                        break;
                    case video:
                        //处理视频消息
                        LogUtils.d("ChatRoomMessageEvent video", "第" + i + "个" + msg.getContent().toJson().toString());
                        final VideoContent videoContent = (VideoContent) msg.getContent();
                        videoContent.getVideoLocalPath();//视频文件本地地址
                        videoContent.getDuration();//语音文件时长
                        final MyMessage videoMessage = new MyMessage("", IMessage.MessageType.RECEIVE_VIDEO.ordinal());
                        long videoId = msg.getFromUser().getUserID();
                        msg.getFromID();
                        long currentVideoId = JMessageClient.getMyInfo().getUserID();
                        if (currentVideoId == videoId) {
                            videoMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
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
                            long preTime = mConvData.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                videoMessage.setTimeString(timeString);
                            }
                        } else {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            videoMessage.setTimeString(timeString);
                        }
                        mData.add(videoMessage);
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
                            }
                            fileMessage.setMediaFilePath(VideoMediaID + ".mp4");
                        } else {
                            switch (fileContent.getFormat()) {
                                case "jpg":
                                    long fileId = msg.getFromUser().getUserID();
                                    long currentFileId = JMessageClient.getMyInfo().getUserID();
                                    if (currentFileId == fileId) {
                                        fileMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
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
                                    }
                                    if (fileContent.getLocalPath() != null) {
                                        fileMessage.setMediaFilePath(fileContent.getLocalPath());
                                    } else {
                                        fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                                            @Override
                                            public void onComplete(int i, String s, File file) {
                                                fileMessage.setMediaFilePath(file.getAbsolutePath());
                                                VideoContent videoContent1 = (VideoContent) msg.getContent();
//                                            fileMessage.setMediaFilePath(videoContent1.getThumbLocalPath());
                                            }
                                        });
                                    }
                                    break;
                                case "png":
                                    long fileId2 = msg.getFromUser().getUserID();
                                    long currentFileId2 = JMessageClient.getMyInfo().getUserID();
                                    if (currentFileId2 == fileId2) {
                                        fileMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
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
////                                fileMessage.setType(IMessage.MessageType.RECEIVE_VOICE.ordinal());
////                                long fileId3 = msg.getFromUser().getUserID();
////                                long currentFileId3 = JMessageClient.getMyInfo().getUserID();
////                                if (currentFileId3 == fileId3) {
////                                    fileMessage.setType(IMessage.MessageType.SEND_VOICE.ordinal());
////                                }
////                                VoiceContent voiceContent1 = (VoiceContent) msg.getContent();
////                                fileMessage.setDuration(voiceContent1.getDuration());
////                                fileContent.downloadFile(msg, new DownloadCompletionCallback() {
////                                    @Override
////                                    public void onComplete(int i, String s, File file) {
////                                        fileMessage.setMediaFilePath(file.getAbsolutePath());
////                                    }
////                                });
//                                break;
                            }
                        }
                }
            }
        }
        return mData;
    }

    // 接收聊天室消息
    public void onEventMainThread(MessageEvent event) {
        Log.d("tag", "ChatRoomMessageEvent received .");
        mConv = JMessageClient.getSingleConversation(FORM_USER_NAME);
        mConvData = mConv.getAllMessage();
        final Message evMsg = event.getMessage();
        final MyMessage myMessage;
//        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mConvData.size(); i++) {
                    final Message msg = mConvData.get(i);
                    LogUtils.d("ChatRoomMessageEvent message", "第" + i + "个" + msg.getContent().toJson().toString());
                }
            }
        }).start();
        for (int i = 0; i < mConvData.size(); i++) {
            final Message msg = mConvData.get(i);
            //这个页面仅仅展示聊天室会话的消息
//            if (i > 47) {

            String s1 = msg.getFromUser().getSignature();
            String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
            DefaultUser defaultUser = new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), avatar);
            defaultUser.setSignature(msg.getFromUser().getSignature().length() >= 0 ? msg.getFromUser().getSignature() : "该用户很懒，没有设置签名");
            defaultUser.setHxUserId(msg.getFromID() + "");
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
                        }
//                    String s1 = msg.getFromUser().getAvatar();
                        textMessage.setUserInfo(defaultUser);
                        textMessage.setText(textContent.getText());
                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mConvData.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                textMessage.setTimeString(timeString);
                            }
                        } else {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            textMessage.setTimeString(timeString);
                        }
                        mData.add(textMessage);
                        mAdapter.addToStart(textMessage, true);
                    }
                    break;
                case image:
                    //处理图片消息
                    LogUtils.d("ChatRoomMessageEvent image", "第" + i + "个" + msg.getContent().toJson().toString());
                    final ImageContent imageContent = (ImageContent) msg.getContent();
                    imageContent.getLocalPath();//图片本地地址
                    imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                    long imageId = msg.getFromUser().getUserID();
                    msg.getFromID();
                    final MyMessage imgMessage = new MyMessage(null, IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                    long currentimageId = JMessageClient.getMyInfo().getUserID();
                    if (currentimageId == imageId) {
                        imgMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
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
                            }
                        });
                    } else {
                        mPathList.add(imageContent.getLocalPath());
                        mMsgIdList.add(imgMessage.getMsgId() + "");
                        imgMessage.setMediaFilePath(imageContent.getLocalThumbnailPath());
                    }
                    if (i > 0) {
                        long messageTime = msg.getCreateTime();
                        long preTime = mConvData.get(i - 1).getCreateTime();
                        long diff = messageTime - preTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            imgMessage.setTimeString(timeString);
                        }
                    } else {
                        long messageTime = msg.getCreateTime();
                        String timeString = DateUtils.formatFriendly(new Date(messageTime));
                        imgMessage.setTimeString(timeString);
                    }
                    mData.add(imgMessage);
                    mAdapter.addToStart(imgMessage, true);
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
                    }
                    String avatarMedialD = msg.getFromUser().getAvatar();
//                    UserInfo userInfo = msg.getFromUser();
//                    userInfo.getAvatarFile().getAbsolutePath();
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
                            }
                        });
                    }

                    if (i > 0) {
                        long messageTime = msg.getCreateTime();
                        long preTime = mConvData.get(i - 1).getCreateTime();
                        long diff = messageTime - preTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            voiceMessage.setTimeString(timeString);
                        }
                    } else {
                        long messageTime = msg.getCreateTime();
                        String timeString = DateUtils.formatFriendly(new Date(messageTime));
                        voiceMessage.setTimeString(timeString);
                    }
                    mData.add(voiceMessage);
                    mAdapter.addToStart(voiceMessage, true);
                    break;
                case location:
                    break;
                case video:
                    //处理视频消息
                    LogUtils.d("ChatRoomMessageEvent video", "第" + i + "个" + msg.getContent().toJson().toString());
                    final VideoContent videoContent = (VideoContent) msg.getContent();
                    videoContent.getVideoLocalPath();//视频文件本地地址
                    videoContent.getDuration();//语音文件时长
                    final MyMessage videoMessage = new MyMessage("", IMessage.MessageType.RECEIVE_VIDEO.ordinal());
                    long videoId = msg.getFromUser().getUserID();
                    msg.getFromID();
                    long currentVideoId = JMessageClient.getMyInfo().getUserID();
                    if (currentVideoId == videoId) {
                        videoMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
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
                        long preTime = mConvData.get(i - 1).getCreateTime();
                        long diff = messageTime - preTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            videoMessage.setTimeString(timeString);
                        }
                    } else {
                        long messageTime = msg.getCreateTime();
                        String timeString = DateUtils.formatFriendly(new Date(messageTime));
                        videoMessage.setTimeString(timeString);
                    }
                    mData.add(videoMessage);
                    mAdapter.addToStart(videoMessage, true);
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
                        }
                        fileMessage.setMediaFilePath(VideoMediaID + ".mp4");
                    } else {
                        switch (fileContent.getFormat()) {
                            case "jpg":
                                long fileId = msg.getFromUser().getUserID();
                                long currentFileId = JMessageClient.getMyInfo().getUserID();
                                if (currentFileId == fileId) {
                                    fileMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
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
                                }
                                if (fileContent.getLocalPath() != null) {
                                    fileMessage.setMediaFilePath(fileContent.getLocalPath());
                                } else {
                                    fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                                        @Override
                                        public void onComplete(int i, String s, File file) {
                                            fileMessage.setMediaFilePath(file.getAbsolutePath());
                                            VideoContent videoContent1 = (VideoContent) msg.getContent();
//                                            fileMessage.setMediaFilePath(videoContent1.getThumbLocalPath());
                                        }
                                    });
                                }
                                break;
                            case "png":
                                long fileId2 = msg.getFromUser().getUserID();
                                long currentFileId2 = JMessageClient.getMyInfo().getUserID();
                                if (currentFileId2 == fileId2) {
                                    fileMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
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
//                                VoiceContent voiceContent1 = (VoiceContent) msg.getContent();
//                                fileMessage.setDuration(voiceContent1.getDuration());
//                                fileContent.downloadFile(msg, new DownloadCompletionCallback() {
//                                    @Override
//                                    public void onComplete(int i, String s, File file) {
//                                        fileMessage.setMediaFilePath(file.getAbsolutePath());
//                                    }
//                                });
//                                break;
                        }
                    }

                    fileMessage.setUserInfo(defaultUser);
                    if (i > 0) {
                        long messageTime = msg.getCreateTime();
                        long preTime = mConvData.get(i - 1).getCreateTime();
                        long diff = messageTime - preTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            fileMessage.setTimeString(timeString);
                        }
                    } else {
                        long messageTime = msg.getCreateTime();
                        String timeString = DateUtils.formatFriendly(new Date(messageTime));
                        fileMessage.setTimeString(timeString);
                    }
                    mData.add(fileMessage);
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
                    mData.add(customMessage);
                    break;
                case unknown:
                    LogUtils.d("ChatRoomMessageEvent unknown", "第" + i + "个" + msg.getContent().toJson().toString());
                    break;
                case prompt:
                    break;
                default:
//                    initMsgAdapter();
                    break;
            }
        }
//        mAdapter.addToEndChronologically(mData);
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
                    Integer resId = getResources().getIdentifier(string.replace("R.drawable.", ""),
                            "drawable", getPackageName());

                    avatarImageView.setImageResource(resId);
                } else {
                    Glide.with(SingleChatActivity.this)
                            .load(string)
                            .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default))
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
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(string)
                        .apply(new RequestOptions().fitCenter().placeholder(R.drawable.aurora_picture_not_found))
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
                Glide.with(SingleChatActivity.this)
                        .asBitmap()
                        .load(uri)
                        // Resize image view by change override size.
                        .apply(new RequestOptions().frame(interval).override(200, 400))
                        .into(imageCover);
            }
        };

        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);

        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_VIDEO.ordinal()) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(SingleChatActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else if (message.getType() == IMessage.MessageType.RECEIVE_IMAGE.ordinal()
                        || message.getType() == IMessage.MessageType.SEND_IMAGE.ordinal()) {
                    Intent intent = new Intent(SingleChatActivity.this, BrowserImageActivity.class);
                    intent.putExtra("messageId", message.getMsgId());
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

//        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
//            @Override
//            public void onMessageLongClick(View view, MyMessage message) {
//                Toast.makeText(getApplicationContext(),
//                        getApplicationContext().getString(R.string.message_long_click_hint),
//                        Toast.LENGTH_SHORT).show();
//                // do something
//            }
//        });

//        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
//            @Override
//            public void onAvatarClick(MyMessage message) {
//                DefaultUser userInfo = (DefaultUser) message.getFromUser();
//                Toast.makeText(getApplicationContext(),
//                        getApplicationContext().getString(R.string.avatar_click_hint),
//                        Toast.LENGTH_SHORT).show();
//                // do something
//            }
//        });

        //消息状态视图点击，重新发送或下载
        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<MyMessage>() {
            @Override
            public void onStatusViewClick(MyMessage message) {
                // message status view click, resend or download here
            }
        });

        mAdapter.addToEndChronologically(mData);
        PullToRefreshLayout layout = mChatView.getPtrLayout();
        layout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PullToRefreshLayout layout) {
                Log.i("SingleChatActivity", "Loading next page");
                loadNextPage();
            }
        });
        // Deprecated, should use onRefreshBegin to load next page
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
//                Log.i("SingleChatActivity", "Loading next page");
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
//                Collections.reverse(list);
                // MessageList 0.7.2 add this method, add messages chronologically.
//                mAdapter.addToEndChronologically(list);
//                mChatView.getPtrLayout().refreshComplete();
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
        unregisterReceiver(mReceiver);
        mSensorManager.unregisterListener(this);
        JMessageClient.unRegisterEventReceiver(this);
    }
}


