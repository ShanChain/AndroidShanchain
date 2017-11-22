package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.SpaceInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/25.
 */

public class StoryTitleStagAdapter extends BaseQuickAdapter<SpaceInfo, BaseViewHolder> {

    public StoryTitleStagAdapter(@LayoutRes int layoutResId, @Nullable List<SpaceInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpaceInfo item) {
        ImageView imageView= helper.getView(R.id.iv_item_head_stag);
        GlideUtils.load(mContext, item.getBackground(), imageView, 0);
        helper.setText(R.id.tv_item_head_stag_title, item.getName());
        helper.setText(R.id.tv_item_head_stag_des, item.getSlogan());

    }
}
