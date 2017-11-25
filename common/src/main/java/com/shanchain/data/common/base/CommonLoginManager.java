package com.shanchain.data.common.base;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by flyye on 2017/11/25.
 */

public class CommonLoginManager {

     public static void bindOtherAccount(String userType){
         JSONObject jsonObject = new JSONObject();
         if(userType.equalsIgnoreCase(UserType.USER_TYPE_WEIBO)){
             jsonObject.put("userType",UserType.USER_TYPE_WEIBO);
         }else if(userType.equalsIgnoreCase(UserType.USER_TYPE_WEIXIN)){
             jsonObject.put("userType",UserType.USER_TYPE_WEIXIN);
         }else if(userType.equalsIgnoreCase(UserType.USER_TYPE_QQ)){
             jsonObject.put("userType",UserType.USER_TYPE_QQ);
         }
         SCBaseEvent baseEvent = new SCBaseEvent(EventConstant.EVENT_MODULE_ARKSPOT,EventConstant.EVENT_KEY_BIND_OTHER_ACCOUNT,jsonObject,null);
         EventBus.getDefault().post(baseEvent);
    }
}
