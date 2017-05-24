package com.shanchain.base;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * Created by zhoujian on 2017/5/23.
 */

public abstract class BaseCommonAdapter<T> extends CommonAdapter<T> {



    public BaseCommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, T t, int position) {
        bindDatas(holder,t,position);
    }

    public abstract void bindDatas(ViewHolder holder, T t, int position);


}
