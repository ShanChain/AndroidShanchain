package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.WaitDonateInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class WaitDonateAdapter extends BaseCommonAdapter<WaitDonateInfo> {

    public WaitDonateAdapter(Context context, int layoutId, List<WaitDonateInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, WaitDonateInfo waitDonateInfo, int position) {

    }
}
