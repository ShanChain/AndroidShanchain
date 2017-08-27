package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.StoryLikeInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/25.
 */

public class StoryTitleLikeAdapter extends BaseQuickAdapter<StoryLikeInfo,BaseViewHolder> {

    public StoryTitleLikeAdapter(@LayoutRes int layoutResId, @Nullable List<StoryLikeInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryLikeInfo item) {
        helper.setText(R.id.tv_item_story_like,item.getTitle());
        Glide.with(mContext)
                .load(item.getImg())
                .into((ImageView) helper.getView(R.id.iv_item_head_like));

    }
}
