package com.shanchain.shandata.adapter;//package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.MessageHomeInfo;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

//**
// * Created by zhoujian on 2017/9/7.
// */

public class MessageListAdapter extends BaseQuickAdapter<MessageHomeInfo, BaseViewHolder> {

    private Conversation jmConversation;

    public MessageListAdapter(@LayoutRes int layoutResId, @Nullable List<MessageHomeInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageHomeInfo item) {
        jmConversation = item.getJMConversation();
        int unreadMsgCount = item.getUnRead();
        if (unreadMsgCount <= 0) {
            helper.setVisible(R.id.tv_item_msg_home_unread, false);
        } else if (unreadMsgCount <= 99 && unreadMsgCount > 0) {
            helper.setVisible(R.id.tv_item_msg_home_unread, true);
            helper.setText(R.id.tv_item_msg_home_unread, unreadMsgCount + "");
        } else {
            helper.setVisible(R.id.tv_item_msg_home_unread, true);
            helper.setText(R.id.tv_item_msg_home_unread, "99+");
        }

        if (item.isTop()) {
            helper.setBackgroundColor(R.id.rl_item_msg_home, mContext.getResources().getColor(R.color.colorDivider));
        } else {
            helper.setBackgroundColor(R.id.rl_item_msg_home, mContext.getResources().getColor(R.color.colorWhite));
        }
        //设置时间
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        helper.setText(R.id.tv_item_msg_home_time,DateUtils.formatFriendly(new Date(jmConversation.getLastMsgDate())));
        //获取JmMessage对象
        Message JmMessage =  jmConversation.getLatestMessage();
        //设置最近一条消息
//        TextContent messageContent = (TextContent) JmMessage.getContent();
        helper.setText(R.id.tv_item_msg_home_last,item.getJMConversation().getLatestText());
        //设置昵称
        helper.setText(R.id.tv_item_msg_home_name,item.getName() );
//        GlideUtils.load(mContext, item.getImg(), (ImageView) helper.getView(R.id.iv_item_msg_home_avatar),0);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.aurora_headicon_default);
        Glide.with(mContext)
                .load(item.getImg())
                .apply(options)
                .into((ImageView) helper.getView(R.id.iv_item_msg_home_avatar));
        /*if (jmConversation.isGroup()) {
            //会话是群组

            String groupImg = item.getImg();
            GlideUtils.load(mContext,groupImg,(ImageView) helper.getView(R.id.iv_item_msg_home_avatar),0);
        } else {
            //会话不是群组
            helper.setText(R.id.tv_item_msg_home_name, item.getName());
            String headImg = item.getImg();
            GlideUtils.load(mContext, headImg, (ImageView) helper.getView(R.id.iv_item_msg_home_avatar),0);
        }*/

    }
}
