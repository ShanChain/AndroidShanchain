package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.FoundNightMarketInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class MarketOtherAdapter extends BaseCommonAdapter<FoundNightMarketInfo> {

    public MarketOtherAdapter(Context context, int layoutId, List<FoundNightMarketInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, FoundNightMarketInfo foundNightMarketInfo, int position) {
        Glide.with(mContext)
                .load(R.drawable.photo_latio)
                .into((ImageView) holder.getView(R.id.iv_item_found_market_goods));
        holder.setText(R.id.tv_item_found_market_name,"美味辣条");
        holder.setText(R.id.tv_item_found_market_price,"0.5善圆");
    }
}
