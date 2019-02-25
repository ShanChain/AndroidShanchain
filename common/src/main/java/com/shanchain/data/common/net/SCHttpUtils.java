package com.shanchain.data.common.net;

import android.text.TextUtils;

import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SystemUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.nio.ByteBuffer;
import java.util.UUID;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.shanchain.data.common.base.Constants.CACHE_CUR_USER;
import static com.shanchain.data.common.base.Constants.CACHE_TOKEN;

/**
 * Created by zhoujian on 2017/8/22.
 */

public abstract class SCHttpUtils {

    public static GetBuilder getAndToken() {
        String userId = SCCacheUtils.getCache("0", "curUser");
        String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
        return OkHttpUtils.get().addParams("token", token);
    }

    public static GetBuilder getNoToken() {
        return OkHttpUtils.get();
    }

    public static GetBuilder get() {
        String userId = SCCacheUtils.getCache("0", "curUser");
        String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
        return OkHttpUtils.get().addParams("token", token);
    }

    public static PostFormBuilder post() {
        String userId = SCCacheUtils.getCache("0", "curUser");
        String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
        return OkHttpUtils.post()
                .addParams("token", token);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM_DATA = MediaType.parse("multipart/form-data");

    public static void postByBody(String url, String json, Callback callback) {
        try {
            String userId = SCCacheUtils.getCache("0", "curUser");
            String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
            OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url + "?token=" + token)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postByBody(String url, RequestBody body, Callback callback) {
        try {
            String userId = SCCacheUtils.getCache("0", "curUser");
            String token = SCCacheUtils.getCache(userId, CACHE_TOKEN);
            OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 描述：带当前时空id的post请求
     */
    public static PostFormBuilder postWithSpaceId() {
        String userId = SCCacheUtils.getCache("0", "curUser");
        String spaceId = SCCacheUtils.getCache(userId, "spaceId");
        return post()
                .addParams("spaceId", spaceId);

    }

    public static PostFormBuilder postWithUidAndSpaceId() {
        String spaceId = SCCacheUtils.getCacheSpaceId();
        return postWithUserId().addParams("spaceId", spaceId);
    }

    /**
     * 描述：带当前角色id的post请求
     */
    public static PostFormBuilder postWithChaId() {
        String userId = SCCacheUtils.getCache("0", "curUser");
        String characterId = SCCacheUtils.getCache(userId, "characterId");
        return post()
                .addParams("characterId", characterId);
    }


    public static PostFormBuilder postNoToken() {
        return OkHttpUtils.post();
    }

    /**
     * 描述：带当前时空id和当前角色id的post请求
     */
    public static PostFormBuilder postWithSpaceAndChaId() {
        String userId = SCCacheUtils.getCache("0", "curUser");
        String spaceId = SCCacheUtils.getCache(userId, "spaceId");
        String characterId = SCCacheUtils.getCache(userId, "characterId");

        LogUtils.d("缓存中获取的spaceid" + spaceId);
        return post()
                .addParams("spaceId", spaceId)
                .addParams("characterId", characterId);
    }

    /**
     * 描述：带userid的post请求
     */
    public static PostFormBuilder postWithUserId() {

        String userId = SCCacheUtils.getCache("0", CACHE_CUR_USER);
        return post()
                .addParams("userId", userId);
    }

    public static PostFormBuilder postWithUidAndCharId() {
        String cacheCharacterId = SCCacheUtils.getCacheCharacterId();
        return postWithUserId().addParams("characterId", cacheCharacterId);
    }


    public static PostFormBuilder postWithUidSpaceIdAndCharId() {
        String userId = SCCacheUtils.getCacheUserId();
        String characterId = SCCacheUtils.getCacheCharacterId();
        String spaceId = SCCacheUtils.getCacheSpaceId();
        return post()
                .addParams("userId", userId)
                .addParams("spaceId", spaceId)
                .addParams("characterId", characterId);
    }

    /**
     * 描述：带基础请求参数的get请求
     */
    public static GetBuilder getWithParams() {
        return OkHttpUtils.get()
                .addParams("AppID", "CHANNEL")          //渠道信息
                .addParams("DeviceID", SystemUtils.getSystemDeviceId(AppManager.getInstance().getContext()))   //设备id
                .addParams("Os", "Android")              //操作系统
                .addParams("OsVersion", VersionUtils.getVersionName(AppManager.getInstance().getContext()))    //app版本
                .addParams("ScreenSize", "")             //屏幕尺寸
                .addParams("Timestamp", System.currentTimeMillis() + "")       //时间戳
                .addParams("ApiVersion", VersionUtils.getApiVersion())       //系统api等级
                .addParams("RequestId", getRequestId())   //
                .addParams("Signture", "");               //签名
    }

    /**
     * 描述：带基础请求参数的post请求
     */
    public static PostFormBuilder postWithParams() {

        return OkHttpUtils.post()
                .addParams("AppID", "CHANNEL")          //渠道信息
                // .addParams("DeviceID", SystemUtils.getSystemDeviceId(MyApplication.getContext()))   //设备id
                .addParams("Os", "Android")              //操作系统
                // .addParams("OsVersion", VersionUtils.getVersionName(MyApplication.getContext()))    //app版本
                .addParams("ScreenSize", "")             //屏幕尺寸
                .addParams("Timestamp", System.currentTimeMillis() + "")       //时间戳
                .addParams("ApiVersion", VersionUtils.getApiVersion())       //系统api等级
                .addParams("RequestId", getRequestId())   //
                .addParams("Signture", "");               //签名
    }


    /**
     * 描述：用于登录的带基础请求参数的post请求
     */
    public static PostFormBuilder postWithParamsForLogin() {
        String deviceToken = CommonCacheHelper.getInstance().getCache(SCCacheUtils.getCacheUserId(), "deviceToken");
        return OkHttpUtils.post()
                .addParams("deviceToken", deviceToken == null ? "" : deviceToken)
                .addParams("AppID", "CHANNEL")          //渠道信息
                // .addParams("DeviceID", SystemUtils.getSystemDeviceId(MyApplication.getContext()))   //设备id
                .addParams("Os", "Android")              //操作系统
                //.addParams("OsVersion", VersionUtils.getVersionName(MyApplication.getContext()))    //app版本
                //.addParams("ScreenSize","")             //屏幕尺寸
                .addParams("ApiVersion", VersionUtils.getApiVersion())       //系统api等级
                .addParams("RequestId", getRequestId())   //
                .addParams("Signture", "");               //签名

    }


    public static String getRequestId() {

        UUID uid = UUID.randomUUID();

        long hBits = uid.getLeastSignificantBits();

        long lBits = uid.getMostSignificantBits();

        byte[] bytes = ByteBuffer.allocate(16).putLong(hBits).putLong(lBits).array();
        String encode = Base64Utils.encode(bytes);
        if (TextUtils.isEmpty(encode)) {
            return "";
        } else {
            return encode;
        }


    }

}
