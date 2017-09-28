package com.shanchain.data.common.rn.modules;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.shanchain.data.common.base.NativePages;
import com.shanchain.data.common.rn.SCReactActivity;
import com.shanchain.data.common.rn.utils.ReactArguments;

import org.json.JSONObject;

/**
 * Created by flyye on 2017/9/20.
 */

public class NavigatorModule extends ReactContextBaseJavaModule {
    private final static String REACT_CLASS = "SCPageNavigator";
    public final static String REACT_EXTRA = "ReactExtra";
    public final static String REACT_SCREEN = "ReactScreen";
    public final static String REACT_INIT_PROPS = "ReactInitProps";

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
        intent.putExtra(REACT_INIT_PROPS, bundle);
        context.startActivity(intent);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void startActivity(String className, String extras) {
        Context context = getReactApplicationContext();
        Intent intent = NativePages.buildIntent(className);
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
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(initialProps);
        } catch (Exception e) {
            jsonObject = null;
        }
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        Bundle bundle = new Bundle();
        bundle.putString(REACT_INIT_PROPS, jsonObject.toString());
        startReactPage(context, screen, bundle);
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
}
