package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/28.
 */

public class RegisterHxInfo {


    /**
     * code : 000000
     * message : ok
     * data : {"hxUserName":"sc-738727121","characterId":128,"userId":4,"hxPassword":"hx1509176195926"}
     */

    private String code;
    private String message;
    private RegisterHxBean data;

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

    public RegisterHxBean getData() {
        return data;
    }

    public void setData(RegisterHxBean data) {
        this.data = data;
    }
}
