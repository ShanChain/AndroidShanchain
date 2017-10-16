package com.shanchain.data.common.net;

/**
 * Created by zhoujian on 2017/8/22.
 */

public interface SCHttpApi {

    /*************《《《《《《《网络请求的响应码*****************/
    String COMMON_SUC_CODE = "000000";      //通用成功

    String COMMON_ERR_CODE = "999999";      //通用失败

    String SMSVC_ERR_CODE = "999998";       //服务器错误

    String USER_REPEAT_ERR_CODE = "999997"; //账号已存在

    String LOGIN_ERR_CODE = "999996";       //账号或密码已错误

    /*************网络请求的响应码》》》》》》*****************/

}
