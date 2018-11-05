package com.shanchain.shandata.ui.presenter.impl;

import com.shanchain.shandata.ui.model.MessageHomeInfo;
import com.shanchain.shandata.ui.presenter.ChatRoomPresenter;

import java.util.List;

import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.model.Conversation;

public class ChatRoomPresenterImpl implements ChatRoomPresenter {

    @Override
    public void initConversationInfo(List<MessageHomeInfo> sourceDatas,long roomID) {
        ChatRoomManager.enterChatRoom(roomID, new RequestCallback<Conversation>() {
            @Override
            public void gotResult(int i, String s, Conversation conversation) {

            }
        });
    }

    @Override
    public void initConversationCache(List<MessageHomeInfo> sourceDatas) {

    }
}