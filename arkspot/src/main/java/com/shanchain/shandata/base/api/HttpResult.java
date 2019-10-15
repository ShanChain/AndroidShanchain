package com.shanchain.shandata.base.api;

import com.google.gson.annotations.SerializedName;

/**
 * 响应结果的封装
 * Created by 胡茂柜 on 2017/2/16.
 */

public class HttpResult<T> {

    // TODO: 2017/2/16 具体json格式需要看到Api文档进行改动
    @SerializedName("code")
    public int resultCode;

    @SerializedName("data")
    public T data;
    @SerializedName("message")
    public String msg;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
