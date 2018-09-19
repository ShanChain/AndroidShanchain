package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/12/6.
 */

public class ConversationInfo {
    private String hxUser;
    private String headImg;
    private String name;
    private boolean isGroup;

    public String getHxUser() {
        return hxUser;
    }

    public void setHxUser(String hxUser) {
        this.hxUser = hxUser;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
