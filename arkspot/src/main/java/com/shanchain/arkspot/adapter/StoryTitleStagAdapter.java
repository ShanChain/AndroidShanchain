package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.StoryTitleStagInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/25.
 */

public class StoryTitleStagAdapter extends BaseQuickAdapter<StoryTitleStagInfo,BaseViewHolder> {
    public StoryTitleStagAdapter(@LayoutRes int layoutResId, @Nullable List<StoryTitleStagInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryTitleStagInfo item) {
        Glide.with(mContext)
                .load(item.getImg())
                .into((ImageView) helper.getView(R.id.iv_item_head_stag));

        helper.setText(R.id.tv_item_head_stag_title,item.getTitle());

        helper.setText(R.id.tv_item_head_stag_des,item.getDes());

    }
}
