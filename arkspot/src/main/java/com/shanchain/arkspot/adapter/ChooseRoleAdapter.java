package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.RoleInfo;

import java.util.List;

public class ChooseRoleAdapter extends BaseQuickAdapter<RoleInfo,BaseViewHolder> {

    public ChooseRoleAdapter(@LayoutRes int layoutResId, @Nullable List<RoleInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoleInfo item) {
        Glide.with(mContext)
                .load(item.getImg())
                .into((ImageView) helper.getView(R.id.iv_item_role));
        helper.setText(R.id.tv_item_role,item.getName());
    }
}
