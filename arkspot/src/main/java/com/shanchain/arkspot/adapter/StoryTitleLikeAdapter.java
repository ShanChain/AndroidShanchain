package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.SpaceBean;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.List;


public class StoryTitleLikeAdapter extends BaseQuickAdapter<SpaceBean,BaseViewHolder> {

    public StoryTitleLikeAdapter(@LayoutRes int layoutResId, @Nullable List<SpaceBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpaceBean item) {
        helper.setText(R.id.tv_item_story_like,item.getIntro());
        /*Glide.with(mContext)
                .load(item.getBgPic())
                .into((ImageView) helper.getView(R.id.iv_item_head_like));*/

        GlideUtils.load(mContext,item.getBackground().trim(),(ImageView)helper.getView(R.id.iv_item_head_like),0);

    }
}
