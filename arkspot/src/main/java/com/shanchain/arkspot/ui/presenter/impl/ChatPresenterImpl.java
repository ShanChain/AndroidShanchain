package com.shanchain.arkspot.ui.presenter.impl;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.shanchain.arkspot.ui.model.MsgInfo;
import com.shanchain.arkspot.ui.presenter.ChatPresenter;
import com.shanchain.arkspot.ui.view.activity.chat.view.ChatView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;

/**
 * Created by zhoujian on 2017/9/12.
 */

public class ChatPresenterImpl implements ChatPresenter {
    private ChatView mChatView;
    private List<EMMessage> mEMMessageList = new ArrayList<>();
    private List<MsgInfo> mMsgInfoList = new ArrayList<>();

    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
    }


    /**
     *  描述：初始化历史聊天记录
     *
     */
    @Override
    public void initChat(String toChatName) {
        updateChatData(toChatName);
        mChatView.onInit(mMsgInfoList);
    }


    /**
     *  描述：当发送消息和接收到消息时更新聊天内容
     *
     */
    @Override
    public void updateData(String toChatName) {
        updateChatData(toChatName);
        mChatView.onUpDate(mMsgInfoList);
    }

    /**
     * 描述：发送消息
     * @param msg 消息内容
     * @param toChatName 聊天对象
     * @param msgAttr 扩展消息参数
     * @param chatType 聊天类型
     *
     */
    @Override
    public void sendMsg(String msg, String toChatName, int msgAttr, EMMessage.ChatType chatType) {
        EMMessage txtSendMessage = EMMessage.createTxtSendMessage(msg, toChatName);
        //设置扩展消息类型
        txtSendMessage.setAttribute("msgAttr",msgAttr);
        txtSendMessage.setChatType(chatType);
        txtSendMessage.setStatus(EMMessage.Status.INPROGRESS);
        mEMMessageList.add(txtSendMessage);
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setItemType(MsgInfo.MSG_TEXT_SEND);
        msgInfo.setEMMessage(txtSendMessage);
        mMsgInfoList.add(msgInfo);
        mChatView.onUpDate(mMsgInfoList);
        txtSendMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                String name = Thread.currentThread().getName();
                LogUtils.d("当前线程" + name);
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        String name1 = Thread.currentThread().getName();
                        LogUtils.d("ThreadUtils = 当前线程" + name1);
                        mChatView.onUpDate(mMsgInfoList);
                    }
                });

            }

            @Override
            public void onError(int i, String s) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onUpDate(mMsgInfoList);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

        EMClient.getInstance().chatManager().sendMessage(txtSendMessage);
    }

    private void updateChatData(String toChatName) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatName);
        if (conversation != null) {
            //将所有的未读消息标记为已读
            conversation.markAllMessagesAsRead();

            //曾经有聊天过
            //那么获取最多最近的20条聊天记录，然后展示到View层
            //获取最后一条消息
            EMMessage lastMessage = conversation.getLastMessage();
            //获取最后一条消息之前的19条（最多）
            int count = 19;
            if (mEMMessageList.size() >= 19) {
                count = mEMMessageList.size();
            }

            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), count);
            Collections.reverse(emMessages);
            mEMMessageList.clear();
            mEMMessageList.add(lastMessage);
            mEMMessageList.addAll(emMessages);
            Collections.reverse(mEMMessageList);
            for (int i = 0; i < mEMMessageList.size(); i++) {
                MsgInfo msgInfo = new MsgInfo();
                if (mEMMessageList.get(i).direct() == EMMessage.Direct.SEND) {
                    msgInfo.setItemType(MsgInfo.MSG_TEXT_SEND);
                } else {
                    msgInfo.setItemType(MsgInfo.MSG_TEXT_RECEIVE);
                }
                msgInfo.setEMMessage(mEMMessageList.get(i));
                mMsgInfoList.add(msgInfo);
            }
        } else {
            mEMMessageList.clear();
            mMsgInfoList.clear();
        }

    }
}
