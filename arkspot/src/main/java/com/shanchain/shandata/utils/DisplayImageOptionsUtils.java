package com.shanchain.shandata.utils;


import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shanchain.shandata.R;

/**
 * 加载图片时候的options
 * Created by yw on 2014/10/13.
 */
public class DisplayImageOptionsUtils {


    public static DisplayImageOptions buildDefaultAvatarOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.place_image_commen)
                .showImageForEmptyUri(R.mipmap.place_image_commen)
                .showImageOnFail(R.mipmap.place_image_commen)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .cacheOnDisk(true).build();
        return options;

    }
}
