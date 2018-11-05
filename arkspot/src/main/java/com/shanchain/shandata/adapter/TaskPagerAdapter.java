package com.shanchain.shandata.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shanchain.shandata.ui.view.fragment.AttentionFragment;
import com.shanchain.shandata.ui.view.fragment.CurrentFragment;
import com.shanchain.shandata.ui.view.fragment.FragmentMyTask;
import com.shanchain.shandata.ui.view.fragment.FragmentTaskList;
import com.shanchain.shandata.ui.view.fragment.RecommendedFragment;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class TaskPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;

    public TaskPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentTaskList();
                break;
            case 1:
                fragment = new FragmentMyTask();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
