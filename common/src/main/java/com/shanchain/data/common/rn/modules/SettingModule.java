package com.shanchain.data.common.rn.modules;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.shanchain.data.common.cache.SCCacheUtils;

import static com.shanchain.data.common.rn.modules.NavigatorModule.REACT_PROPS;

public class SettingModule extends ReactContextBaseJavaModule {


    public SettingModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "SettingModule";
    }

    @ReactMethod
    public void dataToJS(Callback successBack, Callback errorBack) {
        try {
            Activity currentActivity = getCurrentActivity();
            String gDataString = SCCacheUtils.getCacheGData();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gData", JSONObject.parse(gDataString));
            Bundle bundle = new Bundle();
            bundle.putString(REACT_PROPS, jsonObject.toString());
            bundle.putString("page", "SettingScreen");
//            setArguments(bundle);
            String result = currentActivity.getIntent().getStringExtra("gData");
//            if (TextUtils.isEmpty(result)) {
//                result = "没有数据";
//            }
            successBack.invoke(result);
        } catch (Exception e) {
            errorBack.invoke(e.getMessage());
        }
    }
}
