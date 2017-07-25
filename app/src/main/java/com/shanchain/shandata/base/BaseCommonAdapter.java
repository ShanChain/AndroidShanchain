package com.shanchain.shandata.base;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public abstract class BaseCommonAdapter<T> extends CommonAdapter<T> {

    public BaseCommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, T t, int position) {
        bindData(holder,t,position);
    }

    public abstract void bindData(ViewHolder holder, T t, int position);


}
