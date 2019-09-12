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
    String accountLevel;
    String brokerageNotFrozenCoin;//返佣解冻金币
    String brokerageFrozenCoin;//返佣冻结金币

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

    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getBrokerageNotFrozenCoin() {
        return brokerageNotFrozenCoin;
    }

    public void setBrokerageNotFrozenCoin(String brokerageNotFrozenCoin) {
        this.brokerageNotFrozenCoin = brokerageNotFrozenCoin;
    }

    public String getBrokerageFrozenCoin() {
        return brokerageFrozenCoin;
    }

    public void setBrokerageFrozenCoin(String brokerageFrozenCoin) {
        this.brokerageFrozenCoin = brokerageFrozenCoin;
    }
}
