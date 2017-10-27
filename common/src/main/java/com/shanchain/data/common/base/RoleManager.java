package com.shanchain.data.common.base;

import com.shanchain.data.common.cache.SCCacheUtils;

public class RoleManager {

    public static void switchRoleCache(int characterId,String characterInfo,int spaceId,String spaceInfo){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);

        SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_ID,spaceId+"");
        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_ID,characterId + "");
        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_INFO,characterInfo);
        SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_INFO,spaceInfo);
    }

}
