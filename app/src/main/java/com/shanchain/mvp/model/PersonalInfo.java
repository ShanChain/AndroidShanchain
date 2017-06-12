package com.shanchain.mvp.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/8.
 */

public class PersonalInfo {
    private String avatar;
    private String name;
    private int focusCounts;
    private int fansCounts;
    private List<StoryInfo> storyInfos;
    private double shanyuan;
    private int shanquan;
    private int storyCounts;
    private int challageCounts;
    private List<PublisherInfo> mPublisherInfos;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFocusCounts() {
        return focusCounts;
    }

    public void setFocusCounts(int focusCounts) {
        this.focusCounts = focusCounts;
    }

    public int getFansCounts() {
        return fansCounts;
    }

    public void setFansCounts(int fansCounts) {
        this.fansCounts = fansCounts;
    }

    public List<StoryInfo> getStoryInfos() {
        return storyInfos;
    }

    public void setStoryInfos(List<StoryInfo> storyInfos) {
        this.storyInfos = storyInfos;
    }

    public double getShanyuan() {
        return shanyuan;
    }

    public void setShanyuan(double shanyuan) {
        this.shanyuan = shanyuan;
    }

    public int getShanquan() {
        return shanquan;
    }

    public void setShanquan(int shanquan) {
        this.shanquan = shanquan;
    }

    public int getStoryCounts() {
        return storyCounts;
    }

    public void setStoryCounts(int storyCounts) {
        this.storyCounts = storyCounts;
    }

    public int getChallageCounts() {
        return challageCounts;
    }

    public void setChallageCounts(int challageCounts) {
        this.challageCounts = challageCounts;
    }

    public List<PublisherInfo> getPublisherInfos() {
        return mPublisherInfos;
    }

    public void setPublisherInfos(List<PublisherInfo> publisherInfos) {
        mPublisherInfos = publisherInfos;
    }
}
