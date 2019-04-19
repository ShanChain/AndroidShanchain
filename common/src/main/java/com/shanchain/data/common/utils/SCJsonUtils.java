package com.shanchain.data.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.NetErrCode;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/18.
 */

public class SCJsonUtils {

    private SCJsonUtils() {
    }

    public static String parseCode(String response) {
        return JSONObject.parseObject(response).getString("code");
    }

    public static String parseData(String response) {
        String code = JSONObject.parseObject(response).getString("code");
        if (NetErrCode.SUC_CODE.equals(code) || NetErrCode.COMMON_SUC_CODE.equals(code)) {
            return JSONObject.parseObject(response).getString("data");
        } else {
            return null;
        }
    }

    public static String parseMsg(String response) {
        if (JSONObject.parseObject(response).getString("message") != null) {
            String message = JSONObject.parseObject(response).getString("message");
            return message;
        } else if (JSONObject.parseObject(response).getString("msg") != null) {
            String msg = JSONObject.parseObject(response).getString("msg");
            return msg;
        } else {
            String resultMsg = "";
            return resultMsg;
        }

    }

    public static boolean parseBoolean(String jsonStr, String key) {
        return JSONObject.parseObject(jsonStr).getBoolean(key);
    }

    public static String parseString(String jsonStr, String key) {
        return JSONObject.parseObject(jsonStr).getString(key);

    }

    public static int parseInt(String jsonStr, String key) {
        return JSONObject.parseObject(jsonStr).getIntValue(key);
    }

    public static <T> T parseObj(String jsonStr, Class<T> clazz) {
        return JSONObject.parseObject(jsonStr, clazz);
    }

    public static List parseArr(String jsonStr, Class clazz) {
        return JSONObject.parseArray(jsonStr, clazz);
    }
}
