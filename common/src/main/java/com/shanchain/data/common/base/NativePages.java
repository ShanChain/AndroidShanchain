package com.shanchain.data.common.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.HashMap;

/**
 * Created by flyye on 2017/9/20.
 */

public final class NativePages {

    public static final String PAGE_FLAG = "page_flag";
    public static final String PAGE_MAIN = "page_main"; //MainActivity
    public static final String PAGE_WEBVIEW = "page_webview"; //MainActivity
    public static final String PAGE_CONTACT = "page_contact";//ContactActivity
    public static final String PAGE_PRAISED = "page_praised";//PraisedActivity
    public static final String page_my_comments = "page_my_comments";//MyCommentsActivity
    private static HashMap<String, ActivityInfo> actMaps = new HashMap<>();


    public static Intent buildIntent(final String activityFlag) {
        Intent intent = new Intent();
        ActivityInfo info = actMaps.get(activityFlag);
        if (info != null) {
            intent.setComponent(new ComponentName(info.packageName, info.name));
        } else {
            throw new IllegalArgumentException("No Flag : " + activityFlag);
        }
        return intent;
    }


    public static Intent buildMainScreenIntent() {
        Intent result = buildIntent(NativePages.PAGE_MAIN);
        return result;
    }

    public static final void initAllowJumpPages(Application app) {
        try {
            PackageInfo packageInfo = app.getPackageManager().getPackageInfo(app.getPackageName(), PackageManager.GET_ACTIVITIES);
            ActivityInfo activities[] = packageInfo.activities;
            for (ActivityInfo actInfo : activities) {
                ActivityInfo info = app.getPackageManager().getActivityInfo(new ComponentName(actInfo.packageName, actInfo.name), PackageManager.GET_META_DATA);
                if (info.metaData != null && info.metaData.containsKey(NativePages.PAGE_FLAG)) {
                    String flag = info.metaData.getString(NativePages.PAGE_FLAG);
                    actMaps.put(flag, info);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
