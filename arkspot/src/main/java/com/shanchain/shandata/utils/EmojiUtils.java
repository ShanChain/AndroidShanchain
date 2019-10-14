package com.shanchain.shandata.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by WealChen
 * Date : 2019/10/12
 * Describe :
 */
public class EmojiUtils {
    /**
     * 字符串换成UTF-8
     *
     * @param str
     * @return
     */
    public static String stringToUtf8(String str) {
        String result = null;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * utf-8换成字符串
     *
     * @param str
     * @return
     */
    public static String utf8ToString(String str) {
        String result = null;
        try {
            result = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
