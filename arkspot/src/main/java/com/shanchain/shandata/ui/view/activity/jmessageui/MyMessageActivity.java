package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MessageListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.MessageHomeInfo;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.jiguang.imui.model.DefaultUser;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public class MyMessageActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {
    @Bind({R.id.tb_main})
    ArthurToolBar toolBar;
    @Bind(R.id.rv_message_list)
    RecyclerView rvMessageList;

    private List<MessageHomeInfo> chatRoomlist = new ArrayList<>();

    List<Conversation> conversationList;
    private Conversation conversation;
    private MessageListAdapter messageListAdapter;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initView();
        initRecyclerView();

    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyMessageActivity.this, LinearLayoutManager.VERTICAL, false);
        rvMessageList.setLayoutManager(linearLayoutManager);
        rvMessageList.setAdapter(new BaseQuickAdapter<MessageHomeInfo, BaseViewHolder>(R.layout.item_members_chat_room, chatRoomlist) {
            @Override
            protected void convert(BaseViewHolder helper, MessageHomeInfo item) {
                helper.setText(R.id.tv_item_contact_child_name, item.getJMConversation().getTargetId());
                helper.setText(R.id.tv_item_contact_child_focus, "对话");
                helper.setText(R.id.tv_item_contact_child_des, "" + item.getJMConversation().getLatestText());
            }

        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        rvMessageList.setLayoutManager(layoutManager);
        if (chatRoomlist.size()>1){
            rvMessageList.addItemDecoration(new RecyclerViewDivider(mActivity));
        }
        messageListAdapter = new MessageListAdapter(R.layout.item_msg_home, chatRoomlist);
        rvMessageList.setAdapter(messageListAdapter);
        messageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(mActivity, SingleChatActivity.class);
                MessageHomeInfo messageHomeInfo = chatRoomlist.get(position);
                messageHomeInfo.setUnRead(0);
                String toChatName = messageHomeInfo.getJMConversation().getTargetId();
                DefaultUser defaultUser = new DefaultUser(0, toChatName, messageHomeInfo.getImg());
                defaultUser.setHxUserId(messageHomeInfo.getHxUser());
                Bundle bundle = new Bundle();
                bundle.putParcelable("userInfo", defaultUser);
                readyGo(SingleChatActivity.class, bundle);
            }
        });

        messageListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                //showDialog(position);
                return true;
            }
        });

    }

    public void onEvent(Message message) {
        //有新消息到来
        LogUtils.d("会话fragment接收到新消息");
        if (messageListAdapter != null) {
            LogUtils.d("适配器不为null");

            messageListAdapter.notifyDataSetChanged();
        } else {
            LogUtils.d("适配器为null");
        }
    }

    private void initData() {
        String hxusername = SCCacheUtils.getCacheHxUserName();
        conversationList = JMessageClient.getConversationList();
        for (int i = 0; i < conversationList.size(); i++) {
            conversation = conversationList.get(i);
            if (conversation.getType() == ConversationType.single) {
                final MessageHomeInfo messageHomeInfo = new MessageHomeInfo();
                messageHomeInfo.setJMConversation(conversation);
               UserInfo userInfo = (UserInfo) conversation.getTargetInfo();
                String targetId = conversation.getTargetId();
                String avatar = conversation.getAvatarFile() != null ? conversation.getAvatarFile().getAbsolutePath() :"";
                DefaultUser defaultUser = new DefaultUser(0, userInfo.getNickname(), avatar);
                defaultUser.setHxUserId(targetId);
                messageHomeInfo.setName(userInfo.getNickname());
                messageHomeInfo.setHxUser(conversation.getTargetId());
                messageHomeInfo.setUnRead(conversation.getUnReadMsgCnt());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                conversation.getLastMsgDate();
                messageHomeInfo.setTime(conversation.getLastMsgDate() + "");
                messageHomeInfo.isTop();
                messageHomeInfo.setImg(conversation.getAvatarFile() == null ? "" : conversation.getAvatarFile().getAbsolutePath());
                chatRoomlist.add(messageHomeInfo);

                /*String avatar = userInfo.getAvatarFile()!=null?userInfo.getAvatarFile().getAbsolutePath():userInfo.getAvatar();
                DefaultUser defaultUser = new DefaultUser(userInfo.getUserID(), userInfo.getNickname(),avatar);
                defaultUser.setHxUserId(String.valueOf(userInfo.getUserID()));
                messageHomeInfo.setName(userInfo.getNickname());
                messageHomeInfo.setHxUser(conversation.getTargetId());
                messageHomeInfo.setUnRead(conversation.getUnReadMsgCnt());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                conversation.getLastMsgDate();
                messageHomeInfo.setTime(conversation.getLastMsgDate() + "");
                messageHomeInfo.isTop();
                messageHomeInfo.setImg(conversation.getAvatarFile() == null ? "" : conversation.getAvatarFile().getAbsolutePath());
                chatRoomlist.add(messageHomeInfo);*/
            }
        }
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
}
