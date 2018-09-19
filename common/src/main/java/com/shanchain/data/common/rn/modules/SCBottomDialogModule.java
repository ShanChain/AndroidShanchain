package com.shanchain.data.common.rn.modules;

import android.text.TextUtils;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.ui.widgets.SCBottomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoujian on 2017/10/25.
 */

public class SCBottomDialogModule extends ReactContextBaseJavaModule {

    private static final String REACT_CLASS = "SCBottomAlertDialog";
    private SCBottomDialog mDialog;

    public SCBottomDialogModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }



    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void show(final ReadableMap options, final Callback successCallback,final Callback failCallback) {

        ReadableMapKeySetIterator iterator = options.keySetIterator();
        List<String> items = new ArrayList<>();
        List<String> temps = new ArrayList<>();
        items.add("");
        items.add("");
        items.add("");
        items.add("");
        while (iterator.hasNextKey()){
            String key = iterator.nextKey();
            String value = options.getString(key);
            Integer num = Integer.valueOf(key);
            items.set(num,value);
        }

        for (int i = 0; i < items.size(); i ++) {
            if (!TextUtils.isEmpty(items.get(i))){
                temps.add(items.get(i));
            }
        }

        mDialog = new SCBottomDialog(ActivityStackManager.getInstance().getTopActivity());
        mDialog.setItems(temps);

        mDialog.setCallback(new SCBottomDialog.BottomCallBack() {
            @Override
            public void btnClick(String btnValue) {
                String key = getKeyByValue(options, btnValue);
                successCallback.invoke(key);
            }
        });

        mDialog.show();

    }

    @ReactMethod
    public void dismiss(){
        if (mDialog != null){
            mDialog.dismiss();
        }
    }

    //根据Value取Key
    private static String getKeyByValue(ReadableMap options, String value) {
        String keys="";
        ReadableMapKeySetIterator iterator = options.keySetIterator();
        while (iterator.hasNextKey()){
            String key = iterator.nextKey();
            String v = options.getString(key);
            if (v.equals(value)){
                keys = key;
            }
        }
        return keys;
    }

}
