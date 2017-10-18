package com.shanchain.data.common.net;

import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by zhoujian on 2017/8/22.
 */

public class SCHttpUtils {


    public static GetBuilder get() {
        return OkHttpUtils.get();
    }

    public static PostFormBuilder post() {
        return OkHttpUtils.post();
    }

    public static PostFormBuilder postWithSpaceId() {
        String userId = SCCacheUtils.getCache("0", "curUser");
        String spaceId = SCCacheUtils.getCache(userId, "spaceId");
        return OkHttpUtils.post()
                .addParams("spaceId",spaceId);
    }

    public static PostFormBuilder postFWhitSpceAndChaId() {
        String userId = SCCacheUtils.getCache("0", "curUser");
        String spaceId = SCCacheUtils.getCache(userId, "spaceId");
        String characterId = SCCacheUtils.getCache(userId, "characterId");
        LogUtils.d("缓存中获取的spaceid" + spaceId);
        return OkHttpUtils.post()
                .addParams("spaceId",spaceId)
                .addParams("characterId",characterId);
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
