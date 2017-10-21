package com.shanchain.data.common.cache;

import android.content.Context;
import android.text.TextUtils;

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

}
