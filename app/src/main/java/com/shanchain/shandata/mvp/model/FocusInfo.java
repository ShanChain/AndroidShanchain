package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/7/3.
 */

public class FocusInfo {
    private String avatarUrl;
    private String name;
    private boolean isFocused;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }
}
