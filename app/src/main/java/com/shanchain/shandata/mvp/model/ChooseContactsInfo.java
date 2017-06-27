package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/6/7.
 */

public class ChooseContactsInfo {
   private String avatarUrl;
   private String name;
   private boolean isChecked;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
