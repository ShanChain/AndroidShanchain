package com.shanchain.shandata.rn.module;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import com.shanchain.data.common.utils.ToastUtils;

@ReactModule(name = SCToastModule.NAME)
public class SCToastModule extends ReactContextBaseJavaModule {

    protected static final String NAME = "SCToast";

    public SCToastModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void showToast(String msg ) {
        ToastUtils.showToast(getReactApplicationContext(),msg);
    }
}
