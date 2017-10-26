package com.shanchain.arkspot.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.GlideUtils;

import java.util.ArrayList;

/**
 * Created by zhoujian on 2017/9/1.
 */

public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<String> mArrayList;


    public ImagePagerAdapter(ArrayList<String> arrayList) {
        mArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        layoutParams.leftMargin = DensityUtils.dip2px(container.getContext(),DensityUtils.dip2px(container.getContext(),15));
        layoutParams.rightMargin = DensityUtils.dip2px(container.getContext(),DensityUtils.dip2px(container.getContext(),15));
        imageView.setLayoutParams(layoutParams);

        GlideUtils.load(container.getContext(),mArrayList.get(position),imageView,0);
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPagerClickListener.onPagerClickListener();
            }
        });
        return imageView;
    }

    public interface pagerClickListener{
        void onPagerClickListener();
    }


    private pagerClickListener mPagerClickListener;
    public void setOnPagerClickListener(pagerClickListener pagerClickListener){
        mPagerClickListener = pagerClickListener;
    }

}
