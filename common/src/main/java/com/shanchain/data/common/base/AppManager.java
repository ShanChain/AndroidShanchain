package com.shanchain.data.common.base;

import android.app.Application;
import android.content.Context;

import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by flyye on 2017/10/19.
 */

public class AppManager {

    private static AppManager instance;
    private Context mContext;
    private Application application;

    public synchronized static AppManager getInstance() {
        if (null == instance) {
            instance = new AppManager();
        }
        return instance;
    }

    public void init(Application application) {
        this.application = application;
        this.mContext = application.getApplicationContext();
    }

    private AppManager() {}

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setContext(Context context) {
        this.mContext = context.getApplicationContext();
    }


    public Context getContext() {
        return mContext;
    }

    public String getOsVersion(){
        return android.os.Build.VERSION.RELEASE;
    }

    public void logout(){
        SCBaseEvent baseEvent = new SCBaseEvent(EventConstant.EVENT_MODULE_ARKSPOT,EventConstant.EVENT_KEY_LOGOUT,null,null);
        EventBus.getDefault().post(baseEvent);
    }

//    public void clearCache(){
//        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
//        Iterator<String> iterator = conversations.keySet().iterator();
//
//        while (iterator.hasNext()){
//            String user = iterator.next();
//            LogUtils.i("删除的会话记录 = " + user);
//            EMClient.getInstance().chatManager().deleteConversation(user,true);
//        }
//
//    }

}
