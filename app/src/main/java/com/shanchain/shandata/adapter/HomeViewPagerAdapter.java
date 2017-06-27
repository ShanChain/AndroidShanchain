package com.shanchain.shandata.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by zhoujian on 2017/6/13.
 */

public class HomeViewPagerAdapter extends PagerAdapter {
    private int[] imgUrls;

    public HomeViewPagerAdapter(int[] imgUrls) {
        this.imgUrls = imgUrls;
    }

    @Override
    public int getCount() {
        return imgUrls.length * 1000000 * 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int mPosition = position % imgUrls.length;
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(imgUrls[mPosition]);

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
