package com.shanchain.shandata.adapter;

import android.content.Context;
import android.view.View;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseCommonAdapter;
import com.shanchain.shandata.mvp.model.SwitchCityInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class SwitchCityAdapter extends BaseCommonAdapter<SwitchCityInfo> {

    private List<SwitchCityInfo> mDatas;
    public SwitchCityAdapter(Context context, int layoutId, List<SwitchCityInfo> datas) {
        super(context, layoutId, datas);
        mDatas = datas;
    }

    @Override
    public void bindData(ViewHolder holder, SwitchCityInfo switchCityInfo, int position) {
        String pinying = switchCityInfo.getPinying();



        holder.setText(R.id.tv_item_city_letter, pinying);
        holder.setText(R.id.tv_item_city_address,switchCityInfo.getAddress());

        if (position <= 2 ){
            holder.getView(R.id.tv_item_city_letter).setVisibility(View.VISIBLE);
        }else {

            String pinying1 = mDatas.get(position-2).getPinying();
            String pinying2 = mDatas.get(position-3).getPinying();

            if (pinying1.equals(pinying2)){
                holder.getView(R.id.tv_item_city_letter).setVisibility(View.GONE);
            }else {
                holder.getView(R.id.tv_item_city_letter).setVisibility(View.VISIBLE);
            }
        }

    }
}
