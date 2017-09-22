package com.shanchain.arkspot.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.shanchain.arkspot.R;

import java.util.ArrayList;

import com.shanchain.data.common.utils.DensityUtils;

/**
 * Created by zhoujian on 2017/9/1.
 */

public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList mArrayList;

    private int[] images = {R.drawable.photo_yue,R.drawable.photo_water,R.drawable.photo_public,R.drawable.photo_heart,
    R.drawable.photo_city,R.drawable.photo_bear};

    public ImagePagerAdapter(ArrayList arrayList) {
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
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = DensityUtils.dip2px(container.getContext(),DensityUtils.dip2px(container.getContext(),15));
        layoutParams.rightMargin = DensityUtils.dip2px(container.getContext(),DensityUtils.dip2px(container.getContext(),15));
        imageView.setLayoutParams(layoutParams);

        Glide.with(container.getContext()).load(mArrayList.get(position)).into(imageView);
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
