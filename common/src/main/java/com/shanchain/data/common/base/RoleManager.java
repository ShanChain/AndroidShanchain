package com.shanchain.data.common.base;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;

public class RoleManager {

    public static void switchRoleCache(int characterId,String characterInfo,int spaceId,String spaceInfo,String userName,String pwd){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);

        SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_ID,spaceId+"");
        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_ID,characterId + "");
        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_INFO,characterInfo);
        SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_INFO,spaceInfo);

        SCCacheUtils.setCache(userId,Constants.CACHE_HX_USER_NAME,userName);
        SCCacheUtils.setCache(userId,Constants.CACHE_HX_PWD,pwd);


        String token = SCCacheUtils.getCache(userId, Constants.CACHE_TOKEN);

        JSONObject gData = new JSONObject();
        gData.put("userId",userId);
        gData.put("token",token);
        gData.put("spaceId",spaceId + "");
        gData.put("characterId",characterId+"");
        SCCacheUtils.setCache("0", Constants.CACHE_GDATA,gData.toString());


    }


    public static void switchRoleCache(String characterId,String characterInfo,String spaceId,String spaceInfo,String userName,String pwd){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);

        SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_ID,spaceId);
        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_ID,characterId);
        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_INFO,characterInfo);
        SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_INFO,spaceInfo);

        String token = SCCacheUtils.getCache(userId, Constants.CACHE_TOKEN);

        JSONObject gData = new JSONObject();
        gData.put("userId",userId);
        gData.put("token",token);
        gData.put("spaceId",spaceId );
        gData.put("characterId",characterId);
        SCCacheUtils.setCache("0", Constants.CACHE_GDATA,gData.toString());


    }

}
