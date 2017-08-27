package com.shanchain.arkspot.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.ui.model.FloorsInfo;

import java.util.ArrayList;

/**
 * Created by zhoujian on 2017/8/24.
 */

public class StoryItemFloorsAdapter extends BaseAdapter {

    private ArrayList<FloorsInfo> datas;

    public StoryItemFloorsAdapter(ArrayList<FloorsInfo> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.item_floors,null);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_item_floors_content);



        return view;
    }
}
