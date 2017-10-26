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

/**
 * Created by zhoujian on 2017/8/25.
 */

public class StoryTitleStagAdapter extends BaseQuickAdapter<SpaceBean,BaseViewHolder> {
    public StoryTitleStagAdapter(@LayoutRes int layoutResId, @Nullable List<SpaceBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpaceBean item) {

        GlideUtils.load(mContext,item.getBackground(),(ImageView) helper.getView(R.id.iv_item_head_stag),0);

        helper.setText(R.id.tv_item_head_stag_title,item.getName());

        helper.setText(R.id.tv_item_head_stag_des,item.getSlogan());

    }
}
