package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.SpaceCharacterBean;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.List;

public class ChooseRoleAdapter extends BaseQuickAdapter<SpaceCharacterBean,BaseViewHolder> {

    public ChooseRoleAdapter(@LayoutRes int layoutResId, @Nullable List<SpaceCharacterBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpaceCharacterBean item) {

        GlideUtils.load(mContext,item.getHeadImg(),(ImageView) helper.getView(R.id.iv_item_role));

        helper.setText(R.id.tv_item_role,item.getName());
    }
}
