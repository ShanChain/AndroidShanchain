package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.SleepEarlierListInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/14.
 */

public class SleepEarlierAdapter extends BaseCommonAdapter<SleepEarlierListInfo> {
    public SleepEarlierAdapter(Context context, int layoutId, List<SleepEarlierListInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, SleepEarlierListInfo sleepEarierListInfo, int position) {
        holder.setText(R.id.tv_item_sleep_title,sleepEarierListInfo.getTitle());
        holder.setText(R.id.tv_item_sleep_des,sleepEarierListInfo.getDes());
        holder.setText(R.id.tv_item_sleep_active,sleepEarierListInfo.getActive() + "人参加");
        holder.setText(R.id.tv_item_sleep_arthur,sleepEarierListInfo.getName()+"创建于"+sleepEarierListInfo.getTime());
    }
}
