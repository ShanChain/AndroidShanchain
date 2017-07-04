package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/6/30.
 */

public class MyPublicWelfareInfo {
    private String avatarUrl;
    private String pubWelfare;
    private String time;
    private String shanVouchers;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPubWelfare() {
        return pubWelfare;
    }

    public void setPubWelfare(String pubWelfare) {
        this.pubWelfare = pubWelfare;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShanVouchers() {
        return shanVouchers;
    }

    public void setShanVouchers(String shanVouchers) {
        this.shanVouchers = shanVouchers;
    }
}
