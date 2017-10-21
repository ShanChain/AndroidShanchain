package com.shanchain.arkspot.adapter;


import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.shanchain.arkspot.ui.view.activity.story.ImageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StoryItemNineAdapter extends NineGridImageViewAdapter<String> {
    @Override
    protected void onDisplayImage(Context context, ImageView imageView, String imgUrl) {
        //Glide.with(context).load(imgUrl).centerCrop().into(imageView);
        //这里用glide会出现跳动情况
        Picasso.with(context).load(imgUrl).into(imageView);
    }

    @Override
    protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
        //图片点击事件
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < list.size(); i ++) {
            arrayList.add(list.get(i));
        }

        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("index",index);
        intent.putStringArrayListExtra("arrayList",arrayList);
        context.startActivity(intent);

    }
}
