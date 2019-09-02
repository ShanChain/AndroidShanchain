package com.shanchain.shandata.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Picasso加载配置类
 * Created by Humg on 2016/8/2 0002.
 */
class PhotoSelectPicassoImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private Bitmap.Config mConfig;

    public PhotoSelectPicassoImageLoader() {
        this(Bitmap.Config.RGB_565);
    }

    public PhotoSelectPicassoImageLoader(Bitmap.Config config) {
        this.mConfig = config;
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
//        Picasso.with(activity)
//                .load(new File(path))
//                .placeholder(defaultDrawable)
//                .error(defaultDrawable)
//                .config(mConfig)
//                .resize(width, height)
//                .centerInside()
////                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                .into(imageView);

//        GalleryFinal.getCoreConfig().getImageLoader().displayImage(activity, path, imageView, defaultDrawable, width, height);
//        ImageLoader.getInstance().displayImage("file://"+);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultDrawable)
                .showImageForEmptyUri(defaultDrawable)
                .showImageOnFail(defaultDrawable)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .cacheOnDisk(false).build();
        ImageSize imageSize = new ImageSize(width, height);
        ImageLoader.getInstance().displayImage("file://" + path, new ImageViewAware(imageView), options, imageSize, null, null);
    }

    @Override
    public void clearMemoryCache() {
    }
}
