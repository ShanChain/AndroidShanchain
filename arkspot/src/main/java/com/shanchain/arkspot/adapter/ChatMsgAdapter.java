package com.shanchain.arkspot.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.MsgInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/11.
 */

public class ChatMsgAdapter extends BaseMultiItemQuickAdapter<MsgInfo,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatMsgAdapter(List<MsgInfo> data) {
        super(data);
        addItemType(MsgInfo.MSG_TEXT_RECEIVE, R.layout.item_msg_text_receive);
        addItemType(MsgInfo.MSG_TEXT_SEND,R.layout.item_msg_text_send);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgInfo item) {
        switch (helper.getItemViewType()) {
            case MsgInfo.MSG_TEXT_SEND:
                //发送的消息
                 EMMessage msgSend = item.getEMMessage();
                long msgSendTime = msgSend.getMsgTime();
                EMTextMessageBody bodySend = (EMTextMessageBody) msgSend.getBody();
                String messageSend = bodySend.getMessage();
                helper.setText(R.id.tv_item_msg_send_content,messageSend);
                helper.setText(R.id.tv_item_msg_send_time, DateUtils.getTimestampString(new Date(msgSendTime)));

                //设置时间戳是否显示
               if (helper.getLayoutPosition() == 0){
                    helper.setVisible(R.id.tv_item_msg_send_time,true);
                }else {
                    long preMsgTime = mData.get(helper.getLayoutPosition() - 1).getEMMessage().getMsgTime();
                    if (DateUtils.isCloseEnough(msgSendTime,preMsgTime)){
                        helper.setVisible(R.id.tv_item_msg_send_time,false);
                    }else {
                        helper.setVisible(R.id.tv_item_msg_send_time,true);
                    }
                }


                break;

            case MsgInfo.MSG_TEXT_RECEIVE:
                //接收的消息
                EMMessage msgReceive = item.getEMMessage();
                long msgReceiveTime = msgReceive.getMsgTime();
                EMTextMessageBody bodyReceive = (EMTextMessageBody) msgReceive.getBody();
                String messageReceive = bodyReceive.getMessage();
                helper.setText(R.id.tv_item_msg_receive_content,messageReceive);
                helper.setText(R.id.tv_item_msg_receive_time,DateUtils.getTimestampString(new Date(msgReceiveTime)));

                //设置时间戳是否显示
                if (helper.getLayoutPosition() == 0){
                    helper.setVisible(R.id.tv_item_msg_receive_time,true);
                }else {
                    long preMsgReceiveTime = mData.get(helper.getLayoutPosition() - 1).getEMMessage().getMsgTime();
                    if (DateUtils.isCloseEnough(msgReceiveTime,preMsgReceiveTime)){
                        helper.setVisible(R.id.tv_item_msg_receive_time,false);
                    }else {
                        helper.setVisible(R.id.tv_item_msg_receive_time,true);
                    }
                }
                break;
        }
    }
}
