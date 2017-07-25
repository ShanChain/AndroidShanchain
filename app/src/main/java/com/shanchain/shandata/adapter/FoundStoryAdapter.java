package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.FoundGoodStoryInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class FoundStoryAdapter extends BaseCommonAdapter<FoundGoodStoryInfo> {

    public FoundStoryAdapter(Context context, int layoutId, List<FoundGoodStoryInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, FoundGoodStoryInfo foundGoodStoryInfo, int position) {
        Glide.with(mContext)
                .load(R.drawable.photo_str)
                .into((ImageView) holder.getView(R.id.iv_item_found_market));
    }
}
