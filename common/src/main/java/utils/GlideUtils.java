package utils;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
    public static void load(Context context,String url,ImageView iv,int placeHolderResId){
        Glide.with(context).load(url).placeholder(placeHolderResId).into(iv);
    }

    /**
     *  描述：加载圆形图片
     */
    public static void loadCircle(Context context,String url,ImageView iv){
        Glide.with(context).load(url).transform(new GlideCircleTransform(context)).into(iv);
    }

}
