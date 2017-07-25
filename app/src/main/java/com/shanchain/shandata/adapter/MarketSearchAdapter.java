package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.MarketSearchInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class MarketSearchAdapter extends BaseCommonAdapter<MarketSearchInfo> {

    public MarketSearchAdapter(Context context, int layoutId, List<MarketSearchInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, MarketSearchInfo marketSearchInfo, int position) {
        holder.setText(R.id.tv_item_search_market_name,marketSearchInfo.getGoodsName());
        holder.setText(R.id.tv_item_search_market_price,marketSearchInfo.getPrice() + "善圆");
        holder.setText(R.id.tv_item_search_market_des,marketSearchInfo.getDes());

        Glide.with(mContext)
                .load(R.drawable.photo_bear)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_search_market_img));

    }
}
