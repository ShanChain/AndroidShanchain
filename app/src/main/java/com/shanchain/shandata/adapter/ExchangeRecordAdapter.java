package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.ExchangeRecordInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class ExchangeRecordAdapter extends BaseCommonAdapter<ExchangeRecordInfo> {

    public ExchangeRecordAdapter(Context context, int layoutId, List<ExchangeRecordInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, ExchangeRecordInfo exchangeRecordInfo, int position) {
        Glide.with(mContext)
                .load(R.drawable.photo_bear)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_exchange_record_img));
    }
}
