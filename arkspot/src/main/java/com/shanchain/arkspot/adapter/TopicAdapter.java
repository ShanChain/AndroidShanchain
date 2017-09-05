package com.shanchain.arkspot.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.TopicInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/8/30.
 */

public class TopicAdapter extends BaseQuickAdapter<TopicInfo,BaseViewHolder> {

    public TopicAdapter(@LayoutRes int layoutResId, @Nullable List<TopicInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicInfo item) {
        helper.setText(R.id.tv_item_topic,"#"+item.getTopic()+"#");
    }
}