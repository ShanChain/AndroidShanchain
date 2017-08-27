package com.shanchain.netrequest;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by zhoujian on 2017/8/22.
 */

public class SCHttpUtlis {
    public static GetBuilder get() {
        return OkHttpUtils.get();
    }

    public static PostFormBuilder post() {
        return OkHttpUtils.post();
    }

    /**
     *  描述：带基础请求参数的get请求
     */
    public static GetBuilder getWithParams() {
        return OkHttpUtils.get()
                .addParams("AppID", "CHANNEL")          //渠道信息
                //  .addParams("DeviceID", SystemUtils.getSystemDeviceId(MyApplication.getContext()))   //设备id
                .addParams("Os","Android")              //操作系统
                //.addParams("OsVersion", VersionUtils.getVersionName(MyApplication.getContext()))    //app版本
                .addParams("ScreenSize","")             //屏幕尺寸
                .addParams("Timestamp",System.currentTimeMillis()+"")       //时间戳
                .addParams("ApiVersion",VersionUtils.getApiVersion())       //系统api等级
                .addParams("RequestId",getRequestId())   //
                .addParams("Signture","");               //签名
    }

    /**
     *  描述：带基础请求参数的post请求
     */
    public static PostFormBuilder postWithParams(){


        return OkHttpUtils.post()
                .addParams("AppID", "CHANNEL")          //渠道信息
                // .addParams("DeviceID", SystemUtils.getSystemDeviceId(MyApplication.getContext()))   //设备id
                .addParams("Os","Android")              //操作系统
               // .addParams("OsVersion", VersionUtils.getVersionName(MyApplication.getContext()))    //app版本
                .addParams("ScreenSize","")             //屏幕尺寸
                .addParams("Timestamp",System.currentTimeMillis()+"")       //时间戳
                .addParams("ApiVersion",VersionUtils.getApiVersion())       //系统api等级
                .addParams("RequestId",getRequestId())   //
                .addParams("Signture","");               //签名
    }


    /**
     *  描述：用于登录的带基础请求参数的post请求
     */
    public static PostFormBuilder postWithParamsForLogin(){

        return OkHttpUtils.post()
                .addParams("AppID", "CHANNEL")          //渠道信息
                // .addParams("DeviceID", SystemUtils.getSystemDeviceId(MyApplication.getContext()))   //设备id
                .addParams("Os","Android")              //操作系统
                //.addParams("OsVersion", VersionUtils.getVersionName(MyApplication.getContext()))    //app版本
                .addParams("ScreenSize","")             //屏幕尺寸
                .addParams("ApiVersion",VersionUtils.getApiVersion())       //系统api等级
                .addParams("RequestId",getRequestId())   //
                .addParams("Signture","");               //签名

    }


    public static String getRequestId() {

        UUID uid = UUID.randomUUID();

        long hBits = uid.getLeastSignificantBits();

        long lBits = uid.getMostSignificantBits();

        byte[] bytes = ByteBuffer.allocate(16).putLong(hBits).putLong(lBits).array();

        return Base64Utils.encode(bytes);

    }

}
