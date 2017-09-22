package com.shanchain.data.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @des 屏幕像素密度工具类
 */

public class DensityUtils {
    /**
     *  2017/5/16
     *  描述：私有构造
     */
    private DensityUtils() {
    }

    /**
     * 描述: 密度转换 DP转PX
     * @param dpValue DP值
     * @return PX值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 描述: 密度转换 PX转DP
     * @param context
     * @param pxValue PX值
     * @return DP值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 描述: 获取手机当前密度值
     * @param context
     * @return 当前手机屏幕密度值
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 描述: 获得屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE );
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( outMetrics);
        return outMetrics .widthPixels ;
    }
    /**
     * 描述: 获得屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE );
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( outMetrics);
        return outMetrics .heightPixels ;
    }

}
