package com.shanchain.shandata.mvp.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.BannerImageAdapter;
import com.shanchain.shandata.adapter.HomeAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.mvp.model.DynamicInfo;
import com.shanchain.shandata.mvp.view.activity.challenge.ChallengeActivity;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/5/16.
 * 主页页面
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.xrv_home)
    XRecyclerView mXrvHome;
    private List<DynamicInfo> datas;
    private View mHeadView;
    private int[] imgUrls = {R.drawable.photo6,R.drawable.photo5,R.drawable.photo4};
    private Banner mVpHeadHome;
    private TextView mTvHeadHomeChallenge;
    private TextView mTvHeadHomeStory;
    private List<Integer> imgs ;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_home, null);
    }

    @Override
    public void initData() {
        getData();
        initHeadView();
        mXrvHome.setLayoutManager(new LinearLayoutManager(mActivity));

        mXrvHome.addHeaderView(mHeadView);
        mXrvHome.setPullRefreshEnabled(false);
        mXrvHome.setLoadingMoreEnabled(false);
        HomeAdapter homeAdapter = new HomeAdapter(mActivity,R.layout.item_home,datas);
        mXrvHome.setAdapter(homeAdapter);

    }

    private void initHeadView() {
        mHeadView = View.inflate(mActivity, R.layout.headview_home,null);
        mVpHeadHome = (Banner) mHeadView.findViewById(R.id.vp_head_home);
        mTvHeadHomeChallenge = (TextView) mHeadView.findViewById(R.id.tv_head_home_challenge);
        mTvHeadHomeStory = (TextView) mHeadView.findViewById(R.id.tv_head_home_story);

        mVpHeadHome.setImageLoader(new BannerImageAdapter());
        mVpHeadHome.setImages(imgs);

        mVpHeadHome.setDelayTime(3000);
        mVpHeadHome.start();

        mTvHeadHomeChallenge.setOnClickListener(this);
        mTvHeadHomeStory.setOnClickListener(this);
    }

    private void getData() {
        datas= new ArrayList<>();
        for (int i = 0; i < 32; i ++) {
            DynamicInfo dynamicInfo = new DynamicInfo();
            dynamicInfo.setLeft1("什么鬼" + i);
            datas.add(dynamicInfo);
        }

        imgs = new ArrayList<>();
        imgs.add(R.drawable.photo);
        imgs.add(R.drawable.photo2);
        imgs.add(R.drawable.photo3);
        imgs.add(R.drawable.photo6);
    }


    @Override
    public void onStart() {
        super.onStart();
        mVpHeadHome.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mVpHeadHome.stopAutoPlay();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_home_challenge:
                mActivity.startActivity(new Intent(mActivity,ChallengeActivity.class));

                break;
            case R.id.tv_head_home_story:

                break;
        }
    }
}
