package com.shanchain.shandata.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModifyUserInfo {
    private String name;
    private String signature;
    private String headImg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
