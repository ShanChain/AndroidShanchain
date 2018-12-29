package com.shanchain.shandata.ui.model;

public class RedPaper {

    /**
     * imgUrl : http://shanchain-picture.oss-cn-beijing.aliyuncs.com/810120b84cab4277af75ce3cae1968b4.jpg
     * receiveTime : 2018-12-26 21:46:15
     * amount : 0.65
     * name : SBB
     * shareUserName : SBB
     */

    private String imgUrl;
    private String receiveTime;
    private double amount;
    private String name;
    private String shareUserName;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShareUserName() {
        return shareUserName;
    }

    public void setShareUserName(String shareUserName) {
        this.shareUserName = shareUserName;
    }
}
