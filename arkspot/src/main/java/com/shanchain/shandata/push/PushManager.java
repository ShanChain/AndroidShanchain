package com.shanchain.shandata.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.CommonCacheHelper;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.chat.ContactActivity;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.umeng.message.UTrack;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;

import static com.shanchain.data.common.base.Constants.CACHE_CUR_USER;
import static com.shanchain.data.common.base.Constants.CACHE_USER_MSG;
import static com.shanchain.data.common.base.Constants.CACHE_USER_MSG_READ_STATUS;
import static com.shanchain.shandata.push.PushConstants.MSG_CHARACTER_BE_FOCUS;
import static com.shanchain.shandata.push.PushConstants.MSG_CHARACTER_BE_FOLLOW;
import static com.shanchain.shandata.push.PushConstants.MSG_DYNAMIC_BE_FORWARD;
import static com.shanchain.shandata.push.PushConstants.MSG_STORY_BE_COMMENT;
import static com.shanchain.shandata.push.PushConstants.MSG_STORY_BE_PRAISE;

/**
 * Created by flyye on 2017/11/17.
 */

public class PushManager {

    public static void dealWithCustomClickAction(Context context, UMessage message){
        if(TextUtils.isEmpty(message.custom)){
            return;
        }
        String userId = CommonCacheHelper.getInstance().getCache("0",Constants.CACHE_CUR_USER);
        JSONObject jsonObject = JSONObject.parseObject(message.custom);
        if(!TextUtils.isEmpty(jsonObject.getString("send_userId")) && jsonObject.getString("send_userId").equalsIgnoreCase(userId)){
            return;
        }
        String msgKey = jsonObject.getString("msg_key");
        JSONObject msgBody = jsonObject.getJSONObject("msg_body");
        JSONObject actionBody = msgBody.getJSONObject("action_body");
        JSONObject params = actionBody.getJSONObject("params");
        String time = jsonObject.getString("create_time");


        if(msgBody.getString("action_type").equalsIgnoreCase("open_page")){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            switch (actionBody.getString("page_name")){
                case PushConstants.PAGE_STORY:
                    intent.setClass(context,MainActivity.class);
                    intent.putExtra("fragmentId",0);
                    break;
                case PushConstants.PAGE_CHAT:
                    intent.setClass(context,MainActivity.class);
                    intent.putExtra("fragmentId",1);
                    break;
                case PushConstants.PAGE_SPACE:
                    intent.setClass(context,MainActivity.class);
                    intent.putExtra("fragmentId",2);
                    break;
                case PushConstants.PAGE_MINE:
                    intent.setClass(context,MainActivity.class);
                    intent.putExtra("fragmentId",3);
                    break;
                case PushConstants.PAGE_CHARACTER:
                    intent.setClass(context,FriendHomeActivity.class);
                    intent.putExtra("characterId",Integer.parseInt(actionBody.getJSONObject("params").getString("characterId")));
                    break;
                case PushConstants.PAGE_CONTACT:
                    intent.setClass(context,ContactActivity.class);
                    break;
                case PushConstants.PAGE_NOTIFICATION:
                    NavigatorModule.startReactPage(context, RNPagesConstant.NotificationScreen,new Bundle());
                    return;
                case PushConstants.PAGE_ROLE:
                    RNDetailExt detailExt = new RNDetailExt();
                    RNGDataBean gDataBean = new RNGDataBean();
                    String uId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
                    String characterId = SCCacheUtils.getCache(uId, Constants.CACHE_CHARACTER_ID);
                    String spaceId = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_ID);
                    String token = SCCacheUtils.getCache(uId, Constants.CACHE_TOKEN);
                    Bundle bundle = new Bundle();
                    gDataBean.setCharacterId(characterId);
                    gDataBean.setSpaceId(spaceId);
                    gDataBean.setToken(token);
                    gDataBean.setUserId(uId);
                    detailExt.setgData(gDataBean);
                    detailExt.setModelId(actionBody.getJSONObject("params").getString("modelId") + "");
                    String json =JSONObject.toJSONString(detailExt);
                    bundle.putString(NavigatorModule.REACT_PROPS, json);
                    NavigatorModule.startReactPage(context, RNPagesConstant.RoleDetailScreen,bundle);
                    return;
                case PushConstants.PAGE_SETTING:
                    NavigatorModule.startReactPage(context, RNPagesConstant.SettingScreen,new Bundle());                    intent.putExtra("fragmentId",1);
                    break;
                default:
                    return;

            }
            context.startActivity(intent);
        }else if (msgBody.getString("action_type").equalsIgnoreCase("red_point")){
             String msg = CommonCacheHelper.getInstance().getCache(userId,CACHE_USER_MSG);
            if(TextUtils.isEmpty(msg)){
                msg = new JSONObject().toString();
            }
            JSONObject msgJson = JSONObject.parseObject(msg);
            JSONObject data = new JSONObject();
            switch (msgKey){
                case MSG_CHARACTER_BE_FOCUS:
                    if(TextUtils.isEmpty(params.getString("storyId"))){
                        return;
                    }
                    data.put("storyId",params.getString("storyId"));
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_CHARACTER_BE_FOCUS + params.getString("storyId"),data);
                    break;
                case MSG_STORY_BE_COMMENT:
                    if(TextUtils.isEmpty(params.getString("storyId"))){
                        return;
                    }
                    data.put("storyId",params.getString("storyId"));
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_STORY_BE_COMMENT + params.getString("storyId"),data);
                    break;
                case MSG_STORY_BE_PRAISE:
                    data.put("storyId",params.getString("storyId"));
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_STORY_BE_PRAISE + params.getString("storyId"),data);
                    break;
                case MSG_DYNAMIC_BE_FORWARD:
                    data.put("storyId",params.getString("storyId"));
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_DYNAMIC_BE_FORWARD + params.getString("storyId"),data);
                    break;
                case MSG_CHARACTER_BE_FOLLOW:
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_CHARACTER_BE_FOLLOW,data);
                    break;
                default:
                    break;
            }
            if(data.isEmpty()){
                return;
            }
            CommonCacheHelper.getInstance().setCache(userId,CACHE_USER_MSG,msgJson.toJSONString());
            CommonCacheHelper.getInstance().setCache(userId,CACHE_USER_MSG_READ_STATUS,"false");
            SCBaseEvent baseEvent = new SCBaseEvent(EventConstant.EVENT_MODULE_ARKSPOT,EventConstant.EVENT_KEY_NEW_MSG,null,null);
            EventBus.getDefault().post(baseEvent);
        }

    }


    public static void dealWithCustomMessage(Context context,UMessage message){
        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
        boolean isClickOrDismissed = true;
        if(isClickOrDismissed) {
            //自定义消息的点击统计
            UTrack.getInstance(context).trackMsgClick(message);
        } else {
            //自定义消息的忽略统计
            UTrack.getInstance(context).trackMsgDismissed(message);
        }
        if(TextUtils.isEmpty(message.custom)){
            return;
        }

        JSONObject jsonObject = JSONObject.parseObject(message.custom);
        String userId = CommonCacheHelper.getInstance().getCache("0",Constants.CACHE_CUR_USER);
        if(!TextUtils.isEmpty(jsonObject.getString("send_userId")) && jsonObject.getString("send_userId").equalsIgnoreCase(userId)){
            return;
        }
        String msgKey = jsonObject.getString("msg_key");
        JSONObject msgBody = jsonObject.getJSONObject("msg_body");
        JSONObject actionBody = msgBody.getJSONObject("action_body");
        JSONObject params = actionBody.getJSONObject("params");
        String time = jsonObject.getString("create_time");

         if (msgBody.getString("action_type").equalsIgnoreCase("red_point")){
            String msg = CommonCacheHelper.getInstance().getCache(userId,CACHE_USER_MSG);
            if(TextUtils.isEmpty(msg)){
                msg = new JSONObject().toString();
            }
            JSONObject msgJson = JSONObject.parseObject(msg);
            JSONObject data = new JSONObject();
            switch (msgKey){
                case MSG_CHARACTER_BE_FOCUS:
                    if(TextUtils.isEmpty(params.getString("storyId"))){
                        return;
                    }
                    data.put("storyId",params.getString("storyId"));
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_CHARACTER_BE_FOCUS + params.getString("storyId"),data);
                    break;
                case MSG_STORY_BE_COMMENT:
                    if(TextUtils.isEmpty(params.getString("storyId"))){
                        return;
                    }
                    data.put("storyId",params.getString("storyId"));
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_STORY_BE_COMMENT + params.getString("storyId"),data);
                    break;
                case MSG_STORY_BE_PRAISE:
                    data.put("storyId",params.getString("storyId"));
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_STORY_BE_PRAISE + params.getString("storyId"),data);
                    break;
                case MSG_DYNAMIC_BE_FORWARD:
                    data.put("storyId",params.getString("storyId"));
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_DYNAMIC_BE_FORWARD + params.getString("storyId"),data);
                    break;
                case MSG_CHARACTER_BE_FOLLOW:
                    data.put("characterId",params.getString("characterId"));
                    data.put("time",time);
                    msgJson.put(MSG_CHARACTER_BE_FOLLOW,data);
                    break;
                default:
                    break;
            }
            if(data.isEmpty()){
                return;
            }
            CommonCacheHelper.getInstance().setCache(userId,CACHE_USER_MSG,msgJson.toJSONString());
            CommonCacheHelper.getInstance().setCache(userId,CACHE_USER_MSG_READ_STATUS,"false");
             SCBaseEvent baseEvent = new SCBaseEvent(EventConstant.EVENT_MODULE_ARKSPOT,EventConstant.EVENT_KEY_NEW_MSG,null,null);
             EventBus.getDefault().post(baseEvent);
        }


    }

}
