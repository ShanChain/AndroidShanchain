package com.shanchain.shandata.utils;

/**
 * Created by zhoujian on 2017/9/6.
 */

public class StringUtils {

    private StringUtils() {
    }

    /**
     * 描述：将字体染色成蓝色
     */
    public static String getBlueColorStr(String str) {
        return "<font color='blue'>" + str + "</font>";
    }

    /**
     * 描述：将字体染色成主题色
     */
    public static String getThemeColorStrAt(String str) {
        return "<font color=#3bbac8 >" + str + "</font>";
    }



}
