package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.SleepEarlierResultInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/22.
 */

public class SleepEarlierResultAdapter extends BaseCommonAdapter<SleepEarlierResultInfo> {

    public SleepEarlierResultAdapter(Context context, int layoutId, List<SleepEarlierResultInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, SleepEarlierResultInfo sleepEarlierResultInfo, int position) {
        holder.setText(R.id.tv_item_sleep_result_name, sleepEarlierResultInfo.getName());
        holder.setText(R.id.tv_item_sleep_result_confidence, sleepEarlierResultInfo.getConfidence());
        holder.setText(R.id.tv_item_sleep_result_shanquan, sleepEarlierResultInfo.getShanquan());
        holder.setText(R.id.tv_item_sleep_result_shanyuan, sleepEarlierResultInfo.getShanyuan());
        Glide.with(mContext).load(R.drawable.photo2)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_sleep_result_img));
    }
}
