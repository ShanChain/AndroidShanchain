package com.shanchain.data.common.net;

/**
 * Created by flyye on 2017/10/11.
 */

public class NetErrCode {

    //服务端错误码6位
    public static final String COMMON_SUC_CODE = "000000";
    public static final String SUC_CODE = "200";
    public static final String BALANCE_NOT_ENOUGH = "10001"; //余额不足
    public static final String WALLET_NOT_CREATE = "10003"; //没有创建钱包
    public static final String WALLET_PASSWORD_INVALID = "10004"; //钱包密码已失效
    public static final String WALLET_NOT_CREATE_PASSWORD = "10024"; //没有设置钱包密码
    public static final String COUPON_INVALID_QRCODE = "10050"; //非发券人，不能核销
    public static final String TRANSACTION_FAILURE = "10009"; //交易失败
    public static final String COMMON_ERR_CODE = "999999";
    public static final String COMMON_UNOPENED_CODE = "999987";//待开发/未开放的功能
    public static final String SMSVC_ERR_CODE = "999998";
    public static final String USER_REPEAT_ERR_CODE = "999997";//账号已存在
    public static final String UN_VERIFIED_CODE = "999970";//未实名认证
    public static final String LOGIN_ERR_CODE = "999996";//账号或密码已错误
    public static final String HAVE_BEEN_CODE = "999977";//该元社区已创建
    public static final String PARAMS_ERR_CODE = "999995";
    public static final String BIND_PONE_ERR_CODE = "999993";
    public static final String SPACE_CREATE_ERR_CODE = "999992";//创建时空，人物模型，话题名重复
    public static final String COMMON_TOKEN_OVERDUE_CODE = "999991";    //token过期
    public static final String COMMON_SPACE_PERMISSION = "999990";
    public static final String ACCOUNT_HAS_BINDED = "999989";   //账号已绑定
    public static final String VOD_UNCOMPLETED_TRANSCODING = "999988";  //视频转码中。。。
    //本地访问错误码
    public static final String REQUEST_NO_NETWORK = "L_00000";
    public static final String REQUEST_NO_PARAMS = "L_00001";
    public static final String REQUEST_METHOD_ERROR = "L_00002";

    //上传钱包密码时选择图片返回
    public static final int WALLET_PHOTO = 10004;
    //添加元社区成功
    public static final int ADD_ROOM_SUCCESS = 99977;
    //刷新社区帮，
    public static final int REFRESH_MY_TASK = 99966;
}
