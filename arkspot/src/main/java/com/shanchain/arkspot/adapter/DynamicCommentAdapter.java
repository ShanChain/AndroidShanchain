package com.shanchain.arkspot.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.CommentBean;
import com.shanchain.arkspot.utils.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zhoujian on 2017/9/4.
 */

public class DynamicCommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    private Drawable likeDefault;
    private Drawable likeSelected;

    public DynamicCommentAdapter(@LayoutRes int layoutResId, @Nullable List<CommentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {

        helper.setText(R.id.tv_item_dynamic_comment, item.getContent());
        helper.setText(R.id.tv_item_dynamic_comment_time, DateUtils.formatFriendly(new Date(item.getCreateTime())));
        helper.addOnClickListener(R.id.tv_item_comment_like);

        TextView tvLike = helper.getView(R.id.tv_item_comment_like);
        tvLike.setText("" + item.getSupportCount());

        if (likeDefault == null) {
            likeDefault = mContext.getResources().getDrawable(R.mipmap.abs_dynamic_btn_like_default);
        }

        if (likeSelected == null){
            likeSelected = mContext.getResources().getDrawable(R.mipmap.abs_dynamic_btn_like_selected);
        }

        likeDefault.setBounds(0,0,likeDefault.getMinimumWidth(),likeDefault.getMinimumHeight());
        likeSelected.setBounds(0,0,likeSelected.getMinimumWidth(),likeSelected.getMinimumHeight());

        tvLike.setCompoundDrawables(null,null,item.isMySupport()?likeSelected:likeDefault,null);

    }
}
