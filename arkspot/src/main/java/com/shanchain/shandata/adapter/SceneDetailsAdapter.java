package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.BdGroupMemberInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/14.
 */

public class SceneDetailsAdapter extends BaseQuickAdapter<BdGroupMemberInfo,BaseViewHolder> {

    public SceneDetailsAdapter(@LayoutRes int layoutResId, @Nullable List<BdGroupMemberInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BdGroupMemberInfo item) {
        ImageView iv = helper.getView(R.id.iv_item_scene_numbers);
        GlideUtils.load(mContext,item.getHeadImg(),iv,0);
    }
}
