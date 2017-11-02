package com.shanchain.data.common.rn.modules;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.modules.core.PermissionListener;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.rn.SCReactActivity;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhoujian on 2017/11/2.
 */

public class PhotoPickerModule extends ReactContextBaseJavaModule {
    private static final String REACT_CLASS = "PhotoPicker";
    private static final int REQUEST_CODE = 100;

    public PhotoPickerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void selectPhoto(final Callback callback) {
        final Activity topActivity = ActivityStackManager.getInstance().getTopActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (topActivity instanceof SCReactActivity) {
                int checkSelfPermission = ContextCompat.checkSelfPermission(topActivity, Manifest.permission.CAMERA);
                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                    LogUtils.d("未申请权限,正在申请");
                    ((SCReactActivity) topActivity).requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE, new PermissionListener() {
                        @Override
                        public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                            if (requestCode == REQUEST_CODE) {
                                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                                    pickImages(topActivity,callback);
                                } else {
                                    //未授权
                                    ToastUtils.showToast(topActivity,"未给予权限，不能打开相机");
                                }
                            }
                            return true;
                        }
                    });
                }
            } else {
                LogUtils.d("已经申请过权限");
                pickImages(topActivity, callback);
            }
        } else {
            LogUtils.d("版本低于6.0");
            pickImages(topActivity, callback);
        }
    }

    private void pickImages(Activity topActivity, final Callback callback) {
        PhotoPicker.builder()
                .setPhotoCount(5)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(topActivity, PhotoPicker.REQUEST_CODE);
        if (topActivity instanceof SCReactActivity) {
            ((SCReactActivity) topActivity).setResultListener(new SCReactActivity.ResultListener() {
                @Override
                public void onResult(int requestCode, int resultCode, Intent data) {
                    if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK){
                        if (data != null){
                            ArrayList<String> photos =
                                    data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                            WritableArray array = new WritableNativeArray();
                            for (int i = 0; i < photos.size(); i ++) {
                                array.pushString(photos.get(i));
                            }
                            callback.invoke(array);
                        }
                    }
                }
            });
        }
    }

}
