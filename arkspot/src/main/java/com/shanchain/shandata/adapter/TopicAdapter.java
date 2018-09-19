package com.shanchain.shandata.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.ui.model.TopicInfo;

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
        boolean isNew = item.isNew();
        if (isNew){
            helper.setText(R.id.tv_item_topic_new,"#创建新话题#");
        }else {
            helper.setText(R.id.tv_item_topic_new,"话题");
        }
        helper.setText(R.id.tv_item_topic,"#"+item.getTopic()+"#");
    }
}
