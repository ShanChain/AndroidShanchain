package com.shanchain.shandata.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.RNPagesConstant;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.rn.modules.NavigatorModule;
import com.shanchain.shandata.ui.model.RNDetailExt;
import com.shanchain.shandata.ui.model.RNGDataBean;
import com.shanchain.shandata.ui.view.activity.MainActivity;
import com.shanchain.shandata.ui.view.activity.chat.ContactActivity;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.umeng.message.UTrack;
import com.umeng.message.entity.UMessage;

/**
 * Created by flyye on 2017/11/17.
 */

public class PushManager {

    public static void dealWithCustomClickAction(Context context, UMessage message){
//        Toast.makeText(context, message.custom, Toast.LENGTH_LONG).show();
        if(TextUtils.isEmpty(message.custom)){
            return;
        }

        JSONObject jsonObject = JSONObject.parseObject(message.custom);
        String msgKey = jsonObject.getString("msg_key");
        JSONObject msgBody = jsonObject.getJSONObject("msg_body");

        if(msgBody.getString("action_type").equalsIgnoreCase("open_page")){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            switch (msgBody.getString("page_name")){
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
                    intent.putExtra("characterId",msgBody.getJSONObject("params").getString("characterId"));
                    break;
                case PushConstants.PAGE_CONTACT:
                    intent.setClass(context,ContactActivity.class);
                    break;
                case PushConstants.PAGE_NOTIFICATION:
                    NavigatorModule.startReactPage(context, RNPagesConstant.NotificationScreen,new Bundle());
                    return;
                case PushConstants.PAGE_RULE:
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
                    detailExt.setModelId(msgBody.getJSONObject("params").getString("modelId") + "");
                    String json =JSONObject.toJSONString(detailExt);
                    bundle.putString(NavigatorModule.REACT_PROPS, json);
                    NavigatorModule.startReactPage(context, RNPagesConstant.RoleDetailScreen,bundle);
                    return;
                case PushConstants.PAGE_SETTING:
                    NavigatorModule.startReactPage(context, RNPagesConstant.SettingScreen,new Bundle());                    intent.putExtra("fragmentId",1);
                    return;
                default:
                    return;

            }
            context.startActivity(intent);
        }else if (msgBody.getString("action_type").equalsIgnoreCase("red_point")){

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
//        Toast.makeText(context, message.custom, Toast.LENGTH_LONG).show();

    }

}
