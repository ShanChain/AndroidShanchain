package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.HotNewsInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class HotNewsAdapter extends BaseCommonAdapter<HotNewsInfo> {

    public HotNewsAdapter(Context context, int layoutId, List<HotNewsInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, HotNewsInfo hotNewsInfo, int position) {
        Glide.with(mContext)
                .load(R.drawable.photo_water)
                .into((ImageView) holder.getView(R.id.iv_hot_news_img));
    }
}
