package com.shanchain.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.shanchain.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/1.
 */

public class NineGridDetailsAdapter extends NineGridImageViewAdapter<String> {

    @Override
    protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
        super.onItemImageClick(context, imageView, index, list);
    }

    @Override
    protected void onDisplayImage(Context context, ImageView imageView, String imageurl) {
        Picasso.with(context).load(R.drawable.photo6).into(imageView);
    }
}
