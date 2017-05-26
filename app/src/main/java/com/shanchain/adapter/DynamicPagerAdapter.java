package com.shanchain.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shanchain.mvp.view.fragment.HotFragment;
import com.shanchain.mvp.view.fragment.AttentionFragment;
import com.shanchain.mvp.view.fragment.SameCityFragment;

/**
 * Created by zhoujian on 2017/5/19.
 */

public class DynamicPagerAdapter extends FragmentPagerAdapter {
    String[] titles ;
    public DynamicPagerAdapter(FragmentManager fm , String[] tabName) {
        super(fm);
        titles = tabName;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new HotFragment();
                break;
            case 1:
                fragment = new AttentionFragment();
                break;
            case 2:
                fragment = new SameCityFragment();
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
