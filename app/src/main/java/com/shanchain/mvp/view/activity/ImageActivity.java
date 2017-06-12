package com.shanchain.mvp.view.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.shanchain.R;
import com.shanchain.adapter.ImagePagerAdapter;
import com.shanchain.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;

public class ImageActivity extends BaseActivity {


    @Bind(R.id.vp_images)
    ViewPager mVpImages;
    @Bind(R.id.activity_image)
    RelativeLayout mActivityImage;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_image;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        int index = intent.getIntExtra("index", 0);
        ArrayList<String> arrayList = intent.getStringArrayListExtra("arrayList");
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(arrayList);
        mVpImages.setAdapter(imagePagerAdapter);
        mVpImages.setCurrentItem(index);
        imagePagerAdapter.setOnPagerClickListener(new ImagePagerAdapter.pagerClickListener() {
            @Override
            public void onPagerClickListener() {
                finish();
            }
        });
    }
}
