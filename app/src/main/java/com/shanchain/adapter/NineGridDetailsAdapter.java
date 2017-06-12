package com.shanchain.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.shanchain.R;
import com.shanchain.mvp.view.activity.ImageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoujian on 2017/6/1.
 */

public class NineGridDetailsAdapter extends NineGridImageViewAdapter<String> {

    @Override
    protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
        super.onItemImageClick(context, imageView, index, list);
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < list.size(); i ++) {
            arrayList.add(list.get(i));
        }

        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("index",index);
        intent.putStringArrayListExtra("arrayList",arrayList);
        context.startActivity(intent);
    }

    @Override
    protected void onDisplayImage(Context context, ImageView imageView, String imageurl) {
        Picasso.with(context).load(R.drawable.photo6).into(imageView);
    }
}
