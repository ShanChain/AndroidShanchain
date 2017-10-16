package com.shanchain.arkspot.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/10/10.
 */

public class TagContentBean implements Serializable{

    /**
     * rate : 0.0
     * tagId : 40
     * tagName : 奇幻
     */

    private double rate;
    private int tagId;
    private String tagName;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
