package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.ShanCoinsInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class CoinsAdapter extends BaseCommonAdapter<ShanCoinsInfo> {

    public CoinsAdapter(Context context, int layoutId, List<ShanCoinsInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, ShanCoinsInfo shanCoinsInfo, int position) {
        holder.setText(R.id.tv_item_coins_type,shanCoinsInfo.getType());
        Glide.with(mContext)
                .load(R.drawable.photo6)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_coins_img));
        holder.setText(R.id.tv_item_coins_des,shanCoinsInfo.getDes());
        holder.setText(R.id.tv_item_coins_time,shanCoinsInfo.getTime());
        holder.setText(R.id.tv_item_coins_counts,shanCoinsInfo.getCounts() + "善圆");
    }
}
