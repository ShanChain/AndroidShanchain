package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.TagInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class TagAdapter extends BaseCommonAdapter<TagInfo> {

    public TagAdapter(Context context, int layoutId, List<TagInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, TagInfo tagInfo, int position) {
        holder.setText(R.id.tv_item_hot_tag , tagInfo.getTagName());
        holder.setText(R.id.tv_item_hot_tag_count,tagInfo.getStoryCount() + "个故事");
    }
}
