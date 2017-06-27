package com.shanchain.shandata.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.TopicInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/7.
 */

public class TopicAdapter extends BaseCommonAdapter<TopicInfo> {
    public TopicAdapter(Context context, int layoutId, List<TopicInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, TopicInfo topicInfo, int position) {
        holder.setText(R.id.tv_item_topic,topicInfo.getTopic());
        TextView tvTopicNew = holder.getView(R.id.tv_item_topic_new);
        if (topicInfo.isNew()){
            tvTopicNew.setVisibility(View.VISIBLE);
        }else {
            tvTopicNew.setVisibility(View.INVISIBLE);
        }
    }
}
