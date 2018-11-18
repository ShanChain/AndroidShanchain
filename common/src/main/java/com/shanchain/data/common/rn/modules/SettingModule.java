package com.shanchain.data.common.rn.modules;

import android.app.Activity;
import android.text.TextUtils;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

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
            String result = currentActivity.getIntent().getStringExtra("data");
            if (TextUtils.isEmpty(result)) {
                result = "没有数据";
            }
            successBack.invoke(result);
        } catch (Exception e) {
            errorBack.invoke(e.getMessage());
        }
    }
}
