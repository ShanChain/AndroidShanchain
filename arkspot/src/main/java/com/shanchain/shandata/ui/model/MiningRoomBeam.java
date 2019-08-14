package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/8/14
 * Describe :
 */
public class MiningRoomBeam implements Serializable {
    String userId;
    String userName;
    String diggingsId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDiggingsId() {
        return diggingsId;
    }

    public void setDiggingsId(String diggingsId) {
        this.diggingsId = diggingsId;
    }
}
