package com.shanchain.data.common.rn.modules;

import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.shanchain.data.common.cache.CommonCacheHelper;

/**
 * Created by flyye on 17/10/11.
 */
public class RNCommonCacheHelper extends ReactContextBaseJavaModule {

    private final static String REACT_CLASS = "CommonCacheHelper";
    private CommonCacheHelper mCommonCacheHelper;

    public RNCommonCacheHelper(ReactApplicationContext reactContext) {
        super(reactContext);
        mCommonCacheHelper = mCommonCacheHelper.getInstance();

    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void setCache(String userId,String key,String value){
        Log.i("RN_save","userId="+userId+","+"key="+","+key+"value="+value);
        if (mCommonCacheHelper != null){
            mCommonCacheHelper.setCache(userId,key,value);
        }
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public String getCache(String userId,String key) {
       return mCommonCacheHelper.getCache(userId, key);
    }
    @ReactMethod(isBlockingSynchronousMethod = true)
    public String getCacheAndTime(String userId,String key) {
        return mCommonCacheHelper.getCacheAndTime(userId, key);
    }

    @ReactMethod
    public void getCacheAsync(String userId,String key,Promise promise) {
        promise.resolve(mCommonCacheHelper.getCache(userId, key));
    }


}
