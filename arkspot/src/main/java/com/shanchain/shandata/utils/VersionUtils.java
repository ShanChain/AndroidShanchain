package com.shanchain.shandata.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.shanchain.data.common.utils.encryption.Base64Utils;

import java.nio.ByteBuffer;
import java.util.UUID;

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


    public static String getApiVersion(){
        int sdkInt = Build.VERSION.SDK_INT;
        String version = "android ";
        switch (sdkInt) {

            case 8:
                version = version + "2.2";
                break;
            case 9:
                version = version + "2.3";
                break;
            case 10:
                version = version + "2.3.3";
                break;
            case 11:
                version = version + "3.0";
                break;
            case 12:
                version = version + "3.1";
                break;
            case 13:
                version = version + "3.2";
                break;
            case 14:
                version = version + "4.0";
                break;
            case 15:
                version = version + "4.0.4";
                break;
            case 16:
                version = version + "4.1";
                break;
            case 17:
                version = version + "4.2";
                break;
            case 18:
                version = version + "4.3";
                break;
            case 19:
                version = version + "4.4";
                break;
            case 20:
                version = version + "4.4w";
                break;
            case 21:
                version = version + "5.0";
                break;
            case 22:
                version = version + "5.1";
                break;
            case 23:
                version = version + "6.0";
                break;
            case 24:
                version = version + "7.0";
                break;
            case 25:
                version = version + "7.1";
                break;
        }
        return version;
    }

    public String getRequestId() {

        UUID uid = UUID.randomUUID();

        long hBits = uid.getLeastSignificantBits();

        long lBits = uid.getMostSignificantBits();

        byte[] bytes = ByteBuffer.allocate(16).putLong(hBits).putLong(lBits).array();
        return Base64Utils.encode(bytes);
    }

}
