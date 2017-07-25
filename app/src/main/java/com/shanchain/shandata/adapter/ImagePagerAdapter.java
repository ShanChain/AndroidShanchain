package com.shanchain.shandata.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.utils.DensityUtils;

import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {
    private ArrayList mArrayList;

    private int[] images = {R.drawable.photo,R.drawable.photo2,R.drawable.photo3,R.drawable.photo4,R.drawable.photo5,R.drawable.photo6};

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

        Glide.with(container.getContext()).load(images[position]).into(imageView);
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
