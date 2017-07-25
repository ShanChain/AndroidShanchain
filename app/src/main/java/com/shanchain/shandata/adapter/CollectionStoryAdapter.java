package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.CollectionStoryInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class CollectionStoryAdapter extends BaseCommonAdapter<CollectionStoryInfo> {

    public CollectionStoryAdapter(Context context, int layoutId, List<CollectionStoryInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, CollectionStoryInfo collectionStoryInfo, int position) {
        Glide.with(mContext)
                .load(R.drawable.photo_yue)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_collection_story_img));
    }
}
