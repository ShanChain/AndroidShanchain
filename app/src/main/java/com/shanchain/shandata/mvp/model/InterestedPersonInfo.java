package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/6/6.
 */

public class InterestedPersonInfo {
    private String avatarUrl;
    private String nickName;
    private String signature;
    /** 描述：感兴趣的原因*/
    private String reason;


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
