package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/9/26
 * Describe :
 */
public class PasspostBean implements Serializable {
    String id;
    String userId;
    String idcardNo;
    String passportNo;
    String realName;
    String passportPhoto;
    String passportPhotoHand;
    String cardPhotoFront;
    String cardPhotoBackground;
    String cardPhotoHand;
    int realStatus;
    String failReason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassportPhoto() {
        return passportPhoto;
    }

    public void setPassportPhoto(String passportPhoto) {
        this.passportPhoto = passportPhoto;
    }

    public String getPassportPhotoHand() {
        return passportPhotoHand;
    }

    public void setPassportPhotoHand(String passportPhotoHand) {
        this.passportPhotoHand = passportPhotoHand;
    }

    public String getCardPhotoFront() {
        return cardPhotoFront;
    }

    public void setCardPhotoFront(String cardPhotoFront) {
        this.cardPhotoFront = cardPhotoFront;
    }

    public String getCardPhotoBackground() {
        return cardPhotoBackground;
    }

    public void setCardPhotoBackground(String cardPhotoBackground) {
        this.cardPhotoBackground = cardPhotoBackground;
    }

    public String getCardPhotoHand() {
        return cardPhotoHand;
    }

    public void setCardPhotoHand(String cardPhotoHand) {
        this.cardPhotoHand = cardPhotoHand;
    }

    public int getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(int realStatus) {
        this.realStatus = realStatus;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
