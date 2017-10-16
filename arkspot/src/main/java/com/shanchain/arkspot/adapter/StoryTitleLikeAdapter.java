package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.FavoriteSpaceBean;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/25.
 */

public class StoryTitleLikeAdapter extends BaseQuickAdapter<FavoriteSpaceBean,BaseViewHolder> {

    public StoryTitleLikeAdapter(@LayoutRes int layoutResId, @Nullable List<FavoriteSpaceBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FavoriteSpaceBean item) {
        helper.setText(R.id.tv_item_story_like,item.getIntro());
        Glide.with(mContext)
                .load(item.getBgPic())
                .into((ImageView) helper.getView(R.id.iv_item_head_like));

    }
}
