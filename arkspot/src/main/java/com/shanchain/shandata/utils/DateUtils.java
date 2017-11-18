package com.shanchain.shandata.utils;

import java.util.Date;

/**
 * Created by zhoujian on 2017/10/16.
 */

public class DateUtils {

    private final static long MINUTE = 60 * 1000;// 1分钟
    private final static long HOUR = 60 * MINUTE;// 1小时
    private final static long DAY = 24 * HOUR;// 1天
    private final static long MONTH = 31 * DAY;// 月
    private final static long YEAR = 12 * MONTH;// 年


    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > YEAR) {
            r = (diff / YEAR);
            return r + "年前";
        }
        if (diff > MONTH) {
            r = (diff / MONTH);
            return r + "个月前";
        }
        if (diff > DAY) {
            r = (diff / DAY);
            return r + "天前";
        }
        if (diff > HOUR) {
            r = (diff / HOUR);
            return r + "个小时前";
        }
        if (diff > MINUTE) {
            r = (diff / MINUTE);
            return r + "分钟前";
        }
        return "刚刚";
    }


    public static String getDramaStartTime(long diffTime){
        long minTime = diffTime / 60;   //分
        long hourTime = minTime / 60;   //小时
        long dayTime = hourTime / 24;   //天
        if (dayTime>0){
            return dayTime + "天"+ (hourTime - dayTime*24) + "小时" + (minTime - hourTime * 60) +"分钟";
        }

        if (hourTime > 0){
            return hourTime + "小时" + (minTime - hourTime * 60) +"分钟";
        }

        if (minTime > 0){
            return minTime +"分钟";
        }

        return "";
    }

}
