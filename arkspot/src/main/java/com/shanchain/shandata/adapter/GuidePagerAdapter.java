package com.shanchain.shandata.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shanchain.shandata.ui.view.fragment.GuideFragmentFirst;
import com.shanchain.shandata.ui.view.fragment.GuideFragmentFour;
import com.shanchain.shandata.ui.view.fragment.GuideFragmentSecond;
import com.shanchain.shandata.ui.view.fragment.GuideFragmentThird;

/**
 * Created by zhoujian on 2017/12/4.
 */

public class GuidePagerAdapter extends FragmentPagerAdapter {

    public GuidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new GuideFragmentFirst();
                break;
            case 1:
                fragment = new GuideFragmentSecond();
                break;
            case 2:
                fragment = new GuideFragmentThird();
                break;
            case 3:
                fragment = new GuideFragmentFour();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

}
