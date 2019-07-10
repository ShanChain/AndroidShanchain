package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/7/10
 * Describe :手机号前缀
 */
public class PhoneFrontBean implements Serializable {
    String contry;
    String phoneFront;
    String front;//去除加号的手机号前缀字符
    int sourceType;

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getPhoneFront() {
        return phoneFront;
    }

    public void setPhoneFront(String phoneFront) {
        this.phoneFront = phoneFront;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }
}
