package com.shanchain.adapter;

import android.content.Context;

import com.shanchain.R;
import com.shanchain.base.BaseCommonAdapter;
import com.shanchain.mvp.model.DynamicInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhoujian on 2017/6/12.
 */

public class HomeAdapter extends BaseCommonAdapter<DynamicInfo> {

    public HomeAdapter(Context context, int layoutId, List<DynamicInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void bindDatas(ViewHolder holder, DynamicInfo dynamicInfo, int position) {
        holder.setText(R.id.tv_item_home_left1,dynamicInfo.getLeft1());
    }
}
