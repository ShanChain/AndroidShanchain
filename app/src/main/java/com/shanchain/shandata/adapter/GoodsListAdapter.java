package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.FoundNightMarketInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class GoodsListAdapter extends BaseCommonAdapter<FoundNightMarketInfo> {

    public GoodsListAdapter(Context context, int layoutId, List<FoundNightMarketInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, FoundNightMarketInfo foundNightMarketInfo, int position) {

    }
}
