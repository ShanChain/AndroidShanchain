package com.shanchain.shandata.manager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhoujian on 2017/11/23.
 */

public class TokenManager {

    private static TokenManager instance;

    public synchronized static TokenManager getInstance() {
        if (null == instance) {
            instance = new TokenManager();
        }
        EventBus.getDefault().register(instance);
        return instance;
    }



}
