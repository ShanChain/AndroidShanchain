package com.shanchain.data.common.net;

/**
 * Created by flyye on 2017/10/11.
 */

public class NetErrCode {

    //服务端错误码6位
    public static final String COMMON_SUC_CODE = "000000";
    public static final String COMMON_ERR_CODE = "999999";
    public static final String SMSVC_ERR_CODE = "999998";
    public static final String USER_REPEAT_ERR_CODE = "999997";//账号已存在
    public static final String LOGIN_ERR_CODE = "999996";//账号或密码已错误
    public static final String PARAMS_ERR_CODE = "999995";

    //本地访问错误码
    public static final String REQUEST_NO_NETWORK = "L_00000";
    public static final String REQUEST_NO_PARAMS = "L_00001";
    public static final String REQUEST_METHOD_ERROR = "L_00002";

}