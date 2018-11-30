package com.shanchain.data.common.rn;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.shanchain.common.R;

public class SettingActivity extends ReactActivity {

    public SettingActivity(ReactApplicationContext reactContext) {
    }

    @Override
    protected String getMainComponentName() {
        return "SettingScreen";
    }

    @ReactMethod
    public void dataToJS(Callback successBack, Callback errorBack){
        try{
            String result = getIntent().getStringExtra("gData");
            if (TextUtils.isEmpty(result)){
                result = "没有数据";
            }
            successBack.invoke(result);
        }catch (Exception e){
            errorBack.invoke(e.getMessage());
        }
    }

}
