package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.shandata.ui.model.GroupMembersInfo;
import com.shanchain.shandata.ui.model.MsgInfo;
import com.shanchain.shandata.ui.model.ResponseGroupMemberBean;
import com.shanchain.shandata.ui.model.ResponseSceneDetailInfo;
import com.shanchain.shandata.ui.presenter.ChatPresenter;
import com.shanchain.shandata.ui.view.activity.chat.view.ChatView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhoujian on 2017/9/12.
 */

public class ChatPresenterImpl implements ChatPresenter {
    private ChatView mChatView;
    private List<EMMessage> mEMMessageList = new ArrayList<>();
    private List<MsgInfo> mMsgInfoList = new ArrayList<>();
    private List<EMMessage> copyMessageList = new ArrayList();
    private List<String> members = new ArrayList<>();
    private String characherId = SCCacheUtils.getCacheCharacterId();
    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
    }

    /** 获取群信息完成，主要获取群主*/
    private boolean groupFinish;
    /** 获取群成员信息完成*/
    private boolean memberFinish;

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
     * @param headImg 发送消息时附带头像信息
     * @param nickName 发送消息时附带昵称信息
     * @param atMembers 艾特的人数组
     */
    @Override
    public void sendMsg(String msg, String toChatName, int msgAttr, EMMessage.ChatType chatType,String headImg,String nickName,boolean isGroup,String groupImg,List<String> atMembers) {
        EMMessage txtSendMessage = EMMessage.createTxtSendMessage(msg, toChatName);
        //设置扩展消息类型
        String atArr = JSONObject.toJSONString(atMembers);
        txtSendMessage.setAttribute(Constants.MSG_ATTR,msgAttr);
        txtSendMessage.setAttribute(Constants.MSG_HEAD_IMG,headImg);
        txtSendMessage.setAttribute(Constants.MSG_NICK_NAME,nickName);
        txtSendMessage.setAttribute(Constants.MSG_IS_GROUP,isGroup);
        txtSendMessage.setAttribute(Constants.MSG_GROUP_IMG,groupImg);
        txtSendMessage.setAttribute(Constants.MSG_AT_LIST,atArr);
        txtSendMessage.setAttribute(Constants.MSG_CHARACTER_ID,characherId);
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
        String from = txtSendMessage.getFrom();
        String userName = txtSendMessage.getUserName();
        String to = txtSendMessage.getTo();
        LogUtils.i("发送消息  username = " + userName + "; from = " + from + "; to = " + to);
    }

    /**
     *  描述：拉取聊天记录
     */
    @Override
    public void pullHistoryMsg(String toChatName) {
        if (mMsgInfoList.size() == 0){
            mChatView.onPullHistory(null);
            return;
        }
        String msgId = mMsgInfoList.get(0).getEMMessage().getMsgId();
        copyMessageList.clear();
        copyMessageList.addAll(mEMMessageList);
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatName);
        if (conversation != null) {
            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(msgId, 20);
            //Collections.reverse(emMessages);
            mEMMessageList.clear();
            mEMMessageList.addAll(emMessages);
            mEMMessageList.addAll(copyMessageList);
            mMsgInfoList.clear();
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

            mChatView.onPullHistory(emMessages);

        }
    }

    @Override
    public void initGroup(String toChatName) {
        //自己服务器极客接口获取群信息
        SCHttpUtils.post()
                .url(HttpApi.HX_GROUP_QUERY)
                .addParams("groupId", toChatName)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取群信息失败");
                        e.printStackTrace();
                        mChatView.initGroupSuccess(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到群信息 = " + response);
                            ResponseSceneDetailInfo sceneDetailInfo = JSONObject.parseObject(response, ResponseSceneDetailInfo.class);
                            String code = sceneDetailInfo.getCode();
                            if (TextUtils.equals(code,NetErrCode.COMMON_SUC_CODE)){
                                String groupName = sceneDetailInfo.getData().getGroupName();
                                String hxUserName = sceneDetailInfo.getData().getGroupOwner().getHxUserName();
                                members.add(hxUserName);
                                members.add(0,groupName);
                                groupFinish = true;
                                if (memberFinish){
                                    mChatView.initGroupSuccess(members);
                                }

                            }else {
                                mChatView.initGroupSuccess(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mChatView.initGroupSuccess(null);
                        }
                    }
                });

        SCHttpUtils.post()
                .url(HttpApi.HX_GROUP_MEMBERS)
                .addParams("groupId",toChatName)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取群成员失败");
                        e.printStackTrace();
                        mChatView.initGroupSuccess(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取群成员 = " + response);

                            GroupMembersInfo groupMembersInfo = JSONObject.parseObject(response, GroupMembersInfo.class);
                            String code = groupMembersInfo.getCode();
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
                                List<ResponseGroupMemberBean> memberBeanList = groupMembersInfo.getData();
                                for (int i = 0; i < memberBeanList.size(); i ++) {
                                    members.add(memberBeanList.get(i).getHxUserName());
                                }
                                memberFinish = true;
                                if (groupFinish){
                                    mChatView.initGroupSuccess(members);
                                }
                            }else {
                                mChatView.initGroupSuccess(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mChatView.initGroupSuccess(null);
                        }

                    }
                });

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
