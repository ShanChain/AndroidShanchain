package com.shanchain.data.common.rn.modules;

import android.content.Context;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.CommonLoginManager;
import com.shanchain.data.common.base.RoleManager;

/**
 * Created by flyye on 2017/11/7.
 */

public class AppManagerModule extends ReactContextBaseJavaModule {
    private Context mContext;
    private final static String REACT_CLASS = "AppManagerModule";

    public AppManagerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = getReactApplicationContext();
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }


    @ReactMethod
    public void switchRole(final ReadableMap options) {
        ActivityStackManager.getInstance().getTopActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String modelId = options.getString("modelId");
                String spaceId = options.getString("spaceId");
                RoleManager.switchRole(modelId,spaceId,null);
            }
        });

    }

    @ReactMethod
    public void bindOtherAccount(String userType){
        CommonLoginManager.bindOtherAccount(userType);
    }

    @ReactMethod
    public void clearCache(){
        AppManager.getInstance().clearCache();
    }

}
