package com.shanchain.arkspot.adapter;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/24.
 */

public class StoryItemNineAdapter extends NineGridImageViewAdapter<Integer> {
    @Override
    protected void onDisplayImage(Context context, ImageView imageView, Integer imgId) {
        Glide.with(context).load(imgId).into(imageView);
    }

    @Override
    protected void onItemImageClick(Context context, ImageView imageView, int index, List<Integer> list) {
        //图片点击事件
        
    }
}
