package com.shanchain.shandata.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.mvp.model.SwitchCityInfo;

import java.util.List;

public class SwitchCityListAdapter extends BaseAdapter {

    private List<SwitchCityInfo> mDatas;

    public SwitchCityListAdapter(List<SwitchCityInfo> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_city_switch,null);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.tv_item_city_letter);
            holder.tvCityName = (TextView) convertView.findViewById(R.id.tv_item_city_address);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        SwitchCityInfo cityInfo = mDatas.get(position);
        holder.tvCityName.setText(cityInfo.getAddress());
        holder.tvLetter.setText(cityInfo.getPinying());
        if (position<1){
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(cityInfo.getPinying());
        }else {
            if (mDatas.get(position-1).getPinying().equals(cityInfo.getPinying())){
                holder.tvLetter.setVisibility(View.GONE);

            }else {
                holder.tvLetter.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tvLetter;
        TextView tvCityName;
    }

}
