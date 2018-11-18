package com.shanchain.shandata.ui.model;

import cn.jiguang.imui.model.ChatEventMessage;

/**
 * Created by zhoujian on 2017/10/30.
 */

public class BdCommentBean {
    private int characterId;
    private CommentBean mCommentBean;
    private ContactBean mContactBean;
    private ChatEventMessage chatEventMessage;

    public ChatEventMessage getChatEventMessage() {
        return chatEventMessage;
    }

    public void setChatEventMessage(ChatEventMessage chatEventMessage) {
        this.chatEventMessage = chatEventMessage;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public CommentBean getCommentBean() {
        return mCommentBean;
    }

    public void setCommentBean(CommentBean commentBean) {
        mCommentBean = commentBean;
    }

    public ContactBean getContactBean() {
        return mContactBean;
    }

    public void setContactBean(ContactBean contactBean) {
        mContactBean = contactBean;
    }
}
