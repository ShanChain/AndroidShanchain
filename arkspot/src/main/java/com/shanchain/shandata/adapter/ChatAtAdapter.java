package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.data.common.utils.GlideUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.GroupBriefBean;

import java.util.List;

/**
 * Created by zhoujian on 2017/12/5.
 */

public class ChatAtAdapter extends BaseQuickAdapter<GroupBriefBean,BaseViewHolder> {

    public ChatAtAdapter(@LayoutRes int layoutResId, @Nullable List<GroupBriefBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupBriefBean item) {
        helper.setText(R.id.tv_item_at_name,item.getName());
        GlideUtils.load(mContext,item.getHeadImg(), (ImageView) helper.getView(R.id.iv_item_at_avatar),0);
    }
}
