package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/8/23
 * Describe :
 */
public class InvationRecordBean implements Serializable {
    String userId;
    String acceptUserId;
    String createTime;
    String acceptUserName;
    int isActive;
    String acceptUserMobile;
    String userName;

    public String getAcceptUserName() {
        return acceptUserName;
    }

    public void setAcceptUserName(String acceptUserName) {
        this.acceptUserName = acceptUserName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getAcceptUserMobile() {
        return acceptUserMobile;
    }

    public void setAcceptUserMobile(String acceptUserMobile) {
        this.acceptUserMobile = acceptUserMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
