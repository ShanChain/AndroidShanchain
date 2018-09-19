package com.shanchain.data.common.rn.modules;


import android.app.Dialog;
import android.text.TextUtils;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.ui.widgets.StandardLoading;


public class SCDialogModule extends ReactContextBaseJavaModule {

    private final static String REACT_CLASS = "SCAlertDialog";
    private Dialog mDialog;


    public SCDialogModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }


    @Override
    public String getName() {
        return REACT_CLASS;
    }


    @ReactMethod
    public void show(ReadableMap options, final Callback sureCallback,final Callback cancelCallback) {
        if(options.getString("type").equalsIgnoreCase("1")){
            mDialog = new StandardDialog(ActivityStackManager.getInstance().getTopActivity());
            ((StandardDialog)mDialog).setStandardMsg(options.getString("msg"));
            ((StandardDialog)mDialog).setStandardTitle(options.getString("title"));
            if(options.hasKey("sureText")){
                ((StandardDialog)mDialog).setSureText(options.getString("sureText"));
            }
            if(options.hasKey("cancelText")){
                ((StandardDialog)mDialog).setCancelText(options.getString("cancelText"));
            }
            com.shanchain.data.common.base.Callback sureCallbak = new com.shanchain.data.common.base.Callback() {
                @Override
                public void invoke() {
                    if(sureCallback !=null){
                        sureCallback.invoke();
                    }
                }
            };
            com.shanchain.data.common.base.Callback cancelCallbak = new com.shanchain.data.common.base.Callback() {
                @Override
                public void invoke() {
                    if(cancelCallback !=null){
                        cancelCallback.invoke();
                    }
                }
            };
            ((StandardDialog)mDialog).setCallback(sureCallbak,cancelCallbak);

            mDialog.show();
        }else if(options.getString("type").equalsIgnoreCase("2")){
            mDialog = new StandardLoading(ActivityStackManager.getInstance().getTopActivity());
            mDialog.show();
        }
    }

    @ReactMethod
    public void dismiss() {
        if(mDialog != null){
            mDialog.dismiss();
        }
    }


}
