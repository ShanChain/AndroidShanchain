package com.shanchain.shandata.ui.model;

import com.hyphenate.chat.EMConversation;

import java.io.Serializable;

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

    private EMConversation mEMConversation;


    public EMConversation getEMConversation() {
        return mEMConversation;
    }

    public void setEMConversation(EMConversation EMConversation) {
        mEMConversation = EMConversation;
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
}
