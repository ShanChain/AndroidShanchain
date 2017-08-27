package utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

/**
 * 文字染色工具类
 */

public class SpanUtils {

    /**
     * SpannableStringBuilder 修改TextView中部分文字的颜色
     *
     * @param str     要处理的字符串
     * @param colorId 要染成的颜色
     * @param start   染色的字符串的起始位置
     * @param end     颜色的字符串的结束位置
     * @return
     */
    public static SpannableStringBuilder buildSpannableString(String str, int colorId, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(colorId); // Color.RED
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * @param str
     * @param colorId      为titiel染色
     * @param start
     * @param end
     * @param colorContent 用于为内容染色
     * @return
     */
    public static SpannableStringBuilder buildSpannableString(String str, int colorId, int start, int end, int colorContent) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        ForegroundColorSpan firstSpan = new ForegroundColorSpan(colorId); //
        ForegroundColorSpan SecondSpan = new ForegroundColorSpan(colorContent); //
        builder.setSpan(firstSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(SecondSpan, end, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder buildSpannableString(String str1, String num1, String str2, String num2, String str3, String num3, String str4) {
        String str = str1 + num1 + str2 + num2 + str3 + num3 + str4;
        SpannableStringBuilder builder = new SpannableStringBuilder(str);

        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(0xFF2B2B2B);
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(0xFFFF9933);
        ForegroundColorSpan graySpan = new ForegroundColorSpan(0xFFB3B3B3);

        builder.setSpan(blackSpan, str1.length(), str1.length() + num1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(yellowSpan, str1.length() + num1.length() + str2.length(), str1.length() + num1.length() + str2.length() + num2.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(graySpan, str1.length() + num1.length() + str2.length() + num2.length() + str3.length(), str1.length() + num1.length() + str2.length() + num2.length() + str3.length() + num3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }

}
