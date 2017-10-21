package com.shanchain.data.common.rn.modules;

import android.content.Context;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.shanchain.data.common.ui.widgets.SCInputDialog;

/**
 * Created by zhoujian on 2017/10/21.
 */

public class SCInputDialogModule extends ReactContextBaseJavaModule {

    private final static String REACT_CLASS = "SCInputAlertDialog";

    private Context mContext;

    public SCInputDialogModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void show(ReadableMap options, final Callback sureCallback, final Callback cancelCallback) {
        String placeHolder = options.getString("placeHolder");
        String title = options.getString("title");
        //int maxLength = options.getInt("maxLength");
        final SCInputDialog scInputDialog = new SCInputDialog(mContext, title, placeHolder);

        final com.shanchain.data.common.base.Callback sureCallBac = new com.shanchain.data.common.base.Callback() {
            @Override
            public void invoke() {
                String inputContent = scInputDialog.getInputContent();
                WritableMap input = new WritableNativeMap();
                input.putString("input",inputContent);
                sureCallback.invoke(input);
            }
        };

        com.shanchain.data.common.base.Callback cancelCallBac = new com.shanchain.data.common.base.Callback() {
            @Override
            public void invoke() {
                cancelCallback.invoke();
            }
        };

        scInputDialog.setCallback(sureCallBac, cancelCallBac);

        scInputDialog.show();

    }

}
