package com.shanchain.shandata.adapter;//package com.shanchain.shandata.adapter;

import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.MessageHomeInfo;
import com.shanchain.shandata.ui.view.activity.jmessageui.MessageListActivity;
import com.shanchain.shandata.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

//**
// * Created by zhoujian on 2017/9/7.
// */

public class MessageListAdapter extends BaseQuickAdapter<MessageHomeInfo, BaseViewHolder> {

    private Conversation jmConversation;

    public MessageListAdapter(@LayoutRes int layoutResId, @Nullable List<MessageHomeInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MessageHomeInfo item) {
        jmConversation = item.getJMConversation();
        final int unreadMsgCount = item.getUnRead();
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
        final CircleImageView circleImageView = helper.getView(R.id.iv_item_msg_home_avatar);
        JMessageClient.getUserInfo(item.getJmName(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if(userInfo!=null && !TextUtils.isEmpty(userInfo.getAvatar())){
                    userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            if (bitmap != null) {
                                circleImageView.setImageBitmap(bitmap);
                            } else {
                                circleImageView.setBackground(mContext.getResources().getDrawable(R.mipmap.aurora_headicon_default));
                            }
                        }
                    });
                }else {
                    circleImageView.setBackground(mContext.getResources().getDrawable(R.mipmap.aurora_headicon_default));
                }
            }
        });

    }
}
