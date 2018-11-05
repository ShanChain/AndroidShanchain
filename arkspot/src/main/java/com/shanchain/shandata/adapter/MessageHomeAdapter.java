//package com.shanchain.shandata.adapter;
//
//import android.support.annotation.LayoutRes;
//import android.support.annotation.Nullable;
//import android.widget.ImageView;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.shanchain.data.common.base.Constants;
//import com.shanchain.data.common.cache.SCCacheUtils;
//import com.shanchain.data.common.utils.GlideUtils;
//import com.shanchain.shandata.R;
//import com.shanchain.shandata.ui.model.MessageHomeInfo;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by zhoujian on 2017/9/7.
// */
//
//public class MessageHomeAdapter extends BaseQuickAdapter<MessageHomeInfo, BaseViewHolder> {
//
//    public MessageHomeAdapter(@LayoutRes int layoutResId, @Nullable List<MessageHomeInfo> data) {
//        super(layoutResId, data);
//    }
//
////    @Override
////    protected void convert(BaseViewHolder helper, MessageHomeInfo item) {
////        EMConversation emConversation = item.getEMConversation();
////        int unreadMsgCount = emConversation.getUnreadMsgCount();
////        if (unreadMsgCount <= 0) {
////            helper.setVisible(R.id.tv_item_msg_home_unread, false);
////        } else if (unreadMsgCount <= 99 && unreadMsgCount > 0) {
////            helper.setVisible(R.id.tv_item_msg_home_unread, true);
////            helper.setText(R.id.tv_item_msg_home_unread, unreadMsgCount + "");
////        } else {
////            helper.setVisible(R.id.tv_item_msg_home_unread, true);
////            helper.setText(R.id.tv_item_msg_home_unread, "99+");
////        }
////
////        if (item.isTop()) {
////            helper.setBackgroundColor(R.id.rl_item_msg_home, mContext.getResources().getColor(R.color.colorDivider));
////        } else {
////            helper.setBackgroundColor(R.id.rl_item_msg_home, mContext.getResources().getColor(R.color.colorWhite));
////        }
////        //设置时间
////        helper.setText(R.id.tv_item_msg_home_time, DateUtils.getTimestampString(new Date(emConversation.getLastMessage().getMsgTime())));
////        //获取EmMessage对象
////        EMMessage emMessage =  emConversation.getLastMessage();
////        //设置最近一条消息
////        helper.setText(R.id.tv_item_msg_home_last, emMessage.getStringAttribute(Constants.MSG_NICK_NAME,emMessage.getFrom())+":"+((EMTextMessageBody) emConversation.getLastMessage().getBody()).getMessage());
////        //设置昵称
////        EMConversation.EMConversationType type = emConversation.getType();
////        helper.setText(R.id.tv_item_msg_home_name, item.getName());
////        GlideUtils.load(mContext, item.getImg(), (ImageView) helper.getView(R.id.iv_item_msg_home_avatar),0);
////        /*if (emConversation.isGroup()) {
////            //会话是群组
////
////            String groupImg = item.getImg();
////            GlideUtils.load(mContext,groupImg,(ImageView) helper.getView(R.id.iv_item_msg_home_avatar),0);
////        } else {
////            //会话不是群组
////            helper.setText(R.id.tv_item_msg_home_name, item.getName());
////            String headImg = item.getImg();
////            GlideUtils.load(mContext, headImg, (ImageView) helper.getView(R.id.iv_item_msg_home_avatar),0);
////        }*/
////
////    }
//}
