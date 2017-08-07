package com.shanchain.shandata.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;


public class LifeLabelViewPagerAdapter extends PagerAdapter {


    private List<String> listDatas;
    private List<Integer> imgs;

    public LifeLabelViewPagerAdapter() {
        listDatas = new ArrayList<>();
        listDatas.add("家人一起出去玩");
        listDatas.add("玩游戏");
        listDatas.add("睡觉");
        listDatas.add("出门逛超市");
        listDatas.add("睡觉~~~~");

        imgs = new ArrayList<>();
        imgs.add(R.drawable.photo_bear);
        imgs.add(R.drawable.photo_public);
        imgs.add(R.drawable.photo_yue);
        imgs.add(R.drawable.photo_water);

    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = View.inflate(container.getContext(),R.layout.item_life_label,null);
        Banner banner = (Banner) view.findViewById(R.id.vp_item_life_label);
        TextView tv = (TextView) view.findViewById(R.id.tv_item_life_label_date);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_item_life_label);

        banner.setImageLoader(new BannerImageAdapter());
        banner.setImages(imgs);
        banner.isAutoPlay(false);
        banner.start();
        tv.setText("8月4日");
        rv.setLayoutManager(new GridLayoutManager(container.getContext(),2));
        rv.setAdapter(new LifeLabelAdapter(container.getContext(),R.layout.item_life_label_list,listDatas));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
