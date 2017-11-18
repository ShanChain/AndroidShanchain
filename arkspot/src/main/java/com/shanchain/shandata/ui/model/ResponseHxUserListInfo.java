package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/28.
 */

public class ResponseHxUserListInfo {


    /**
     * code : 000000
     * data : [{"characterId":93,"hxUserName":"20"},{"characterId":92,"hxUserName":"sc-1425612084"},{"characterId":91,"hxUserName":"sc-1425612085"},{"characterId":93,"hxUserName":"sc2054380322"}]
     * message : ok
     */

    private String code;
    private String message;
    private List<ResponseHxUerBean> data;

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

    public List<ResponseHxUerBean> getData() {
        return data;
    }

    public void setData(List<ResponseHxUerBean> data) {
        this.data = data;
    }
}
