package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/10/13.
 */

public class TailBean implements Serializable{

    /**
     * name : 时空大圣
     * rate : 0
     * tailId : 12
     */

    private String name;
    private int rate;
    private int tailId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getTailId() {
        return tailId;
    }

    public void setTailId(int tailId) {
        this.tailId = tailId;
    }
}
