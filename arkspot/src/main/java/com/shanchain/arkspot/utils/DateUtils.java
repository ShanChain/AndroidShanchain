package com.shanchain.arkspot.utils;

/**
 * Created by zhoujian on 2017/10/16.
 */

public class DateUtils {

    public static String getStandardDate(String timeStr) {
        String temp = "";
        try {
            long now = System.currentTimeMillis() / 1000;
            long publish = Long.parseLong(timeStr);
            long diff = now - publish;
            long months = diff / (60 * 60 * 24*30);
            long days = diff / (60 * 60 * 24);
            long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
            long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / 60;
            if (months > 0) {
                temp = months + "月前";
            } else if (days > 0) {
                temp = days + "天前";
            } else if (hours > 0) {
                temp = hours + "小时前";
            } else {
                temp = minutes + "分钟前";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

}
