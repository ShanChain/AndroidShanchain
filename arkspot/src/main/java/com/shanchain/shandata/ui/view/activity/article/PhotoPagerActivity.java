package com.shanchain.shandata.ui.view.activity.article;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.HackyViewPager;
import com.shanchain.shandata.widgets.takevideo.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by WealChen
 * Date : 2019/8/1
 * Describe :查看图片列表
 */
public class PhotoPagerActivity extends BaseActivity {
    @Bind(R.id.view_pager)
    HackyViewPager viewPager;
    @Bind(R.id.im_back)
    ImageView imBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    private List<String> photoList;
    private List<Uri> mUriList = new ArrayList<>();
    private int position;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_view_pager_new;
    }

    @Override
    protected void initViewsAndEvents() {
        photoList = new ArrayList<>();
        photoList = getIntent().getStringArrayListExtra("list");
        position = getIntent().getIntExtra("position",0);

        viewPager.setAdapter(new SamplePagerAdapter());
        viewPager.setCurrentItem(position);
        tvTitle.setText((position+1)+"/"+photoList.size());
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText((position+1)+"/"+photoList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
//            photoView.setImageURI(mUriList.get(position));
            // Now just add PhotoView to ViewPager and return it
            Glide.with(mContext).load(HttpApi.BASE_URL+photoList.get(position).replaceAll("\\\\",""))
                    .into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
