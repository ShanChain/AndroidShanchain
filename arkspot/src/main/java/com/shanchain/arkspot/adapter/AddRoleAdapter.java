package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.StoryTagInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/28.
 */

public class AddRoleAdapter extends BaseQuickAdapter<StoryTagInfo,BaseViewHolder> {

    public AddRoleAdapter(@LayoutRes int layoutResId, @Nullable List<StoryTagInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryTagInfo item) {
        helper.setText(R.id.tv_item_add_role_tag,item.getTag());
        helper.setBackgroundRes(R.id.tv_item_add_role_tag,
                item.isSelected()?R.mipmap.abs_therrbody_btn_more_default
                :R.mipmap.abs_therrbody_btn_label_default);
        helper.setTextColor(R.id.tv_item_add_role_tag,
                mContext.getResources().getColor(item.isSelected()?
                        R.color.colorWhite:R.color.colorTextDefault));

    }
}
