package com.shanchain.shandata.utils;

import android.os.Environment;

/**
 * Created by zhoujian on 2017/12/20.
 */

public class FilePathUtils {

    public static String getInnerCachePath(){
        return Environment.getRootDirectory().getAbsolutePath();
    }


}
