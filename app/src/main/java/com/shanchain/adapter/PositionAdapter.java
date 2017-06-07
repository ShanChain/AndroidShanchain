package com.shanchain.adapter;

import android.content.Context;

import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.shanchain.mvp.model.PositionInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/7.
 */

public class PositionAdapter extends BaseCommonAdapter<PositionInfo> {

    public PositionAdapter(Context context, int layoutId, List<PositionInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, PositionInfo positionInfo, int position) {
        holder.setText(R.id.tv_item_position_address,positionInfo.getAddress());
        holder.setText(R.id.tv_item_position_details,positionInfo.getDetails());
    }
}
