package com.shanchain.arkspot.ui.presenter;

import com.hyphenate.chat.EMMessage;

/**
 * Created by zhoujian on 2017/9/12.
 */

public interface ChatPresenter {
    void initChat(String toChatName);

    void updateData(String toChatName);

    void sendMsg(String msg, String toChatName, int msgAttr, EMMessage.ChatType chatType,String headImg,String nickName,boolean isGroup,String groupImg);

    void pullHistoryMsg(String toChatName);

}
