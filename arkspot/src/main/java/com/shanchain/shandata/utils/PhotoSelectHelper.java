package com.shanchain.shandata.utils;

import android.content.Context;
import android.graphics.Color;

import com.shanchain.shandata.R;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * 图片选择器主题设置类
 * Created by Humg on 2016/8/2 0002.
 */
public class PhotoSelectHelper {
    private final Context mContext;
    private final int mCropSize;
    private boolean mCrop = false;
    private FunctionConfig functionConfig;

    public PhotoSelectHelper(Context context, boolean crop, int cropSize) {
        mContext = context;
        mCrop = crop;
        mCropSize = cropSize;
        init();
    }

    private void init() {
        //设置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .setCheckSelectedColor(Color.parseColor("#a260e6"))
                .setCheckNornalColor(mContext.getResources().getColor(android.R.color.transparent))
//                .setIconCheck(R.mipmap.ic_launcher)
                .setTitleBarBgColor(Color.parseColor("#a260e6"))
                .setFabNornalColor(Color.parseColor("#a260e6"))
                .setFabPressedColor(Color.parseColor("#a260e6"))
//                .setIconBack(com.wt.basecore.R.mipmap.icon_return)
                .build();
        //配置功能
        functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableRotate(false)
                .setEnableCrop(mCrop)
                .setCropSquare(mCrop)
                .setForceCrop(mCrop)
                .setCropWidth(mCropSize)
                .setCropHeight(mCropSize)
                .setCropReplaceSource(true)
                .setEnableEdit(false)
                .setEnablePreview(false)
                .build();

        //配置imageloader
        PhotoSelectPicassoImageLoader imageloader = new PhotoSelectPicassoImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(mContext, imageloader, theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }

    /**
     * 单张图片选择
     *
     * @param code
     * @param callback
     */
    public void openGallerySingle(int code, GalleryFinal.OnHanlderResultCallback callback) {
        GalleryFinal.openGallerySingle(code, functionConfig, callback);
    }

    /**
     * 多张图片选择
     *
     * @param code
     * @param maxNum
     * @param callback
     */
    public void openGalleryMulti(int code, int maxNum, GalleryFinal.OnHanlderResultCallback callback) {
        GalleryFinal.openGalleryMuti(code, maxNum, callback);
    }

    /**
     * 拍照
     *
     * @param code
     * @param callback
     */
    public void openCamera(int code, GalleryFinal.OnHanlderResultCallback callback) {
        GalleryFinal.openCamera(code, functionConfig, callback);
    }
}
