package com.shanchain.shandata.ui.model;

import java.util.Date;

public class CouponInfo {
    private String createTime;
    private String deadline;
    private String name;
    private String price;
    private String remainAmount;
    private String userStatus;
    private String couponsId;
    private int getStatus;

    public static final int COUPONS_CREATE = 20; //创建
    public static final int COUPONS_RECEIVER = 21;//已领取
    public static final int COUPONS_UN_RECEIVER = 22;//未领取

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getGetStatus() {
        return getStatus;
    }

    public void setGetStatus(int getStatus) {
        this.getStatus = getStatus;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(String remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(String couponsId) {
        this.couponsId = couponsId;
    }
}
