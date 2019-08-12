package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/8/12
 * Describe :
 */
public class TDiggingJoinLogs implements Serializable {
    String userId;
    String userIcon;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    @Override
    public String toString() {
        return "TDiggingJoinLogs{" +
                "userId='" + userId + '\'' +
                ", userIcon='" + userIcon + '\'' +
                '}';
    }
}
