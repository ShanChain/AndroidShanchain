package com.shanchain.data.common.cache;

import android.content.Context;
import android.text.TextUtils;

import com.shanchain.data.common.base.Constants;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class SCCacheUtils {
    private static Context mContext;

    public static void initCache(Context context){
        mContext = context.getApplicationContext();
    }

    private SCCacheUtils(){}

    public static String getCache(String userId,String key){

        if (TextUtils.isEmpty(userId)){
            return "";
        }

        return CommonCacheHelper.getInstance().getCache(userId, key);
    }

    public static void setCache(String userId ,String key ,String value){
        if (TextUtils.isEmpty(key)){
            return;
        }
        CommonCacheHelper.getInstance().setCache(userId,key,value);
    }


    public static String getCacheUserId(){
      return  getCache("0", Constants.CACHE_CUR_USER);
    }

    public static String getCacheCharacterId(){
        String userId = getCacheUserId();
        return getCache(userId, Constants.CACHE_CHARACTER_ID);
    }

    public static String getCacheSpaceId(){
        String userId = getCacheUserId();
        return getCache(userId,Constants.CACHE_SPACE_ID);
    }

    public static String getCacheCharacterInfo(){
        String userId = getCacheUserId();
        return getCache(userId,Constants.CACHE_CHARACTER_INFO);
    }

    public static String getCacheSpaceInfo() {
        String userId = getCacheUserId();
        return getCache(userId,Constants.CACHE_SPACE_INFO);
    }

    public static String getCacheGData(){
        String userId = getCacheUserId();
        return getCache(userId,Constants.CACHE_GDATA);
    }

    public static String getCacheHxUserName(){
        String userId = getCacheUserId();
        return getCache(userId,Constants.CACHE_HX_USER_NAME);
    }

    public static String getCacheHxPwd(){
        String userId = getCacheUserId();
        return getCache(userId,Constants.CACHE_HX_PWD);
    }

}
