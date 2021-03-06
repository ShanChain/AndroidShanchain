package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.SpaceInfo;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.List;


public class StoryTitleLikeAdapter extends BaseQuickAdapter<SpaceInfo,BaseViewHolder> {

    public StoryTitleLikeAdapter(@LayoutRes int layoutResId, @Nullable List<SpaceInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpaceInfo item) {
        helper.setText(R.id.tv_item_story_like,item.getName());
        /*Glide.with(mContext)
                .load(item.getBgPic())
                .into((ImageView) helper.getView(R.id.iv_item_head_like));*/

        GlideUtils.load(mContext,item.getBackground().trim(),(ImageView)helper.getView(R.id.iv_item_head_like),0);

    }
}
