package com.shanchain.data.common.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujian on 2017/11/18.
 */

public class SCJsonUtils {

    private SCJsonUtils(){}

    public static String parseCode(String response){
       return JSONObject.parseObject(response).getString("code");
    }

    public static String parseData(String response){
        return JSONObject.parseObject(response).getString("data");
    }

    public static String parseMsg(String response){
        return JSONObject.parseObject(response).getString("message");
    }

    public static boolean parseBoolean(String jsonStr , String key){
        return JSONObject.parseObject(jsonStr).getBoolean(key);
    }

    public static String parseString(String jsonStr , String key){
        return JSONObject.parseObject(jsonStr).getString(key);
    }

    public static int parseInt(String jsonStr , String key){
        return JSONObject.parseObject(jsonStr).getIntValue(key);
    }

    public static <T> T parseObj(String jsonStr, Class<T> clazz){
        return JSONObject.parseObject(jsonStr, clazz);
    }

    /*public static List<T> parseArr(String jsonStr,Class<T> clazz){
        return JSONObject.parseArray(jsonStr,clazz);
    }
*/
}
