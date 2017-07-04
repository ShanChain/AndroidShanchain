package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.MyPublicWelfareInfo;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/30.
 */

public class MyPublicWelfareAdapter extends BaseCommonAdapter<MyPublicWelfareInfo> {
    public MyPublicWelfareAdapter(Context context, int layoutId, List<MyPublicWelfareInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, MyPublicWelfareInfo myPublicWelfareInfo, int position) {

        Glide.with(mContext)
                .load(R.drawable.photo6)
                .transform(new GlideCircleTransform(mContext))
                .into((ImageView) holder.getView(R.id.iv_item_public_welfare_avatar));

        holder.setText(R.id.tv_item_public_welfare,myPublicWelfareInfo.getPubWelfare());
        holder.setText(R.id.tv_item_public_welfare_time,myPublicWelfareInfo.getTime());
        holder.setText(R.id.tv_item_public_welfare_count,myPublicWelfareInfo.getShanVouchers());
    }
}
