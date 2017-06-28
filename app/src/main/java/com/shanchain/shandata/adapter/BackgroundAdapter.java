package com.shanchain.shandata.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.BackgroundInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/28.
 */

public class BackgroundAdapter extends BaseCommonAdapter<BackgroundInfo> {

    public BackgroundAdapter(Context context, int layoutId, List<BackgroundInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, BackgroundInfo backgroundInfo, int position) {
        Glide.with(mContext)
                .load(backgroundInfo.getRes())
                .into((ImageView) holder.getView(R.id.iv_item_background));
        holder.setText(R.id.tv_background_des,backgroundInfo.getDes());
        holder.setText(R.id.tv_background_type,backgroundInfo.getType());
    }
}
