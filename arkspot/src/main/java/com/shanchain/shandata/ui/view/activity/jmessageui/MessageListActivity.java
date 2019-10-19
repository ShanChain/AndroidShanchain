package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.RoleManager;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.ImageUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.AppsAdapter;
import com.shanchain.shandata.adapter.ImagePickerAdapter;
import com.shanchain.shandata.adapter.SimpleAppsGridView;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.db.ConversationEntryDao;
import com.shanchain.shandata.db.MessageEntryDao;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.ConversationEntry;
import com.shanchain.shandata.ui.model.MessageEntry;
import com.shanchain.shandata.ui.model.ModifyUserInfo;
import com.shanchain.shandata.ui.model.SearchGroupTeamBeam;
import com.shanchain.shandata.ui.model.ShareBean;
import com.shanchain.shandata.ui.presenter.MessageListPresenter;
import com.shanchain.shandata.ui.presenter.impl.MessageListPresenterImpl;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.ChatView;
import com.shanchain.shandata.ui.view.activity.jmessageui.view.MessageListView;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.ui.view.activity.square.PayforSuccessActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskDetailActivity;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.shandata.utils.ManagerUtils;
import com.shanchain.shandata.utils.MyEmojiFilter;
import com.shanchain.shandata.utils.RequestCode;
import com.shanchain.shandata.widgets.XhsEmoticonsKeyBoard;
import com.shanchain.shandata.widgets.arcMenu.ArcMenu;
import com.shanchain.shandata.widgets.other.TipItem;
import com.shanchain.shandata.widgets.other.TipView;
import com.shanchain.shandata.widgets.photochoose.PhotoUtils;
import com.shanchain.shandata.widgets.pickerimage.PickImageActivity;
import com.shanchain.shandata.widgets.pickerimage.utils.Extras;
import com.shanchain.shandata.widgets.pickerimage.utils.StorageType;
import com.shanchain.shandata.widgets.pickerimage.utils.StorageUtil;
import com.shanchain.shandata.widgets.pickerimage.utils.StringUtil;
import com.shanchain.shandata.widgets.takevideo.CameraActivity;

import org.greenrobot.eventbus.EventBus;
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
import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VideoContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.ChatRoomInfo;
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
import sj.keyboard.utils.EmoticonsKeyboardUtils;
import sj.keyboard.utils.imageloader.ImageBase;
import sj.keyboard.widget.EmoticonPageView;
import sj.keyboard.widget.FuncLayout;

public class MessageListActivity extends BaseActivity implements View.OnTouchListener,
        EasyPermissions.PermissionCallbacks,
        SensorEventListener,
        ArthurToolBar.OnRightClickListener,
        ArthurToolBar.OnFavoriteClickListener,
        ImagePickerAdapter.OnRecyclerViewItemClickListener ,
        MessageListView {


    private final static String TAG = "MessageListActivity";
    private final int RC_RECORD_VOICE = 0x0001;
    private final int RC_CAMERA = 0x0002;
    private final int RC_PHOTO = 0x0003;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int IMAGE_PICKER = 1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    public static final String JPG = ".jpg";
    //百度地图
    private com.shanchain.data.common.ui.widgets.CustomDialog mDialog;

    private ArthurToolBar mTbMain;
    private DrawerLayout drawer;
    private TextView userNikeView;
    private TextView tvUserSign;
    private ImageView ivUserModify;
    private ImageView userHeadView;
    private RecyclerView recyclerView;
    private String roomID;
    private String digistId;
    private String newRoomId, roomName, mImgPath;
    private ArrayList<String> photos = new ArrayList<>();
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
    private ArcMenu.OnMenuItemClickListener onMenuItemClickListener;
    private boolean mShowSoftInput = false;
    private boolean isRestartActivity = false;
    private int memberCount;


    /**
     * Store all image messages' path, pass it to {@link BrowserImageActivity},
     * so that click image message can browser all images.
     */
    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<String> mMsgIdList = new ArrayList<>();
    private List<MyMessage> messageList = new ArrayList<>();
    private List<MyMessage> mEventMessageList = new ArrayList<>();
    private Conversation chatRoomConversation;

    private TextView roomNum;
    private RelativeLayout relativeChatRoom;
    private XhsEmoticonsKeyBoard xhsEmoticonsKeyBoard;
    private boolean isIn, isHotChatRoom;
    private String isSuper;
    private List<Message> mMsgs;
    private MessageEntryDao mEntryDao;
    private List<MessageEntry> mMessageEntryList;
    private boolean mIsHasRoom;
    private MessageEntryDao mMessageEntryDao;
    private com.shanchain.data.common.ui.widgets.CustomDialog sureDialog;
    private CustomDialog shareBottomDialog;
    private ShareParams redPaperParams;
    private MessageListPresenter mPresenter;
    private SearchGroupTeamBeam mSearchGroupTeamBeam;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            String toastMsg = (String) msg.obj;
            Toast.makeText(MessageListActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
            closeLoadingDialog();
            shareBottomDialog.dismiss();
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
        //注册相关的硬件服务
        registerProximitySensorListener();
        mArcMenu.setVisibility(View.GONE);

        mPresenter = new MessageListPresenterImpl(this);

        initToolBar(roomID);

        initChatView();
        initEmojiData();
        initMsgAdapter();
        initData();
        initShareListener();
        loadMessageData(roomID);
        mArcMenu.setOnMenuItemClickListener(onMenuItemClickListener);

    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        Intent intent = getIntent();
        roomID = intent.getStringExtra("roomId");
        LogUtils.d("roomId", roomID + "");
        roomName = intent.getStringExtra("roomName");
        digistId = intent.getStringExtra("digistId");
//        isIn = intent.getBooleanExtra("isInCharRoom", true);
        isHotChatRoom = intent.getBooleanExtra("isHotChatRoom", false);
//        JMessageClient.registerEventReceiver(this);
        //进入聊天室
        showLoadingDialog();
        enterChatRoom();
        super.onCreate(savedInstanceState);
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
            ToastUtils.showToast(MessageListActivity.this, R.string.join_chat_room_failed);
            finish();
            closeLoadingDialog();
        }
    }

    //初始化聊天数据
    private void initData() {
        //获取是否是超级用户
        mPresenter.checkUserIsSuper();
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
                }
                break;
            case RequestCode.TAKE_VIDEO:
                if (data != null) {
                    String path = data.getStringExtra("video");
                    final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO.ordinal());
                    File videoFile = new File(path);
                    try {
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(videoFile.getAbsolutePath());
                        Bitmap bitmap = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        long videoDuration = Long.parseLong(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                        VideoContent video = new VideoContent(bitmap, "mp4", videoFile, videoFile.getName(), (int) (videoDuration/1000));
                        final Message msg = chatRoomConversation.createSendMessage(video);
//                            Message msg = chatRoomConversation.createSendFileMessage(videoFile, item.getFileName());
                        message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        message.setMediaFilePath(path);
                        message.setDuration((videoDuration/1000));
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


    private void initToolBar(final String roomId) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mTbMain = (ArthurToolBar) findViewById(R.id.tb_main);
        mTbMain.setTitleText(roomName);//聊天室名字
        mTbMain.setTitleTextSize(14);
        /*final UserInfo userInfo = JMessageClient.getMyInfo();
        if (userInfo != null && userInfo.getAvatarFile() != null) {
            mTbMain.setUserHeadImg(MessageListActivity.this, userInfo.getAvatarFile().getAbsolutePath());
        } else {
            mTbMain.setUserHeadImg(MessageListActivity.this, SCCacheUtils.getCacheHeadImg());
        }*/
        mTbMain.setUserHeadImgDefault(R.mipmap.message_group_icon);
        mTbMain.setFavoriteImage(R.mipmap.share);
        mTbMain.setRightImage(R.mipmap.fb_close);
        mTbMain.isShowChatRoom(true);
        mTbMain.setOnRightClickListener(this);
        mTbMain.setOnFavoriteClickListener(this);
        mTbMain.isShowChatRoom(true);//显示聊天室成员信息
        roomNum = mTbMain.findViewById(R.id.mRoomNum);
        roomNum.setTextColor(getResources().getColor(R.color.colorViolet));
        relativeChatRoom = mTbMain.findViewById(R.id.relative_chatRoom);
        if(isHotChatRoom){//ARS点击过来的显示api的人数
//            mTbMain.setFavoriteImageVisible(View.VISIBLE);
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
                        if (roomName.equals(getString(R.string.entrychat_room))) {
                            roomNum.setText("16968");
                        } else {
                            roomNum.setText("" + memberCount);
                        }
                    }
                }
            });
        }else {
            //查询矿区人数接口
            mPresenter.queryMineRoomNums(roomId);
        }
        mTbMain.setOnUserHeadClickListener(new ArthurToolBar.OnUserHeadClickListener() {
            @Override
            public void onUserHeadClick(View v) {
                //跳转聊天室人员列表
                Intent intent = new Intent(MessageListActivity.this, MemberActivity.class);
                intent.putExtra("roomId", roomID);
                intent.putExtra("count", memberCount);
                intent.putExtra("digistId",digistId);
                intent.putExtra("isHotChatRoom",isHotChatRoom);
                startActivity(intent);
            }
        });
        //初始化分享配置
        shareBottomDialog = new CustomDialog(MessageListActivity.this,
                true, true, 1.0,
                R.layout.layout_bottom_share, new int[]{R.id.share_image,
                R.id.mRlWechat, R.id.mRlWeixinCircle, R.id.mRlQQ, R.id.mRlWeibo, R.id.share_close});
        shareBottomDialog.setCanceledOnTouchOutside(true);
    }


    private PlatActionListener shareListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            if (handler != null) {
                android.os.Message message = handler.obtainMessage();
                message.obj = "分享成功";
                handler.sendMessage(message);
            }

        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            Logger.e(TAG, "error:" + errorCode + ",msg:" + error);
            if (handler != null) {
                android.os.Message message = handler.obtainMessage();
                message.obj = "分享失败:" + error.getMessage() + "---" + errorCode;
                handler.sendMessage(message);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (handler != null) {
                android.os.Message message = handler.obtainMessage();
                message.obj = "分享取消";
                handler.sendMessage(message);
            }
        }
    };
    //分享监听
    private void initShareListener(){
        shareBottomDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                String shareUrl = HttpApi.BASE_URL_WALLET+"/join?"+"inviteUserId="+mSearchGroupTeamBeam.getCreateUser()+"&diggingsId="+digistId
                        +"&inviteCode="+mSearchGroupTeamBeam.getInviteCode();
                redPaperParams = new ShareParams();
                redPaperParams.setTitle(getString(R.string.app_name));
                redPaperParams.setText(getString(R.string.invate_y_join));
                redPaperParams.setUrl(shareUrl);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
                redPaperParams.setImageData(bitmap);
                switch (view.getId()) {
                    case R.id.mRlWechat://微信
                        if(!ManagerUtils.uninstallSoftware(MessageListActivity.this,"com.tencent.mm")){
                            ToastUtils.showToast(MessageListActivity.this, R.string.install_wechat);
                            return;
                        }
                        showLoadingDialog();
                        redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                        //调用分享接口share ，分享到微信平台。
                        JShareInterface.share(Wechat.Name, redPaperParams, shareListener);
                        break;
                    case R.id.mRlWeixinCircle://朋友圈
                        if(!ManagerUtils.uninstallSoftware(MessageListActivity.this,"com.tencent.mm")){
                            ToastUtils.showToast(MessageListActivity.this, R.string.install_wechat);
                            return;
                        }
                        showLoadingDialog();
                        redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                        //调用分享接口share ，分享到朋友圈平台。
                        JShareInterface.share(WechatMoments.Name, redPaperParams, shareListener);
                        break;
                    case R.id.mRlQQ://QQ
                        if(!ManagerUtils.uninstallSoftware(MessageListActivity.this,"com.tencent.mobileqq")){
                            ToastUtils.showToast(MessageListActivity.this, R.string.install_qq);
                            return;
                        }
                        showLoadingDialog();
                        redPaperParams.setShareType(Platform.SHARE_WEBPAGE);
                        //调用分享接口share ，分享到QQ平台。
                        JShareInterface.share(QQ.Name, redPaperParams, shareListener);
                        break;
                    case R.id.share_close:
                        shareBottomDialog.dismiss();
                        break;

                }
            }
        });

        shareBottomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                closeLoadingDialog();
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
//                readyGo(FootPrintActivity.class);
                finish();
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
        super.onBackPressed();
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


    @Override
    public void setQueryMineRoomNumsResponse(String response) {
        String code = SCJsonUtils.parseCode(response);
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE_NEW)) {
            String data = JSONObject.parseObject(response).getString("data");
            List<SearchGroupTeamBeam> mList = JSONObject.parseArray(data, SearchGroupTeamBeam.class);
            mSearchGroupTeamBeam = mList.get(0);
            if(mSearchGroupTeamBeam == null)return;
            memberCount = mSearchGroupTeamBeam.gettDiggingJoinLogs().size();
            roomNum.setText(mSearchGroupTeamBeam.gettDiggingJoinLogs().size()+"");
            if(!TextUtils.isEmpty(mSearchGroupTeamBeam.getCreateUser()) &&
                    SCCacheUtils.getCacheUserId().equals(mSearchGroupTeamBeam.getCreateUser()) &&
                    mSearchGroupTeamBeam.gettDiggingJoinLogs().size()<4){
                mTbMain.setFavoriteImageVisible(View.VISIBLE);
            }else {
                mTbMain.setFavoriteImageVisible(View.GONE);
            }
        }
    }

    @Override
    public void setCheckUserIsSuperResponse(String response) {
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            String data = JSONObject.parseObject(response).getString("data");
            isSuper = data;
            //极光消息监听注册
            JMessageClient.registerEventReceiver(MessageListActivity.this, 1000);
//            handler.sendEmptyMessage(1);
            isSuperUser();

        }
    }

    //是否超级用户
    private void isSuperUser(){
        //加入聊天室按钮
        if (isIn == true || isSuper.equals("true")) {
            mChatView.isShowBtnInputJoin(false);
            mChatView.getChatInputView().setShowBottomMenu(true);
            mArcMenu.setVisibility(View.GONE);
            xhsEmoticonsKeyBoard.getInputJoin().setVisibility(View.GONE);
            xhsEmoticonsKeyBoard.getXhsEmoticon().setVisibility(View.VISIBLE);
            mChatView.setOnBtnInputClickListener(new ChatView.OnBtnInputClickListener() {
                @Override
                public void OnBtnInputClick(View view) {
                    if (isIn) {
                        ToastUtils.showToast(MessageListActivity.this, R.string.not_int_room);
                    }
                    xhsEmoticonsKeyBoard.getXhsEmoticon().setVisibility(View.VISIBLE);
                }
            });
        } else {
            ToastUtils.showToast(MessageListActivity.this, R.string.not_int_room);
        }
    }

    @Override
    public void onFavoriteClick(View v) {
        shareBottomDialog.show();
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



    //从本地数据库读取聊天消息
    private void loadMessageData(String roomID) {
        mEntryDao = MyApplication.getDaoSession().getMessageEntryDao();
        mMessageEntryList = mEntryDao._queryConversationEntry_MMessageEntryList(roomID);
        messageList.clear();
        if (mMessageEntryList.size() != 0) {
            for (int i = 0; i < mMessageEntryList.size(); i++) {
                LogUtils.d("----->>>>>",mMessageEntryList.get(i).toString());
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
                        /*if(TextUtils.isEmpty(messageEntry.getFileFormat())){
                            commonMessage.setType(IMessage.MessageType.RECEIVE_FILE.ordinal());
                            long fileId4 = messageEntry.getUserId();
                            long currentFileId4 = JMessageClient.getMyInfo().getUserID();
                            if (currentFileId4 == fileId4) {
                                commonMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
                            }
                            if (!TextUtils.isEmpty(messageEntry.getMediaFilePath())) {
                                commonMessage.setMediaFilePath(messageEntry.getMediaFilePath());
                                commonMessage.setDuration(messageEntry.getDuration());
                            }
                            messageList.add(commonMessage);
                            mAdapter.addToStart(commonMessage, true);
                        }else {

                        }*/
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
                }
            default:
                commonMessage.setType(IMessage.MessageType.RECEIVE_FILE.ordinal());
                long fileId4 = messageEntry.getUserId();
                long currentFileId4 = JMessageClient.getMyInfo().getUserID();
                if (currentFileId4 == fileId4) {
                    commonMessage.setType(IMessage.MessageType.SEND_VIDEO.ordinal());
                }
                if (!TextUtils.isEmpty(messageEntry.getMediaFilePath())) {
                    commonMessage.setMediaFilePath(messageEntry.getMediaFilePath());
                    commonMessage.setDuration(messageEntry.getDuration());
                }
                break;
        }
        messageList.add(commonMessage);
        mAdapter.addToStart(commonMessage, true);
    }

    //保存文件消息
    private void localSaveFileMessage(Message message, final MessageEntry messageEntry, final MessageEntryDao entryDao) {
        entryDao.insertOrReplace(messageEntry);
        FileContent fileContent = (FileContent) message.getContent();
        if(TextUtils.isEmpty(fileContent.getFormat())){
            fileContent.downloadFile(message, new DownloadCompletionCallback() {
                @Override
                public void onComplete(int i, String s, File file) {
                    messageEntry.setMediaFilePath(file.getAbsolutePath());
                    LogUtils.d("----->>>message video file path: "+file.getAbsolutePath());
                    //获取文件的时长
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(file.getAbsolutePath());
                    String duration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    LogUtils.d("---->>>message file duration: "+duration);
                    if(!TextUtils.isEmpty(duration)){
                        messageEntry.setDuration(Long.parseLong(duration)/1000);
                    }
                }
            });
        }else {
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
        entryDao.update(messageEntry);

    }

    //本地存储聊天室消息
    private void localSaveMessage(List<Message> mMsgs) {
        mMessageEntryDao = MyApplication.getDaoSession().getMessageEntryDao();
        for (int i = 0; i < mMsgs.size(); i++) {
            Message chatMessage = mMsgs.get(i);
            LogUtils.d("------>>message info:"+chatMessage.toJson());
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
        //打印消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mMsgs.size(); i++) {
                    Message msg = mMsgs.get(i);
                    LogUtils.d("ChatRoomMessageEvent message log", "第" + i + "个" + msg.toJson());
                }
                //本地存储聊天室消息
                localSaveMessage(mMsgs);
            }
        }).start();
        if (mMsgs.size() > 1) {
            mEventMessageList.clear();
        }
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
//                            fileMessage.setMediaFilePath(VideoMediaID + ".mp4");
                            fileContent.downloadFile(msg, new DownloadCompletionCallback() {
                                @Override
                                public void onComplete(int i, String s, File file) {
                                    fileMessage.setMediaFilePath(file.getAbsolutePath());
                                    LogUtils.d("---->>>even message file path: "+"i="+i+":s="+s+"----"+file.getAbsolutePath());
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
    @Subscribe(threadMode = ThreadMode.MAIN)
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
                        msg.getServerMessageId();
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
                    enterChatRoom();
                }
            }
        });
        //点击隐藏聊天输入框表情栏
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(final CharSequence input) {
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

        //发送语音
        xhsEmoticonsKeyBoard.getBtnVoice().initConv(chatRoomConversation, mAdapter, mChatView);
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
        /*if(mSocketListener!=null){
            WebSocketHandler.getDefault().removeListener(mSocketListener);
        }*/
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
