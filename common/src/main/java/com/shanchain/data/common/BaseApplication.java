package com.shanchain.data.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.base.CommonConstants;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.base.NativePages;
import com.shanchain.data.common.cache.BaseSqlDao;

import java.util.Arrays;
import java.util.List;

/**
 * Created by flyye on 2017/9/20.
 */

public class BaseApplication extends Application implements ReactApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AppManager.getInstance().setContext(getApplicationContext());
        NativePages.initAllowJumpPages(this);
        BaseSqlDao.getInstance().init(getApplicationContext(), CommonConstants.APP_CHACHE_DB,1);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return Constants.SC_RN_DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage()
//                    new SettingReactPackage()
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
