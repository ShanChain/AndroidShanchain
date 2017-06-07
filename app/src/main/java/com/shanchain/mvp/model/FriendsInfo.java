package com.shanchain.mvp.model;

/**
 * Created by zhoujian on 2017/6/6.
 */

public class FriendsInfo {
    private String name;
    private String nickName;
    private String avatarUrl;
    private boolean isFocus;
    private boolean isShanChainer;

    public boolean isShanChainer() {
        return isShanChainer;
    }

    public void setShanChainer(boolean shanChainer) {
        isShanChainer = shanChainer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }
}
