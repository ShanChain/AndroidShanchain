package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/12/1.
 */

public class AtBean implements Serializable{
    private String name;
    private int atId;

    public AtBean(String name, int atId) {
        this.name = name;
        this.atId = atId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAtId() {
        return atId;
    }

    public void setAtId(int atId) {
        this.atId = atId;
    }
}
