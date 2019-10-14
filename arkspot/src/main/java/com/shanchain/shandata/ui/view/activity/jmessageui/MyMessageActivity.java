package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MessageListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.push.ExampleUtil;
import com.shanchain.shandata.receiver.MyReceiver;
import com.shanchain.shandata.ui.model.MessageHomeInfo;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.model.MyMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyMessageActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {
    @Bind({R.id.tb_main})
    ArthurToolBar toolBar;
    @Bind(R.id.rv_message_list)
    RecyclerView rvMessageList;
    @Bind(R.id.ll_notdata)
    LinearLayout llNotdata;

//    private List<MessageHomeInfo> chatRoomlist = new ArrayList<>();

//    List<Conversation> conversationList = new ArrayList<>();
    private Conversation conversation;
    private MessageListAdapter messageListAdapter;
    public static boolean isForeground = false;
    private MyMessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.shanchain.shandata.MY_MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private JSONObject mMessageJson;
    private String mJguserName;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initViewsAndEvents() {
        JMessageClient.registerEventReceiver(this);
        if (getIntent().getExtras() != null && getIntent().getExtras().getString(JPushInterface.EXTRA_EXTRA) != null) {
            try {
                Bundle bundle = getIntent().getExtras();
                mMessageJson = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                LogUtils.d("MyMessageReceiver", "bundle.getString(KEY_MESSAGE):" + mMessageJson.toString());
                mJguserName = SCJsonUtils.parseString(mMessageJson.toString(), "jguserName");
                String extra = SCJsonUtils.parseString(mMessageJson.toString(), "extra");
                String sysPage = SCJsonUtils.parseString(mMessageJson.toString(), "sysPage");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        initToolBar();
//        initView();
//        initRecyclerView();
//        initData();
        //注册自定义消息广播
//        registerMessageReceiver();
    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMessageList.setLayoutManager(layoutManager);
        final List<MessageHomeInfo> chatRoomlist = new ArrayList<>();
        List<Conversation> conversationList = JMessageClient.getConversationList();
        if(conversationList!=null && conversationList.size()>0){
            llNotdata.setVisibility(View.GONE);
            rvMessageList.setVisibility(View.VISIBLE);
            for (int i = 0; i < conversationList.size(); i++) {
                conversation = conversationList.get(i);
                if (conversation.getType() == ConversationType.single) {
                    LogUtils.d("----->>>>"+conversation.toJsonString());
                    final MessageHomeInfo messageHomeInfo = new MessageHomeInfo();
                    messageHomeInfo.setJMConversation(conversation);
                    UserInfo userInfo = (UserInfo) conversation.getTargetInfo();
                    messageHomeInfo.setName(userInfo.getNickname());
                    messageHomeInfo.setHxUser(conversation.getTargetId());
                    messageHomeInfo.setUnRead(conversation.getUnReadMsgCnt());
                    messageHomeInfo.setJmName(userInfo.getUserName());
                    conversation.getLastMsgDate();
                    messageHomeInfo.setTime(conversation.getLastMsgDate() + "");
                    messageHomeInfo.isTop();
                    messageHomeInfo.setMessageType(conversation.getLatestType().ordinal());
                    messageHomeInfo.setUserInfo(userInfo);
                    chatRoomlist.add(messageHomeInfo);
                }
            }
        }else {
            llNotdata.setVisibility(View.VISIBLE);
            rvMessageList.setVisibility(View.GONE);
        }

        if (chatRoomlist.size() > 1) {
            rvMessageList.addItemDecoration(new RecyclerViewDivider(mActivity));
        }
        messageListAdapter = new MessageListAdapter(R.layout.item_msg_home, chatRoomlist);
        rvMessageList.setAdapter(messageListAdapter);
        messageListAdapter.notifyDataSetChanged();
        messageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageHomeInfo messageHomeInfo = chatRoomlist.get(position);
                messageHomeInfo.setUnRead(0);
                Conversation conversation = messageHomeInfo.getJMConversation();
                conversation.setUnReadMessageCnt(0);
                String toChatName = messageHomeInfo.getJMConversation().getTargetId();
                DefaultUser defaultUser = new DefaultUser(0, toChatName, messageHomeInfo.getImg());
                defaultUser.setHxUserId(messageHomeInfo.getHxUser());
                Bundle bundle = new Bundle();
                bundle.putParcelable("userInfo", defaultUser);
                if (messageHomeInfo.getHxUser().equals("123456") || messageHomeInfo.getHxUser().equals(mJguserName)) {
                    messageHomeInfo.setTop(true);
                }
                readyGo(SingleChatActivity.class, bundle);
            }
        });
    }

    public void onEvent(MessageEvent event) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (messageListAdapter != null) {
                    initRecyclerView();
                    messageListAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    Bitmap mBitmap;
    private Bitmap getUserAvatarBitmap(UserInfo userInfo){
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                LogUtils.d("-------->>>i:"+i+"----s: "+s+"----->>"+bitmap);
                if (bitmap != null) {
                    mBitmap =  bitmap;
                } else {
                    mBitmap = null;
                }
            }
        });
        return mBitmap;
    }

    private void initToolBar() {
        toolBar.setTitleTextColor(Color.BLACK);
        toolBar.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        toolBar.getTitleView().setLayoutParams(layoutParams);
        toolBar.setTitleText(getResources().getString(R.string.nav_my_message));
        toolBar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        toolBar.setLeftImage(R.mipmap.abs_roleselection_btn_back_default);
        toolBar.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        isForeground = true;
//        initData();
        initRecyclerView();
        super.onResume();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MyMessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MyMessageReceiver extends BroadcastReceiver {
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
                    ToastUtils.showToast(MyMessageActivity.this, showMsg.toString());
                    LogUtils.d("MyMessageReceiver", "MyMessageReceiver:" + showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }
}
