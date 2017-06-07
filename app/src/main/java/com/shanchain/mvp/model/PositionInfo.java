package com.shanchain.mvp.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/6/7.
 */

public class PositionInfo implements Serializable{

    private String details;
    private String address;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
