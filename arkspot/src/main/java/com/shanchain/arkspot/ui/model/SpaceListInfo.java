package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/13.
 */

public class SpaceListInfo {

    private String code;
    private String message;
    private List<SpaceBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SpaceBean> getData() {
        return data;
    }

    public void setData(List<SpaceBean> data) {
        this.data = data;
    }
}
