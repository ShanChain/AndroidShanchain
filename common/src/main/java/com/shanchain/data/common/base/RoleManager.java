package com.shanchain.data.common.base;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

public class RoleManager {

    /**
     * 切换角色
     * @param modelId
     * @param spaceId
     * @param spaceInfo  可以为空
     */
    public static void switchRole(String modelId,String spaceId,String spaceInfo){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("modelId",modelId);
        jsonObject.put("spaceId",spaceId);
        if(!TextUtils.isEmpty(spaceInfo)){
            jsonObject.put("userInfo",spaceInfo);
        }
        SCBaseEvent baseEvent = new SCBaseEvent(EventConstant.EVENT_MODULE_ARKSPOT,EventConstant.EVENT_KEY_SWITCH_ROLE,jsonObject,null);
        EventBus.getDefault().post(baseEvent);
    }

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
        SCCacheUtils.setCache(userId,Constants.CACHE_HX_USER_NAME,userName);
        SCCacheUtils.setCache(userId,Constants.CACHE_HX_PWD,pwd);
        JSONObject gData = new JSONObject();
        gData.put("userId",userId);
        gData.put("token",token);
        gData.put("spaceId",spaceId );
        gData.put("characterId",characterId);
        SCCacheUtils.setCache("0", Constants.CACHE_GDATA,gData.toString());

    }

    public static void switchJmRoleCache(String characterId,String userName,String pwd){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);

        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_ID,characterId);

        String token = SCCacheUtils.getCache(userId, Constants.CACHE_TOKEN);
        SCCacheUtils.setCache(userId,Constants.CACHE_HX_USER_NAME,userName);
        SCCacheUtils.setCache(userId,Constants.CACHE_HX_PWD,pwd);
        JSONObject gData = new JSONObject();
        gData.put("userId",userId);
        gData.put("token",token);
        gData.put("characterId",characterId);
        SCCacheUtils.setCache("0", Constants.CACHE_GDATA,gData.toString());

    }

    public static void switchRoleCacheComment(String characterId,String characterInfo,String spaceId,String spaceInfo){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_ID,spaceId);
        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_ID,characterId);
        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_INFO,characterInfo);
        SCCacheUtils.setCache(userId,Constants.CACHE_SPACE_INFO,spaceInfo);
        JSONObject gData = new JSONObject();
        String token = SCCacheUtils.getCache(userId, Constants.CACHE_TOKEN);
        gData.put("userId",userId);
        gData.put("token",token);
        gData.put("spaceId",spaceId );
        gData.put("characterId",characterId);
        SCCacheUtils.setCache("0", Constants.CACHE_GDATA,gData.toString());
    }

    public static void switchRoleCacheHx(String hxUser,String pwd){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        SCCacheUtils.setCache(userId,Constants.CACHE_HX_USER_NAME,hxUser);
        SCCacheUtils.setCache(userId,Constants.CACHE_HX_PWD,pwd);
    }

    public static void switchRoleCacheRoomId(String roomId){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        SCCacheUtils.setCache(userId,Constants.CACHE_ROOM_ID,roomId);
    }

    public static void switchRoleCacheCharacterInfo(String characterInfo){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        SCCacheUtils.setCache(userId,Constants.CACHE_CHARACTER_INFO,characterInfo);
    }

    public static void switchRoleCacheHeadImg(String headImg){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        SCCacheUtils.setCache(userId,Constants.CACHE_HEAD_IMG,headImg);
    }

    public static void switchRoleThirdToken(String userType,String accessToken){
        String userId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        SCCacheUtils.setCache(userId,userType,accessToken);
    }


}
