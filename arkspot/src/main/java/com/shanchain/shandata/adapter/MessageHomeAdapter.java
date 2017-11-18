package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.shanchain.shandata.R;
import com.shanchain.data.common.base.Constants;
import com.shanchain.shandata.ui.model.MessageHomeInfo;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/7.
 */

public class MessageHomeAdapter extends BaseQuickAdapter<MessageHomeInfo, BaseViewHolder> {

    //默认群头像
    private String defaultGroupImg = "http://p4.so.qhimgs1.com/bdr/200_200_/t01b1983d340e91c754.png";
    //默认个人头像
    private String defaultHeadImg = "http://www.qqbody.com/uploads/allimg/201306/29-173154_455.jpg";

    public MessageHomeAdapter(@LayoutRes int layoutResId, @Nullable List<MessageHomeInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageHomeInfo item) {
        EMConversation emConversation = item.getEMConversation();
        int unreadMsgCount = emConversation.getUnreadMsgCount();
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
        helper.setText(R.id.tv_item_msg_home_time, DateUtils.getTimestampString(new Date(emConversation.getLastMessage().getMsgTime())));
        //设置最近一条消息
        helper.setText(R.id.tv_item_msg_home_last, ((EMTextMessageBody) emConversation.getLastMessage().getBody()).getMessage());
        //设置昵称
        EMConversation.EMConversationType type = emConversation.getType();
        if (emConversation.isGroup()) {
            //会话是群组
            String s = emConversation.getLastMessage().conversationId();
            helper.setText(R.id.tv_item_msg_home_name, s);
            String groupImg = emConversation.getLastMessage().getStringAttribute(Constants.MSG_GROUP_IMG, defaultGroupImg);
            GlideUtils.load(mContext,groupImg,(ImageView) helper.getView(R.id.iv_item_msg_home_avatar));
        } else {
            //会话不是群组
            helper.setText(R.id.tv_item_msg_home_name, emConversation.getLastMessage().getTo());
            String headImg = emConversation.getLastMessage().getStringAttribute(Constants.MSG_HEAD_IMG, defaultHeadImg);
            GlideUtils.load(mContext, headImg, (ImageView) helper.getView(R.id.iv_item_msg_home_avatar));
        }

    }
}
