package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/6/29.
 */

public class ShieldPersonInfo {
    private String avatarUrl;
    private String name;
    private boolean isShielded;

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

    public boolean isShielded() {
        return isShielded;
    }

    public void setShielded(boolean shielded) {
        isShielded = shielded;
    }
}
