package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.AppsAdapter;
import com.shanchain.shandata.adapter.SimpleAppsGridView;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.ChatView;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.utils.MyEmojiFilter;
import com.shanchain.shandata.utils.RequestCode;
import com.shanchain.shandata.widgets.XhsEmoticonsKeyBoard;
import com.shanchain.shandata.widgets.other.TipItem;
import com.shanchain.shandata.widgets.other.TipView;
import com.shanchain.shandata.widgets.photochoose.ChoosePhoto;
import com.shanchain.shandata.widgets.photochoose.PhotoUtils;
import com.shanchain.shandata.widgets.pickerimage.PickImageActivity;
import com.shanchain.shandata.widgets.pickerimage.utils.Extras;
import com.shanchain.shandata.widgets.pickerimage.utils.StorageType;
import com.shanchain.shandata.widgets.pickerimage.utils.StorageUtil;
import com.shanchain.shandata.widgets.pickerimage.utils.StringUtil;
import com.shanchain.shandata.widgets.takevideo.CameraActivity;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
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
import sj.keyboard.utils.imageloader.ImageBase;
import sj.keyboard.widget.EmoticonPageView;
import sj.keyboard.widget.FuncLayout;

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
    @Bind(R.id.btn_report)
    Button btnReport;
    //    private final static String FORM_USER_ID = "qwer";
    private String FORM_USER_ID;
    private String FORM_USER_NAME = "";
    private String title;
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
    private String hxUserId;
    private ChoosePhoto mChoosePhoto = new ChoosePhoto();


    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();
    private List<MyMessage> mData = new ArrayList<>();
    private List<Message> mConvData = new ArrayList<>();
    private XhsEmoticonsKeyBoard xhsEmoticonsKeyBoard;
    private boolean isShow = true;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_singer_chat_room;
    }

    @Override
    protected void initViewsAndEvents() {
//        JMessageClient.registerEventReceiver(this);
        mMyInfo = JMessageClient.getMyInfo();
        final Bundle bundle = getIntent().getExtras();
        userInfo = bundle.getParcelable("userInfo");
        if (bundle.getString("hxUserId") != null) {
            FORM_USER_ID = bundle.getString("hxUserId");
        } else {
            FORM_USER_ID = userInfo.getHxUserId();
        }

        if (userInfo != null) {
//            FORM_USER_NAME = userInfo.getDisplayName();
            mConv = JMessageClient.getSingleConversation(FORM_USER_ID);
            if (mConv == null) {
                mConv = Conversation.createSingleConversation(FORM_USER_ID);
            }
            JMessageClient.getUserInfo(FORM_USER_ID, new GetUserInfoCallback() {
                @Override
                public void gotResult(int i, String s, UserInfo userInfo) {
                    FORM_USER_NAME = userInfo.getNickname() != null ? userInfo.getNickname() : userInfo.getUserName();
                    initToolbar();
                }
            });
        } else {
            FORM_USER_ID = "qwer";
        }

        initToolbar();
//        initData();
//        pullToRefreshLayout = findViewById(R.id.pull_to_refresh_layout);
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();
        registerProximitySensorListener();
        mChatView = (ChatView) findViewById(R.id.activity_chat_view);
        mChatView.initModule();
        mChatView.isShowBtnInputJoin(false);
        if (FORM_USER_ID != null) {
            mData = getConversationMessages();
            initMsgAdapter();
        }
        xhsEmoticonsKeyBoard = findViewById(R.id.ek_bar);
        if (FORM_USER_ID.equals("123456")) {
            xhsEmoticonsKeyBoard.getXhsEmoticon().setVisibility(View.GONE);
        } else {
            xhsEmoticonsKeyBoard.getXhsEmoticon().setVisibility(View.VISIBLE);
        }
        initEmojiData();//初始化表情栏
        mReceiver = new HeadsetDetectReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mReceiver, intentFilter);
        mChatView.setOnTouchListener(this);

        //发送单聊消息
        xhsEmoticonsKeyBoard.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mcgContent = xhsEmoticonsKeyBoard.getEtChat().getText().toString();
                scrollToBottom();
                if (mcgContent.equals("")) {
                    return;
                }
                final Message msg;
                TextContent content = new TextContent(mcgContent);
                msg = mConv.createSendMessage(content);
                LogUtils.d("------->>>text message is: "+msg.toJson());
                //构造消息
                final MyMessage message = new MyMessage(mcgContent, IMessage.MessageType.SEND_TEXT.ordinal());
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
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String timeString = DateUtils.formatFriendly(new Date(messageTime));
                    message.setTimeString(timeString);
                }
                //设置用户和消息体Extras参数
                setMsgUserInfo(msg);
                mAdapter.addToStart(message, true);
                msg.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                        } else {
                            message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                        }
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.updateMessage(message);
                            }
                        });
                    }
                });
                JMessageClient.sendMessage(msg);

                xhsEmoticonsKeyBoard.getEtChat().setText("");
            }
        });


        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(final CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
                TextContent textContent = new TextContent(input.toString());
//                Map extrasMap = new HashMap();
//                extrasMap.put("userName", JMessageClient.getMyInfo().getUserName() + "");
//                extrasMap.put("conversationType", "single");
//                extrasMap.put("appkey",conversationMessage.getFromUser().getDisplayName()+"");
//                textContent.setExtras(extrasMap);
//                textContent.setStringExtra("userName", JMessageClient.getMyInfo().getUserName() + "");
//                textContent.setStringExtra("conversationType", "single");
                final Message conversationMessage = mConv.createSendMessage(textContent);
//                final Message conversationMessage = mConv.createSendTextMessage(textContent);
                final MyMessage myMessage = new MyMessage("" + input.toString(), IMessage.MessageType.SEND_TEXT.ordinal());
                //设置用户和消息体Extras参数
                setMsgUserInfo(conversationMessage);
                mAdapter.addToStart(myMessage, true);
                conversationMessage.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        String s1 = s;
                        int i1 = i;
                        if (i == 0) {
                            mMyInfo = JMessageClient.getMyInfo();
                            DefaultUser user = new DefaultUser(mMyInfo.getUserID(), mMyInfo.getDisplayName(), mMyInfo.getAvatar());
                            myMessage.setUserInfo(user);
//                            myMessage.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                            myMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            myMessage.setText(input.toString());
                            mData.add(myMessage);
                        } else {
                            ToastUtils.showToast(SingleChatActivity.this, "发送消息失败");
                            DefaultUser user = new DefaultUser(mMyInfo.getUserID(), mMyInfo.getDisplayName(), mMyInfo.getAvatar());
                            myMessage.setUserInfo(user);
                            myMessage.setText(input.toString());
                            myMessage.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                            myMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                        }
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.updateMessage(myMessage);
                            }
                        });

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
                mConv = JMessageClient.getSingleConversation(FORM_USER_ID);
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
                        final Message msg = mConv.createSendFileMessage(videoFile, item.getFileName());
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setMediaFilePath(item.getFilePath());
                        message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                        //设置用户和消息体Extras参数
                        setMsgUserInfo(msg);
                        mAdapter.addToStart(message, true);
                        final MyMessage finalMsg = message;
                        msg.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    finalMsg.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                } else {
                                    finalMsg.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                }
                                SingleChatActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.updateMessage(finalMsg);
                                    }
                                });
                            }
                        });
                        JMessageClient.sendMessage(msg);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        final MyMessage myMessage = message;
                        myMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                        SingleChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.updateMessage(myMessage);
                            }
                        });
                    } catch (JMFileSizeExceedException e) {
                        e.printStackTrace();
                        final MyMessage myMessage = message;
                        myMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                        SingleChatActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.updateMessage(myMessage);
                            }
                        });
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

        //发送语音
        xhsEmoticonsKeyBoard.getBtnVoice().initConv(mConv, mAdapter, mChatView);

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
                mConv = JMessageClient.getSingleConversation(FORM_USER_ID);
                if (mConv == null) {
                    mConv = Conversation.createSingleConversation(FORM_USER_ID);
                }
                final Message msg;
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE.ordinal());
                try {
//                    msg = mConv.createSendVoiceMessage(voiceFile, duration);
                    VoiceContent voiceContent = new VoiceContent(voiceFile, duration);
                    msg = mConv.createSendMessage(voiceContent);
                    message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                    message.setMediaFilePath(voiceFile.getPath());
                    message.setDuration(duration);
                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    //设置用户和消息体Extras参数
                    setMsgUserInfo(msg);
                    mAdapter.addToStart(message, true);
                    msg.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            int i1 = i;
                            String s1 = s;
                            if (i == 0) {
                                message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                            } else {
                                message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                            }
                            SingleChatActivity.this.runOnUiThread(new Runnable() {
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

        //发送视频文件
        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                final Message msg;
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                if (mConv != null) {
                    try {
                        msg = mConv.createSendImageMessage(new File(photoPath));
                        UserInfo userInfo = msg.getFromUser();
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setMediaFilePath(photoPath);
                        mPathList.add(photoPath);
                        mMsgIdList.add(message.getMsgId());
                        message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getUserName(), msg.getFromUser().getAvatarFile().getAbsolutePath()));
                        //设置用户和消息体Extras参数
                        setMsgUserInfo(msg);
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

    private void setMsgUserInfo(Message msg) {
        UserInfo msgUserInfo = msg.getFromUser();
        MessageContent messageContent = msg.getContent();
        if (msgUserInfo != null) {
            msgUserInfo.setUserExtras("appkey", MyApplication.JM_APP_KEY);
            msgUserInfo.setUserExtras("userName", JMessageClient.getMyInfo().getUserName() + "");
            msgUserInfo.setUserExtras("conversationType", "single");
        }
        if (messageContent != null) {
            messageContent.setStringExtra("userName", JMessageClient.getMyInfo().getUserName() + "");
            messageContent.setStringExtra("appkey", MyApplication.JM_APP_KEY);
            messageContent.setStringExtra("conversationType", "single");
        }
    }

    private void initToolbar() {
        mTbMain.setTitleTextColor(getResources().getColor(R.color.colorTextDefault));
        mTbMain.isShowChatRoom(false);//不在导航栏显示聊天室信息
        mTbMain.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        mTbMain.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTbMain.getTitleView().setLayoutParams(layoutParams);
        mTbMain.setTitleText(FORM_USER_NAME);
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
        if (isShow == true) {
            btnReport.setVisibility(View.VISIBLE);
            isShow = false;
        } else {
            btnReport.setVisibility(View.GONE);
            isShow = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_report)
    public void onViewClicked() {
        Intent intent = new Intent(SingleChatActivity.this, ReportActivity.class);
        intent.putExtra("targetId", "" + FORM_USER_ID);
        startActivity(intent);
        btnReport.setVisibility(View.GONE);
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
        mConv = JMessageClient.getSingleConversation(FORM_USER_ID);
        if (mConv == null) {
            mConv = Conversation.createSingleConversation(FORM_USER_ID);
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
                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mConvData.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String timeString = sdf.format(new Date(messageTime));
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                myMessage.setTimeString(timeString);
                            }
                        } else if (i == mConvData.size() - 1) {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                            imgMessage.setTimeString(timeString);
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
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String timeString = sdf.format(new Date(messageTime));
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                imgMessage.setTimeString(timeString);
                            }
                        } else if (i == mConvData.size() - 1) {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                            imgMessage.setTimeString(timeString);
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
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String timeString = sdf.format(new Date(messageTime));
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                voiceMessage.setTimeString(timeString);
                            }
                        } else if (i == mConvData.size() - 1) {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                            imgMessage.setTimeString(timeString);
                        }
                        mData.add(voiceMessage);
                        break;
                    case location:
                        break;
                    case video:
                        //处理视频消息
                        LogUtils.d("ChatRoomMessageEvent video", "第" + i + "个" + msg.getContent().toJson().toString());
                        final VideoContent videoContent = (VideoContent) msg.getContent();
                        /*videoContent.getVideoLocalPath();//视频文件本地地址
                        videoContent.getDuration();//语音文件时长*/
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
                            LogUtils.d("---->>>message video local path: "+videoContent.getThumbLocalPath());
                            videoMessage.setDuration(videoContent.getDuration());
                        } else {
                            videoContent.downloadThumbImage(msg, new DownloadCompletionCallback() {
                                @Override
                                public void onComplete(int i, String s, File file) {
                                    videoMessage.setMediaFilePath(file.getAbsolutePath());
                                    videoMessage.setDuration(videoContent.getDuration());
                                }
                            });

                            videoContent.downloadVideoFile(msg, new DownloadCompletionCallback() {
                                @Override
                                public void onComplete(int i, String s, File file) {
                                    videoMessage.setMediaFilePath(file.getAbsolutePath());
                                    LogUtils.d("---->>>message video path: "+file.getAbsolutePath());
                                }
                            });
                        }
                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mConvData.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String timeString = sdf.format(new Date(messageTime));
//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                videoMessage.setTimeString(timeString);
                            }
                        } else if (i == mConvData.size() - 1) {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                            imgMessage.setTimeString(timeString);
                        }
                        mData.add(videoMessage);
                        break;
                    case custom:
                        break;
                    case file:
                        LogUtils.d("ChatRoomMessageEvent file", "第" + i + "个" + msg.getContent().toJson().toString());
                        final FileContent fileContent = (FileContent) msg.getContent();
                        String format = fileContent.getFormat();
                        final MyMessage fileMessage = new MyMessage("", IMessage.MessageType.RECEIVE_FILE.ordinal());
                        if (format == null) {
                            Map hashMap = fileContent.getStringExtras();
                            hashMap.get("video");
                            fileMessage.setType(IMessage.MessageType.RECEIVE_VIDEO.ordinal());
                            long fileId1 = msg.getFromUser().getUserID();
                            long currentFileId1 = JMessageClient.getMyInfo().getUserID();
                            if (currentFileId1 == fileId1) {
                                fileMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
                            }
                            fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                                @Override
                                public void onComplete(int i, String s, File file) {
                                    fileMessage.setMediaFilePath(file.getAbsolutePath());
                                    LogUtils.d("---->>>message file path: "+file.getAbsolutePath());
                                    //获取文件的时长
                                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                                    media.setDataSource(file.getAbsolutePath());
                                    String duration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                    LogUtils.d("---->>>message file duration: "+duration);
                                    if(!TextUtils.isEmpty(duration)){
                                        fileMessage.setDuration(Long.parseLong(duration)/1000);
                                    }
                                }
                            });
                            fileMessage.setUserInfo(defaultUser);
                            if (i > 0) {
                                long messageTime = msg.getCreateTime();
                                long preTime = mConvData.get(i - 1).getCreateTime();
                                long diff = messageTime - preTime;
                                if (diff > 3 * 60 * 1000) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String timeString = sdf.format(new Date(messageTime));
                                    fileMessage.setTimeString(timeString);
                                }
                            } else if (i == mConvData.size() - 1) {
                                long messageTime = msg.getCreateTime();
//                            imgMessage.setTimeString(timeString);
                            }
                            mData.add(fileMessage);
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
//                                        ImageContent imageContent1 = (ImageContent) msg.getContent();
//                                        fileMessage.setMediaFilePath(imageContent1.getLocalThumbnailPath());
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
                                    mData.add(fileMessage);
                                    break;
                            }
                        }
                }
            }
        }
        return mData;
    }

    // 接收聊天室消息
    public void onEventMainThread(MessageEvent event) {
        Log.d("tag", "ChatRoomMessageEvent received ."+event.getMessage().toJson());
        mConv = JMessageClient.getSingleConversation(FORM_USER_ID);
        if (mConv != null) {
            mConv.setUnReadMessageCnt(0);
        }
        mConvData = mConv.getAllMessage();
        final Message msg = event.getMessage();
        //这个页面仅仅展示聊天室会话的消息
        String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
        DefaultUser defaultUser = new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), avatar);
        defaultUser.setSignature(msg.getFromUser().getSignature().length() >= 0 ? msg.getFromUser().getSignature() : "该用户很懒，没有设置签名");
        defaultUser.setHxUserId(msg.getFromID() + "");
        switch (msg.getContentType()) {
            case text:
                LogUtils.d("ChatRoomMessageEvent text", msg.getContent().toJson().toString());
                TextContent textContent = (TextContent) msg.getContent();
                if (textContent.getText() != null && !TextUtils.isEmpty(textContent.getText())) {
                    MyMessage textMessage = new MyMessage(textContent.getText(), IMessage.MessageType.RECEIVE_TEXT.ordinal());
                    textMessage.setMessageStatus(IMessage.MessageStatus.RECEIVE_SUCCEED);
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
                    for (int i = 0; i < mConvData.size(); i++) {
                        if (i > 0) {
                            long messageTime = msg.getCreateTime();
                            long preTime = mConvData.get(i - 1).getCreateTime();
                            long diff = messageTime - preTime;
                            if (diff > 3 * 60 * 1000) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String timeString = sdf.format(new Date(messageTime));//                                String timeString = DateUtils.formatFriendly(new Date(messageTime));
                                textMessage.setTimeString(timeString);
                            }
                        } else {
                            long messageTime = msg.getCreateTime();
                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                            textMessage.setTimeString(timeString);
                        }
                    }
//                    mData.add(textMessage);
                    mAdapter.addToStart(textMessage, true);
                }
                break;
            case image:
                //处理图片消息
                LogUtils.d("ChatRoomMessageEvent image", msg.getContent().toJson().toString());
                final ImageContent imageContent = (ImageContent) msg.getContent();
                long imageId = msg.getFromUser().getUserID();
                msg.getFromID();
                final MyMessage imgMessage = new MyMessage(null, IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                long currentimageId = JMessageClient.getMyInfo().getUserID();
                if (currentimageId == imageId) {
                    imgMessage.setType(IMessage.MessageType.SEND_IMAGE.ordinal());
                    imgMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                }
                imgMessage.setUserInfo(defaultUser);
                String mediaID = imageContent.getMediaID();
                for (int i = 0; i < mConvData.size(); i++) {
                    if (i > 0) {
                        long messageTime = msg.getCreateTime();
                        long preTime = mConvData.get(i - 1).getCreateTime();
                        long diff = messageTime - preTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String timeString = sdf.format(new Date(messageTime));
//                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            imgMessage.setTimeString(timeString);
                        }
                    } else {
                        long messageTime = msg.getCreateTime();
                        String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                        imgMessage.setTimeString(timeString);
                    }
                }
//                mData.add(imgMessage);
                mAdapter.addToStart(imgMessage, true);
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
                break;
            case voice:
                //处理语音消息
                LogUtils.d("ChatRoomMessageEvent voice", msg.getContent().toJson().toString());
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
                voiceMessage.setUserInfo(defaultUser);
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
                for (int i = 0; i < mConvData.size(); i++) {
                    if (i > 0) {
                        long messageTime = msg.getCreateTime();
                        long preTime = mConvData.get(i - 1).getCreateTime();
                        long diff = messageTime - preTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String timeString = sdf.format(new Date(messageTime));
//                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            voiceMessage.setTimeString(timeString);
                        }
                    } else {
                        long messageTime = msg.getCreateTime();
                        String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                        voiceMessage.setTimeString(timeString);
                    }
                }
                mAdapter.addToStart(voiceMessage, true);
                break;
            case location:
                break;
            case video:
                //处理视频消息
                LogUtils.d("ChatRoomMessageEvent video", msg.getContent().toJson().toString());
                final VideoContent videoContent = (VideoContent) msg.getContent();
                /*videoContent.getVideoLocalPath();//视频文件本地地址
                videoContent.getDuration();//语音文件时长*/
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
                for (int i = 0; i < mConvData.size(); i++) {
                    if (i > 0) {
                        long messageTime = msg.getCreateTime();
                        long preTime = mConvData.get(i - 1).getCreateTime();
                        long diff = messageTime - preTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String timeString = sdf.format(new Date(messageTime));
//                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            videoMessage.setTimeString(timeString);
                        }
                    } else {
                        long messageTime = msg.getCreateTime();
                        String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                        videoMessage.setTimeString(timeString);
                    }
                }
//                mData.add(videoMessage);
                mAdapter.addToStart(videoMessage, true);
                break;
            case file:
                LogUtils.d("ChatRoomMessageEvent file", msg.getContent().toJson().toString());
                final FileContent fileContent = (FileContent) msg.getContent();
                String format = fileContent.getFormat();
                final MyMessage fileMessage = new MyMessage("", IMessage.MessageType.RECEIVE_IMAGE.ordinal());
                if (format == null) {
                    Map hashMap = fileContent.getStringExtras();
                    hashMap.get("video");
                    fileMessage.setType(IMessage.MessageType.RECEIVE_VIDEO.ordinal());
                    long fileId1 = msg.getFromUser().getUserID();
                    long currentFileId1 = JMessageClient.getMyInfo().getUserID();
                    if (currentFileId1 == fileId1) {
                        fileMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
                        fileMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                    }
//                    fileMessage.setMediaFilePath(VideoMediaID + ".mp4");
                    fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                        @Override
                        public void onComplete(int i, String s, File file) {
                            fileMessage.setMediaFilePath(file.getAbsolutePath());
                            LogUtils.d("---->>>even message file path: "+"i="+i+":s="+s+"----"+file.getAbsolutePath());
                        }
                    });
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
                                        mAdapter.updateMessage(fileMessage);
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
                            if (fileContent.getLocalPath() != null) {
                                fileMessage.setMediaFilePath(fileContent.getLocalPath());
                            } else {
                                fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                                    @Override
                                    public void onComplete(int i, String s, File file) {
                                        fileMessage.setMediaFilePath(file.getAbsolutePath());
                                        VideoContent videoContent1 = (VideoContent) msg.getContent();
                                        fileMessage.setMediaFilePath(videoContent1.getThumbLocalPath());
                                        mAdapter.updateMessage(fileMessage);
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
                                        mAdapter.updateMessage(fileMessage);
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
                for (int i = 0; i < mConvData.size(); i++) {
                    if (i > 0) {
                        long messageTime = msg.getCreateTime();
                        long preTime = mConvData.get(i - 1).getCreateTime();
                        long diff = messageTime - preTime;
                        if (diff > 3 * 60 * 1000) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String timeString = sdf.format(new Date(messageTime));
//                            String timeString = DateUtils.formatFriendly(new Date(messageTime));
                            fileMessage.setTimeString(timeString);
                        }
                    } else {
                        long messageTime = msg.getCreateTime();
                        String timeString = DateUtils.formatFriendly(new Date(messageTime));
//                        fileMessage.setTimeString(timeString);
                    }
                }
//                mData.add(fileMessage);
                LogUtils.d("-------->>>message info: "+fileMessage.toString());
                mAdapter.addToStart(fileMessage, true);
            case eventNotification:
                break;
            case custom:
                break;
            case unknown:
                LogUtils.d("ChatRoomMessageEvent unknowmAdapter.addToStart(fileMessage, true);n", msg.getContent().toJson().toString());
                break;
            case prompt:
                break;
            default:
                break;
        }
//        }
//        mAdapter.addToEndChronologically(mData);
//        initMsgAdapter();
    }

    //聊天室输入框多功能界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThreadByType(AppsAdapter.ImageEvent event) {
        Intent intent;
        if (event.getContext() == SingleChatActivity.this) {
            switch (event.getFlag()) {
                case MyApplication.IMAGE_MESSAGE:
                    int from = PickImageActivity.FROM_LOCAL;
                    int requestCode = RequestCode.PICK_IMAGE;
                    if (ContextCompat.checkSelfPermission(SingleChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
                    } else {
                        PickImageActivity.start(SingleChatActivity.this, requestCode, from, tempFile(), true, 1,
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
                        intent = new Intent(SingleChatActivity.this, CameraActivity.class);
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
                        intent = new Intent(SingleChatActivity.this, CameraActivity.class);
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
    }

    private String tempFile() {
        String filename = StringUtil.get32UUID() + ".jpg";
        return StorageUtil.getWritePath(filename, StorageType.TYPE_TEMP);
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
                    Glide.with(getApplicationContext())
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
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(uri)
                        // Resize image view by change override size.
                        .apply(new RequestOptions().frame(interval).override(200, 400))
                        .into(imageCover);
            }
        };

        // Use default layout

        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        //设置系统消息样式
        holdersConfig.setEventMessage(CustomEvenMsgHolder.class, R.layout.item_custom_event_message);
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
                    if (mMsgIdList.indexOf(message.getMsgId()) != -1) {
                        Intent intent = new Intent(SingleChatActivity.this, BrowserImageActivity.class);
                        intent.putExtra("messageId", message.getMsgId());
                        intent.putStringArrayListExtra("pathList", mPathList);
                        intent.putStringArrayListExtra("idList", mMsgIdList);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(mContext, "图片正在加载中，请滑动页面刷新");
                    }

                } else {
//                    Toast.makeText(getApplicationContext(),
//                            getApplicationContext().getString(R.string.message_click_hint),
//                            Toast.LENGTH_SHORT).show();
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
                    new TipView.Builder(SingleChatActivity.this, mChatView, (int) OldListX + view.getWidth() / 2, (int) OldListY + view.getHeight())
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
                                                ClipboardManager clipboard = (ClipboardManager) SingleChatActivity.this
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
                                            Toast.makeText(SingleChatActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SingleChatActivity.this, "只支持复制文字", Toast.LENGTH_SHORT).show();
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

        //头像点击事件
        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                if(userInfo.getUserType()>0)return;//如果是客服账号，直接返回
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
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.TAKE_PHOTO:
                if (data != null) {
                    final String photoPath = data.getStringExtra("take_photo");
                    Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                    final Message msg;
                    final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                    if (mConv != null) {
                        try {
//                            msg = mConv.createSendImageMessage(new File(photoPath));
                            ImageContent imageContent = new ImageContent(new File(photoPath));
                            msg = mConv.createSendMessage(imageContent);
                            message.setMediaFilePath(photoPath);
                            mPathList.add(photoPath);
                            mMsgIdList.add(message.getMsgId());
                            String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
                            message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getUserName(), avatar));
                            //设置用户和消息体Extras参数
                            setMsgUserInfo(msg);
                            mAdapter.addToStart(message, true);
                            msg.setOnSendCompleteCallback(new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                    } else {
                                        message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                    }
                                    SingleChatActivity.this.runOnUiThread(new Runnable() {
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
            case RequestCode.TAKE_VIDEO:
                if (data != null) {
                    String path = data.getStringExtra("video");
//                    long videoDuration = data.getLongExtra("duration", 0);
                    final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
//                    message.setDuration(videoDuration);
                    File videoFile = new File(path);
                    try {
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
//                        media.setDataSource(path);
                        media.setDataSource(videoFile.getAbsolutePath());
                        Bitmap bitmap = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        long videoDuration = Long.parseLong(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                        VideoContent video = new VideoContent(bitmap, "mp4", videoFile, videoFile.getName(), (int) (videoDuration/1000));
                        final Message msg = mConv.createSendMessage(video);
                        message.setMediaFilePath(path);
                        message.setDuration(videoDuration/1000);
                        String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
                        message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getNickname(), avatar));
                        //设置用户和消息体Extras参数
                        setMsgUserInfo(msg);
                        mAdapter.addToStart(message, true);
                        msg.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                } else {
                                    message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                }
                                SingleChatActivity.this.runOnUiThread(new Runnable() {
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
//                    ToastUtils.showToastLong(SingleChatActivity.this, data.getDataString());
                    final String photoPath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                    Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                    final Message msg;
                    final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE.ordinal());
                    if (mConv != null) {
                        try {
//                            msg = mConv.createSendImageMessage(new File(photoPath));
                            ImageContent imageContent = new ImageContent(new File(photoPath));
                            msg = mConv.createSendMessage(imageContent);
//                            message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                            message.setMediaFilePath(photoPath);
                            mPathList.add(photoPath);
                            mMsgIdList.add(message.getMsgId());
                            String avatar = msg.getFromUser().getAvatarFile() != null ? msg.getFromUser().getAvatarFile().getAbsolutePath() : "";
                            message.setUserInfo(new DefaultUser(msg.getFromUser().getUserID(), msg.getFromUser().getUserName(), avatar));
                            //设置用户和消息体Extras参数
                            setMsgUserInfo(msg);
                            mAdapter.addToStart(message, true);
                            msg.setOnSendCompleteCallback(new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        message.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED);
                                    } else {
                                        message.setMessageStatus(IMessage.MessageStatus.SEND_FAILED);
                                    }
                                    ThreadUtils.runOnMainThread(new Runnable() {
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
                mChoosePhoto.photoUtils.onActivityResult(SingleChatActivity.this, requestCode, resultCode, data);
                break;
        }
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


