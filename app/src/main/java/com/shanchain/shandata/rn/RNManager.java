package com.shanchain.shandata.rn;

import android.app.Application;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.shanchain.shandata.BuildConfig;

/**
 * Created by flyye on 2017/8/16.
 */

public class RNManager {
    private static RNManager mInstance;
    private ReactInstanceManager mReactInstanceManager;
    public static synchronized RNManager getInstance() {
        if (mInstance == null) {
            mInstance = new RNManager();
        }
        return mInstance;
    }
    public ReactInstanceManager getReactInstanceManager() {
        return mReactInstanceManager;
    }
    public void init(final Application application){
        if (mReactInstanceManager != null) {
            return;
        }
        ReactInstanceManagerBuilder builder = ReactInstanceManager.builder().setApplication(application)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .addPackage(new MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.RN_DEBUG)
                .setInitialLifecycleState(LifecycleState.BEFORE_RESUME);
        mReactInstanceManager = builder.build();

    }
}
