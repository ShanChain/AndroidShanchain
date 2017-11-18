package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.StoryModelBean;
import com.shanchain.arkspot.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/1.
 */

public class StoryChainAdapter extends BaseQuickAdapter<StoryModelBean, BaseViewHolder> {

    public StoryChainAdapter(@LayoutRes int layoutResId, @Nullable List<StoryModelBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryModelBean item) {
        helper.addOnClickListener(R.id.tv_item_story_forwarding)
                .addOnClickListener(R.id.tv_item_story_comment)
                .addOnClickListener(R.id.tv_item_story_like)
                .addOnClickListener(R.id.iv_item_story_avatar)
                .addOnClickListener(R.id.iv_item_story_more);
        String time = DateUtils.formatFriendly(new Date(item.getCreateTime()));
        helper.setText(R.id.tv_item_story_time, time);
        helper.setText(R.id.tv_item_story_chain_content,item.getIntro());
    }
}
