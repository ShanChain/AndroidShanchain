package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.SpaceInfo;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/25.
 */

public class StoryTitleStagAdapter extends BaseQuickAdapter<SpaceInfo, BaseViewHolder> {
    ImageView imageView;
    private int mIvWith;
    private int mScreenWidth;
    private int mClearance;

    public StoryTitleStagAdapter(@LayoutRes int layoutResId, @Nullable List<SpaceInfo> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, SpaceInfo item) {

        mScreenWidth = DensityUtils.getScreenWidth(mContext);
        mClearance = DensityUtils.dip2px(mContext, 5);
        mIvWith = (mScreenWidth - mClearance * 4) / 2;

        imageView = helper.getView(R.id.iv_item_head_stag);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        //layoutParams.height = 400;
        layoutParams.width = mIvWith;
        imageView.setLayoutParams(layoutParams);

        GlideUtils.load(mContext, item.getBackground(), imageView, 0);

        helper.setText(R.id.tv_item_head_stag_title, item.getName());

        helper.setText(R.id.tv_item_head_stag_des, item.getSlogan());

    }
}
