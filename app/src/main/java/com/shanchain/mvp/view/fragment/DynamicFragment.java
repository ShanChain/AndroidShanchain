package com.shanchain.mvp.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shanchain.R;
import com.shanchain.adapter.DynamicPagerAdapter;
import com.shanchain.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/5/19.
 * 动态页面
 */

public class DynamicFragment extends BaseFragment {
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.vp_dynamic)
    ViewPager mVpDynamic;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_dynamic, null);
    }

    @Override
    public void initData() {
        init();
    }


    /**
     *  2017/5/19
     *  描述：初始化视图
     *
     */
    private void init() {
        DynamicPagerAdapter adapter = new DynamicPagerAdapter(getChildFragmentManager());
        mVpDynamic.setAdapter(adapter);
        mTab.setupWithViewPager(mVpDynamic);

    }


}
