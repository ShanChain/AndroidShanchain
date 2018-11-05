package com.shanchain.shandata.ui.view.activity.tasklist;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.StoryPagerAdapter;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseFragment;

import butterknife.Bind;


public class TaskListFragment extends BaseFragment {
    @Bind(R.id.tab_task)
    TabLayout mTabStory;
    @Bind(R.id.vp_story)
    ViewPager mVpStory;

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_task, null);
    }

    @Override
    public void initData() {
        String[] titles = {"任务列表","我的任务"};
        TaskPagerAdapter adapter = new TaskPagerAdapter(getChildFragmentManager(), titles);
        mVpStory.setOffscreenPageLimit(2);
        mVpStory.setAdapter(adapter);
        mTabStory.setupWithViewPager(mVpStory);
        mVpStory.setCurrentItem(1);
    }

}
