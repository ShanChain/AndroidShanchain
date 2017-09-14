package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.MessageHomeInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/7.
 */

public class MessageHomeAdapter extends BaseQuickAdapter<MessageHomeInfo, BaseViewHolder> {

    public MessageHomeAdapter(@LayoutRes int layoutResId, @Nullable List<MessageHomeInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageHomeInfo item) {
        if (item.getUnRead() <= 0) {
            helper.setVisible(R.id.tv_item_msg_home_unread, false);
        } else if (item.getUnRead() <= 99 && item.getUnRead() > 0) {
            helper.setVisible(R.id.tv_item_msg_home_unread, true);
            helper.setText(R.id.tv_item_msg_home_unread, item.getUnRead() + "");
        } else {
            helper.setVisible(R.id.tv_item_msg_home_unread, true);
            helper.setText(R.id.tv_item_msg_home_unread, "99+");
        }

        if (item.isTop()) {
            helper.setBackgroundColor(R.id.rl_item_msg_home, mContext.getResources().getColor(R.color.colorDivider));
        } else {
            helper.setBackgroundColor(R.id.rl_item_msg_home, mContext.getResources().getColor(R.color.colorWhite));
        }

    }
}
