package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.StoryInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/1.
 */

public class StoryChainAdapter extends BaseQuickAdapter<StoryInfo, BaseViewHolder> {

    public StoryChainAdapter(@LayoutRes int layoutResId, @Nullable List<StoryInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryInfo item) {
        helper.addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like)
                .addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more);
        helper.setText(R.id.tv_item_story_time, item.getTime() + "    10楼");
    }
}
