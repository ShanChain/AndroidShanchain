package com.shanchain.shandata.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.BdCommentBean;
import com.shanchain.shandata.utils.DateUtils;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/4.
 */

public class DynamicCommentAdapter extends BaseQuickAdapter<BdCommentBean, BaseViewHolder> {

    private Drawable likeDefault;
    private Drawable likeSelected;

    public DynamicCommentAdapter(@LayoutRes int layoutResId, @Nullable List<BdCommentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BdCommentBean item) {

        helper.setText(R.id.tv_item_dynamic_comment, item.getCommentBean().getContent());
        helper.setText(R.id.tv_item_dynamic_comment_time, DateUtils.formatFriendly(new Date(item.getCommentBean().getCreateTime())));
        helper.addOnClickListener(R.id.tv_item_comment_like);
        helper.setText(R.id.tv_item_dynamic_comment_name,item.getContactBean().getName());
        TextView tvLike = helper.getView(R.id.tv_item_comment_like);
        tvLike.setText("" + item.getCommentBean().getSupportCount());
        ImageView ivHead = helper.getView(R.id.iv_item_dynamic_comment_avatar);
        GlideUtils.load(mContext,item.getContactBean().getHeadImg(),ivHead,0);
        if (likeDefault == null) {
            likeDefault = mContext.getResources().getDrawable(R.mipmap.abs_dynamic_btn_like_default);
        }

        if (likeSelected == null){
            likeSelected = mContext.getResources().getDrawable(R.mipmap.abs_dynamic_btn_like_selected);
        }

        likeDefault.setBounds(0,0,likeDefault.getMinimumWidth(),likeDefault.getMinimumHeight());
        likeSelected.setBounds(0,0,likeSelected.getMinimumWidth(),likeSelected.getMinimumHeight());

        tvLike.setCompoundDrawables(null,null,item.getCommentBean().isMySupport()?likeSelected:likeDefault,null);

    }
}
