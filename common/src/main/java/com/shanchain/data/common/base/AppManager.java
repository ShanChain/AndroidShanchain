package com.shanchain.data.common.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.lang.ref.SoftReference;

/**
 * Created by flyye on 2017/10/19.
 */

public class AppManager {

    private static AppManager instance;
    private Context mContext;
    private Application application;

    public synchronized static AppManager getInstance() {
        if (null == instance) {
            instance = new AppManager();
        }
        return instance;
    }

    public void init(Application application) {
        this.application = application;
        this.mContext = application.getApplicationContext();
    }

    private AppManager() {}

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setContext(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public Context getContext() {
        return mContext;
    }

    public String getOsVersion(){
        return android.os.Build.VERSION.RELEASE;
    }

}
