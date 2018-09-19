package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.SpaceCharacterBean;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/25.
 */

public class SearchRoleAdapter extends BaseQuickAdapter<SpaceCharacterBean,BaseViewHolder> {

    public SearchRoleAdapter(@LayoutRes int layoutResId, @Nullable List<SpaceCharacterBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpaceCharacterBean item) {
        GlideUtils.load(mContext,item.getHeadImg(),(ImageView) helper.getView(R.id.iv_item_search_role),0);

        helper.setText(R.id.tv_item_search_role_name,item.getName());
        helper.setText(R.id.tv_item_search_role_des,item.getIntro());
    }
}
