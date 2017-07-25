package com.shanchain.shandata.http;

/**
 * 存放网络请求的接口
 */

public interface HttpApi {

    /*************《《《《《《《网络请求的响应码*****************/
    String COMMON_SUC_CODE = "000000";      //通用成功

    String COMMON_ERR_CODE = "999999";      //通用失败

    String SMSVC_ERR_CODE = "999998";       //服务器错误

    String USER_REPEAT_ERR_CODE = "999997"; //账号已存在

    String LOGIN_ERR_CODE = "999996";       //账号或密码已错误

    /*************网络请求的响应码》》》》》》*****************/


    //请求根路径
    String BASE_URL = "http://115.29.177.22:8080";

    //模拟请求跟地址
    String BASE_MOCKJS_URL = "http://115.29.177.22:18480/mockjs/1";

    //真实服务器跟地址
    String REAL_BASE_URL = "http://192.168.0.143:8080";



    /************《《《《《  用户相关的接口  ***************/

    //创建用户
    String USER_CREATE = BASE_URL + "/v1/user/create";
    //注册用户
    String USER_REGISTER = REAL_BASE_URL + "/v1/user/register";
    //短信验证
    String USER_CHECK_SMS = REAL_BASE_URL + "/v1/sms/unlogin/verifycode";
    //邮箱验证码
    String USER_CHECK_EMAIL = REAL_BASE_URL + "/v1/mail/verifycode";


    //登录
    String USER_LOGIN = REAL_BASE_URL + "/v1/user/login";
    //重置密码
    String USER_RESET_PWD = REAL_BASE_URL + "/v1/user/reset_password";
    //第三方登录
    String USER_THIRD_LOGIN = REAL_BASE_URL + "/v1/user/third_login";


    /***********  用户相关的接口  》》》》》 ***********/





    /************ 《《《《《  挑战相关接口   ************/


    /************  挑战相关接口  》》》》》 ************/





    /*********《《《《《 设置相关接口  ************/


    /************  设置相关接口  》》》》》 **********/

}
