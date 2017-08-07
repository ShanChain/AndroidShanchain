package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


public class LifeLabelAdapter extends BaseCommonAdapter<String> {

    public LifeLabelAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_item_life_label_list,s);
    }
}
