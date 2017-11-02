package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/27.
 */

public class ResponseFocusInfo {


    /**
     * code : 000000
     * data : {"createTime":1,"key":{"characterId":12,"funsId":23}}
     * message : ok
     */

    public String code;
    private ResponseFocusData data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResponseFocusData getData() {
        return data;
    }

    public void setData(ResponseFocusData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
