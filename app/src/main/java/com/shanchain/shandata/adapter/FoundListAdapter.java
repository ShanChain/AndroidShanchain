package com.shanchain.shandata.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanchain.shandata.R;

public class FoundListAdapter extends BaseAdapter {


    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.item_found_hot,null);
        //View view = LayoutInflater.from(context).inflate(R.layout.item_found_hot,null);
        TextView tvType = (TextView) view.findViewById(R.id.tv_item_found_hot_type);
        TextView tvDes = (TextView) view.findViewById(R.id.tv_item_found_hot_des);

        return view;
    }
}
