package com.shanchain.data.common.net;

/**
 * Created by flyye on 2017/10/11.
 */

public class NetErrCode {

    //服务端错误码6位
    public static final String COMMON_SUC_CODE = "000000";
    public static final String COMMON_ERR_CODE = "999999";
    public static final String COMMON_UNOPENED_CODE = "999987";//待开发/未开放的功能
    public static final String SMSVC_ERR_CODE = "999998";
    public static final String USER_REPEAT_ERR_CODE = "999997";//账号已存在
    public static final String LOGIN_ERR_CODE = "999996";//账号或密码已错误
    public static final String PARAMS_ERR_CODE = "999995";
    public static final String SPACE_CREATE_ERR_CODE = "999992";//创建时空，人物模型，话题名重复
    public static final String COMMON_TOKEN_OVERDUE_CODE = "999991";    //token过期
    public static final String COMMON_SPACE_PERMISSION = "999990";
    public static final String ACCOUNT_HAS_BINDED = "999989";   //账号已绑定
    public static final String VOD_UNCOMPLETED_TRANSCODING = "999988";  //视频转码中。。。
    //本地访问错误码
    public static final String REQUEST_NO_NETWORK = "L_00000";
    public static final String REQUEST_NO_PARAMS = "L_00001";
    public static final String REQUEST_METHOD_ERROR = "L_00002";

}
