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
    public static final String PAGE_WEBVIEW = "page_webview"; //SCWebviewActivity
    public static final String PAGE_CONTACT = "page_contact";//ContactActivity
    public static final String PAGE_PHOTO_PICKER = "page_photo_picker";//ContactActivity
    public static final String PAGE_PHOTO_PAGER = "page_photo_pager";//ContactActivity
    public static final String PAGE_PHOTO_IMAGE = "page_photo_image";//ContactActivity
    public static final String PAGE_NOTICE = "page_notice";//AnnouncementDetailsActivity
    public static final String PAGE_STORY_DYNAMIC = "page_story_dynamic";//DynamicDetailsActivity
    public static final String PAGE_NOVEL_DYNAMIC = "page_novel_dynamic";//NovelDetailsActivity
    public static final String PAGE_PRAISED = "page_praised";//PraisedActivity
    public static final String PAGE_MY_COMMENTS = "page_my_comments";//MyCommentsActivity
    public static final String PAGE_ADD_ROLE = "page_add_role";//MyCommentsActivity
    public static final String PAGE_TOPIC_DETAILS = "page_topic_details";
    public static final String PAGE_ADD_TOPIC = "page_add_topic";
    public static final String PAGE_LOGIN = "page_login";
    public static final String BIND_INFO = "bind_info";
    public static final String PAGE_CHARACTER_HOME = "page_character_home";

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
