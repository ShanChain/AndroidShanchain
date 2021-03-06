package com.shanchain.shandata.ui.view.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.adapter.StoryPagerAdapter;

import butterknife.Bind;


public class StoryFragment extends BaseFragment {
    @Bind(R.id.tab_story)
    TabLayout mTabStory;
    @Bind(R.id.vp_story)
    ViewPager mVpStory;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_story, null);
    }

    @Override
    public void initData() {
        String[] titles = {"关注","实时","推荐"};
        StoryPagerAdapter adapter = new StoryPagerAdapter(getChildFragmentManager(), titles);
        mVpStory.setOffscreenPageLimit(3);
        mVpStory.setAdapter(adapter);
        mTabStory.setupWithViewPager(mVpStory);
        mVpStory.setCurrentItem(1);
    }

}
