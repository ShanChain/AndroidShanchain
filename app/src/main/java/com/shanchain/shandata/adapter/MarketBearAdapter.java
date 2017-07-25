package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.FoundNightMarketInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class MarketBearAdapter extends BaseCommonAdapter<FoundNightMarketInfo> {

    public MarketBearAdapter(Context context, int layoutId, List<FoundNightMarketInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, FoundNightMarketInfo foundNightMarketInfo, int position) {
        Glide.with(mContext)
                .load(R.drawable.photo_bear)
                .into((ImageView) holder.getView(R.id.iv_item_found_market_goods));
    }
}
