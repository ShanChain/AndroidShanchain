package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/8/26
 * Describe :
 */
public class InvationBean implements Serializable {
    String userId;
    int acceptUserCount;
    String frozenCoin;
    String brokerageCoin;
    String inviteCodeImg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAcceptUserCount() {
        return acceptUserCount;
    }

    public void setAcceptUserCount(int acceptUserCount) {
        this.acceptUserCount = acceptUserCount;
    }

    public String getFrozenCoin() {
        return frozenCoin;
    }

    public void setFrozenCoin(String frozenCoin) {
        this.frozenCoin = frozenCoin;
    }

    public String getBrokerageCoin() {
        return brokerageCoin;
    }

    public void setBrokerageCoin(String brokerageCoin) {
        this.brokerageCoin = brokerageCoin;
    }

    public String getInviteCodeImg() {
        return inviteCodeImg;
    }

    public void setInviteCodeImg(String inviteCodeImg) {
        this.inviteCodeImg = inviteCodeImg;
    }
}
