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
    //String BASE_URL = "http://115.29.177.22:8080";

    //真实服务器跟地址
    //String REAL_BASE_URL = "https://api.shanchain.com";

    //测试服务器跟地址
    String REAL_BASE_URL = "http://192.168.0.125:8080";


    /************《《《《《  用户相关的接口  ***************/

    //注册用户
    String USER_REGISTER = REAL_BASE_URL + "/v1/user/register";
    //短信验证
    String USER_CHECK_SMS = REAL_BASE_URL + "/v1/sms/unlogin/verifycode";
    //邮箱验证码
    String USER_CHECK_EMAIL = REAL_BASE_URL + "/v1/mail/verifycode";
    //获取用户信息
    String GET_USER_INFO = REAL_BASE_URL + "/v1/userinfo/get";

    //登录
    String USER_LOGIN = REAL_BASE_URL + "/v1/user/login";
    //重置密码
    String USER_RESET_PWD = REAL_BASE_URL + "/v1/user/reset_password";
    //第三方登录
    String USER_THIRD_LOGIN = REAL_BASE_URL + "/v1/user/third_login";
    //绑定其他账号
    String USER_BIND_OTHER_ACCOUNT = REAL_BASE_URL + "/v1/user/bind_other_account";
    //用户反馈
    String USER_FEEDBACK = REAL_BASE_URL + "/v1/user/feedback";
    //更新用户信息
    String USER_UPDATE_INFO = REAL_BASE_URL + "/v1/user/update";

    /***********  用户相关的接口  》》》》》 ***********/





    /************ 《《《《《  挑战相关接口   ************/




    /************  挑战相关接口  》》》》》 ************/


    /************ 《《《《《  故事相关接口   ************/




    /************  故事相关接口  》》》》》 ************/




    /************ 《《《《《  我的相关接口   ************/



    /************  我的相关接口  》》》》》 ************/




    /*********《《《《《 设置相关接口  ************/


    /************  设置相关接口  》》》》》 **********/

}