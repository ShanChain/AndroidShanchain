package com.shanchain.data.common;

import android.app.Application;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.shanchain.data.common.rn.AppReactPackage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by flyye on 2017/9/20.
 */

public class BaseApplication extends Application implements ReactApplication {


    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
//            return BuildConfig.DEBUG;
            return true;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new AppReactPackage()
            );
        }
    };



    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    public ReactInstanceManager getReactInstanceManager(){
        return mReactNativeHost.getReactInstanceManager();
    }
}
