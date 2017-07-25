package com.shanchain.shandata.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.mvp.model.RecommendedCityInfo;

import java.util.ArrayList;

/**
 * Created by zhoujian on 2017/7/11.
 */

public class CityGridAdapter extends BaseAdapter {

    private ArrayList<RecommendedCityInfo> cities;

    public CityGridAdapter(ArrayList<RecommendedCityInfo> cities) {
        this.cities = cities;
    }

    @Override
    public int getCount() {
        return cities.size();
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
        convertView = View.inflate(parent.getContext(), R.layout.item_city_recommended,null);
        RecommendedCityInfo city = cities.get(position);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_item_city_recommended);
        textView.setText(city.getCityName());
        textView.setTextColor(city.isSeletcted()?parent.getResources().getColor(R.color.colorTheme):parent.getResources().getColor(R.color.colorBtn));
        textView.setBackgroundResource(city.isSeletcted()?R.drawable.shape_bg_box_city_selected:R.drawable.shape_bg_box_city_default);
        return convertView;
    }
}
