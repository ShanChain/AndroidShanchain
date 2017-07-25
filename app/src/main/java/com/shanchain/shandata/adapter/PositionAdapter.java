package com.shanchain.shandata.adapter;

import android.content.Context;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.PositionInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class PositionAdapter extends BaseCommonAdapter<PositionInfo> {

    public PositionAdapter(Context context, int layoutId, List<PositionInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindData(ViewHolder holder, PositionInfo positionInfo, int position) {
        holder.setText(R.id.tv_item_position_address,positionInfo.getAddress());
        holder.setText(R.id.tv_item_position_details,positionInfo.getDetails());
    }
}
