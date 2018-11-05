package com.shanchain.data.common.utils;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.common.R;

public class GlideUtils {
    /**
     *  描述：普通图片加载
     */
    public static void load(Context context, String url, ImageView iv){
        Glide.with(context).load(url).into(iv);
    }

    /**
     *  描述：带默认图片的图片加载
     */
    public static void load(Context context, String url, final ImageView iv, int placeHolderResId){
        if (placeHolderResId == 0){
            placeHolderResId = R.drawable.shape_pic_default;
        }
        RequestOptions options = new RequestOptions();
        options.dontAnimate().placeholder(placeHolderResId);
        Glide.with(context).load(url).apply(options).into(iv);
    }


    /**
     *  描述：加载圆形图片
     */
    public static void loadCircle(Context context,String url,ImageView iv){
        RequestOptions options = new RequestOptions();
        options.transform(new GlideCircleTransform(context));
        Glide.with(context).load(url).apply(options).into(iv);
    }

}
