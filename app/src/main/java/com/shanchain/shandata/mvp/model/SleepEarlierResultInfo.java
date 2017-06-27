package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/6/22.
 */

public class SleepEarlierResultInfo {
    private String imgUrl;
    private String name;
    private String confidence;
    private String shanyuan;
    private String shanquan;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getShanyuan() {
        return shanyuan;
    }

    public void setShanyuan(String shanyuan) {
        this.shanyuan = shanyuan;
    }

    public String getShanquan() {
        return shanquan;
    }

    public void setShanquan(String shanquan) {
        this.shanquan = shanquan;
    }
}
