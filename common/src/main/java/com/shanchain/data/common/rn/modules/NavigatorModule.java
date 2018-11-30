package com.shanchain.data.common.rn.modules;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.NativePages;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.rn.SCReactActivity;

/**
 * Created by flyye on 2017/9/20.
 */

public class NavigatorModule extends ReactContextBaseJavaModule {
    private final static String REACT_CLASS = "SCPageNavigator";
    public final static String REACT_EXTRA = "ReactExtra";
    public final static String REACT_SCREEN = "ReactScreen";
    public final static String REACT_INITIAL_PROPS = "activityParams";
    public static final String REACT_PROPS = "screenProps";
//    public static final String SETTING_SCREEN = "SETTING_SCREEN";
    public NavigatorModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    public static void startReactPage(Context context, String screenName) {
        startReactPage(context, screenName, null);
    }

    public static void startReactPage(Context context, String screenName, Bundle bundle) {

        Intent intent = new Intent(context, SCReactActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(REACT_SCREEN, screenName);
        intent.putExtra(REACT_INITIAL_PROPS, bundle);
        context.startActivity(intent);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void startActivity(String pageFlag, String extras) {
        Log.i("startAC",extras);
        Context context = getReactApplicationContext();
        Intent intent = NativePages.buildIntent(pageFlag);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (TextUtils.isEmpty(extras)) {
            context.startActivity(intent);
            return;
        }
        intent.putExtra(REACT_EXTRA, extras);
        context.startActivity(intent);
    }



    @ReactMethod
    public void startWebActivity(String title, String pageName, String params/*JSONObject.toString*/) {
        Intent intent = NativePages.buildIntent(NativePages.PAGE_WEBVIEW);
        Context context = getReactApplicationContext();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pageName", pageName);
        intent.putExtra("params", params);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }



    @ReactMethod
    public void startReactPage(String screen, String initialProps) {
        Context context = getReactApplicationContext();
        try{
            JSONObject jsonObject = JSON.parseObject(initialProps);
            Bundle bundle = new Bundle();
            bundle.putString(REACT_PROPS, jsonObject.toString());
            startReactPage(context, screen, bundle);
        }catch (Exception e){

        }

    }

    @ReactMethod
    public void startWebActivityByUrl(String title, String url, String params/*JSONObject.toString*/) {
        Intent intent = NativePages.buildIntent(NativePages.PAGE_WEBVIEW);
        Context context = getReactApplicationContext();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("params", params);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @ReactMethod
    public void logout(){
        AppManager.getInstance().logout();
    }

    @ReactMethod
    public void login(){
        Intent intent = NativePages.buildIntent(NativePages.PAGE_LOGIN);
        ActivityStackManager.getInstance().finishAllActivity();
        getReactApplicationContext().startActivity(intent);
    }

    @ReactMethod
    public boolean isLogin(){
        String userId = SCCacheUtils.getCache("0", "curUser");
        if (TextUtils.isEmpty(userId)) {
            return false;
        }

        String token = SCCacheUtils.getCache(userId, "token");

        if (TextUtils.isEmpty(token)){
            return false;
        }

        return true;

    }
}
