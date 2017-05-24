package com.shanchain.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/5/24.
 * 列表图片适配器
 */

public class ImageAdapter extends BaseCommonAdapter<String> {

    public ImageAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, String s, int position) {

        Glide.with(mContext).load(R.mipmap.popular_image_story_default).
                into((ImageView) holder.itemView.findViewById(R.id.iv_image));
    }
}
