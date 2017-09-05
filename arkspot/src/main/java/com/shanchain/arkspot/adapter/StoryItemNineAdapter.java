package com.shanchain.arkspot.adapter;


import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.shanchain.arkspot.ui.view.activity.story.ImageActivity;

import java.util.ArrayList;
import java.util.List;

public class StoryItemNineAdapter extends NineGridImageViewAdapter<Integer> {
    @Override
    protected void onDisplayImage(Context context, ImageView imageView, Integer imgId) {
        Glide.with(context).load(imgId).into(imageView);
    }

    @Override
    protected void onItemImageClick(Context context, ImageView imageView, int index, List<Integer> list) {
        //图片点击事件
        ArrayList<Integer> arrayList = new ArrayList<>();

        for (int i = 0; i < list.size(); i ++) {
            arrayList.add(list.get(i));
        }

        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("index",index);
        intent.putIntegerArrayListExtra("arrayList",arrayList);
        context.startActivity(intent);

    }
}
