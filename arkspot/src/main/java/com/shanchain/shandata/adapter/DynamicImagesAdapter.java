package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.DynamicImageInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/6.
 */

public class DynamicImagesAdapter extends BaseQuickAdapter<DynamicImageInfo,BaseViewHolder> {

    public DynamicImagesAdapter(@LayoutRes int layoutResId, @Nullable List<DynamicImageInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicImageInfo item) {
        ImageView ivImg = helper.getView(R.id.iv_item_dynamic_image);
        GlideUtils.load(mContext,item.getImg(),ivImg,0);
        helper.addOnClickListener(R.id.iv_item_dynamic_clear);
    }
}
