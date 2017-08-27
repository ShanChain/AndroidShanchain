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

/**
 * Created by zhoujian on 2017/8/25.
 */

public class SearchRoleAdapter extends BaseQuickAdapter<RoleInfo,BaseViewHolder> {

    public SearchRoleAdapter(@LayoutRes int layoutResId, @Nullable List<RoleInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoleInfo item) {
        Glide.with(mContext)
                .load(item.getImg())
                .into((ImageView) helper.getView(R.id.iv_item_search_role));
        helper.setText(R.id.tv_item_search_role_name,item.getName());
        helper.setText(R.id.tv_item_search_role_des,item.getDes());
    }
}
