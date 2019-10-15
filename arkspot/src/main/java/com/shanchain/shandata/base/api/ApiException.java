package com.shanchain.shandata.base.api;

/**
 * Api的错误类
 * Created by 胡茂柜 on 2017/2/16.
 */

public class ApiException extends Exception {

    //未登录的错误码
    public static final int ERROR_CODE_NOLOGIN = 0x01;
    public static final int ERROR_FAIL = 0x02;

    private int mErrorCode;
    private String mMsg;

    public ApiException(int errorCode , String msg){
        this.mErrorCode = errorCode;
        this.mMsg = msg;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getMsg() {
        return mMsg;
    }
}
