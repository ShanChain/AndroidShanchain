package com.shanchain.shandata.ui.model;


import android.graphics.Bitmap;

import java.io.Serializable;

import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by zhoujian on 2017/9/7.
 */

public class MessageHomeInfo implements Serializable{
    private String img;
    private String name;
    private String type;
    private String time;
    private String hxUser;
    private String lastMsg;
    private int unRead;
    private boolean isTop;
    private String jmName;
    private Bitmap avatarBitmap;
    private UserInfo userInfo;

    private Conversation mJMConversation;


    public Conversation getJMConversation() {
        return mJMConversation;
    }

    public void setJMConversation(Conversation JConversation) {
        mJMConversation = JConversation;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }

    public String getHxUser() {
        return hxUser;
    }

    public void setHxUser(String hxUser) {
        this.hxUser = hxUser;
    }

    public String getJmName() {
        return jmName;
    }

    public void setJmName(String jmName) {
        this.jmName = jmName;
    }

    public Bitmap getAvatarBitmap() {
        return avatarBitmap;
    }

    public void setAvatarBitmap(Bitmap avatarBitmap) {
        this.avatarBitmap = avatarBitmap;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
