package com.shanchain.mvp.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shanchain.R;
import com.shanchain.adapter.DynamicPagerAdapter;
import com.shanchain.base.LazyFragment;
import com.shanchain.utils.LogUtils;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/5/19.
 */

public class DynamicFragment extends LazyFragment {
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.vp_dynamic)
    ViewPager mVpDynamic;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_dynamic
                , null);

        return view;
    }

    @Override
    protected void lazyLoad() {
        init();
    }

    /**
     *  2017/5/19
     *  描述：初始化视图
     *
     */
    private void init() {
        LogUtils.d("init=================");
        DynamicPagerAdapter adapter = new DynamicPagerAdapter(getChildFragmentManager());
        if (adapter == null){
            LogUtils.d("??????????????");
        }
        if (mVpDynamic == null){
            LogUtils.d("不会吧");
        }
        mVpDynamic.setAdapter(adapter);
        mTab.setupWithViewPager(mVpDynamic);

    }


}
