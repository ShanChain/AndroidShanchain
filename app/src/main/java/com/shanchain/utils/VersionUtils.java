package com.shanchain.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by zhoujian on 2017/5/16.
 * 获取app版本信息工具类
 */

public class VersionUtils {

    /**
     * 私有构造
     */
    private VersionUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 得到软件版本号
     *
     * @param context 上下文
     * @return 当前版本Code
     */
    public static int getVersionCode(Context context) {
        int verCode = -1;
        try {
            String packageName = context.getPackageName();
            verCode = context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            String packageName = context.getPackageName();
            verName = context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 版本比较
     *
     * @param nowVersion    app版本
     * @param serverVersion 服务器版本
     * @return
     */
    public static boolean compareVersion(String nowVersion, String serverVersion) {

        if (nowVersion != null && serverVersion != null) {
            String[] nowVersions = nowVersion.split("\\.");
            String[] serverVersions = serverVersion.split("\\.");
            if (nowVersions != null && serverVersions != null && nowVersions.length > 1 && serverVersions.length > 1) {
                int nowVersionsFirst = Integer.parseInt(nowVersions[0]);
                int serverVersionFirst = Integer.parseInt(serverVersions[0]);
                int nowVersionsSecond = Integer.parseInt(nowVersions[1]);
                int serverVersionSecond = Integer.parseInt(serverVersions[1]);
                int nowVersionsThird = Integer.parseInt(nowVersions[2]);
                int serverVersionThird = Integer.parseInt(serverVersions[2]);
                if (nowVersionsFirst < serverVersionFirst) {
                    return true;
                } else if (nowVersionsFirst == serverVersionFirst && nowVersionsSecond < serverVersionSecond) {
                    return true;
                } else if (nowVersionsFirst == serverVersionFirst && nowVersionsSecond == serverVersionSecond && nowVersionsThird < serverVersionThird) {
                    return true;
                }
            }
        }
        return false;
    }

}
