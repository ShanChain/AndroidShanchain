package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/8/10
 * Describe :
 */
public class ShareBean implements Serializable {
    String inviteUserId;
    String diggingsId;
    String inviteCode;
    String roomImage;

    public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getDiggingsId() {
        return diggingsId;
    }

    public void setDiggingsId(String diggingsId) {
        this.diggingsId = diggingsId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    @Override
    public String toString() {
        return "ShareBean{" +
                "inviteUserId='" + inviteUserId + '\'' +
                ", diggingsId='" + diggingsId + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", roomImage='" + roomImage + '\'' +
                '}';
    }
}
