package com.shanchain.arkspot.utils;

/**
 * Created by zhoujian on 2017/10/9.
 */

public class SCImgUrlUtils {

    public static String getImgUrl(String host,String img,int h,int w){
        return host + img + ".jpg?x-oss-process=image/resize,m_fixed,h_" + h + ",w_" + w;
    }



}
