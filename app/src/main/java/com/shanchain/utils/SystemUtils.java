package com.shanchain.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;

/**
 * Created by zhoujian on 2017/5/16.
 * 系统信息工具类
 */

public class SystemUtils {

    /**
     * 获取系统状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取系统唯一标识
     * @param context
     * @return  设备id
     */
    public static String getSystemDeviceId(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }

}
